package com.bigcompany.analyzer.service;

import com.bigcompany.analyzer.model.Employee;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Deprecated
public class OrganizationAnalyzerService {
    private final Map<Long, Employee> employeeMap;
    private static final BigDecimal MIN_SALARY_MULTIPLIER = new BigDecimal("1.2");
    private static final BigDecimal MAX_SALARY_MULTIPLIER = new BigDecimal("1.5");
    private static final Integer MAX_REPORTING_LINE = 5; //including the CEO

    public OrganizationAnalyzerService(List<Employee> employees) {
        this.employeeMap = employees.stream()
                .collect(Collectors.toMap(
                        Employee::getId,
                        employee -> employee,
                        (existing, replacement) -> existing
                ));
        //calculate and prepare the list of reporting employees for each employee
        organizeReportingEmployees();
    }

    private void organizeReportingEmployees() {
        employeeMap.values()
                .forEach(employee ->
                        Optional.ofNullable(employeeMap.get(employee.getManagerId()))
                                .ifPresent(manager -> manager.addReportingEmployee(employee))
                );
    }

    public List<SalaryIssue> findSalaryIssues() {
        return employeeMap.values()
                .stream()
                .filter(Employee::hasDirectReports)
                .map(this::createSalaryAnalysis)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    //start with ceo and drill down
    public List<ReportingLineIssue> findReportingLevelIssues() {
        Employee ceo = findCEO();

        List<Employee> currentLevel = List.of(ceo);
        // this captures the level an employee is in the organization hierarchy, map of employee ID and his/her org level
        Map<Long, Integer> levels = new HashMap<>();

        int level = 0;

        while (!currentLevel.isEmpty()) {
            int finalLevel = level;
            currentLevel.forEach(employee -> levels.put(employee.getId(), finalLevel));
            currentLevel = currentLevel.stream()
                    .flatMap(employee -> employee.getReportingEmployees().stream())
                    .toList();
            level++;
        }

        return levels.entrySet()
                .stream()
                .filter(entry -> entry.getValue() > MAX_REPORTING_LINE)
                .map(entry -> createReportingIssue(
                        employeeMap.get(entry.getKey()),
                        entry.getValue()
                ))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<ReportingLineIssue> createReportingIssue(Employee employee, Integer value) {
        int extraLevels;
        if (value > MAX_REPORTING_LINE) {
            extraLevels = value - MAX_REPORTING_LINE;
            return Optional.of(new ReportingLineIssue(employee, extraLevels));
        }
        return Optional.empty();
    }

    private Employee findCEO() {
        return employeeMap.values()
                .stream()
                .filter(Employee::isCEO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("CEO not found in organization"));
    }

    // As of now this is a simple salary analysis, it does not take into
    // effect the cascading nature of the salary increases or decreases
    // For e,g. if Martin's current salary is 45000, and he is underpaid
    // by 24000, then his adjusted salary won't be taken into consideration
    // to calculate salary estimates for his manager.
    // His manager's salary adjustments will be made when application is run again
    // with csv updated with updated salaries made after previous application run
    private Optional<SalaryIssue> createSalaryAnalysis(Employee employee) {
        BigDecimal avgReportingEmpSalary = employee.getAverageReportingEmployeesSalary();
        BigDecimal minimumSalary = avgReportingEmpSalary.multiply(MIN_SALARY_MULTIPLIER);
        BigDecimal maximumSalary = avgReportingEmpSalary.multiply(MAX_SALARY_MULTIPLIER);
        BigDecimal salary = employee.getSalary();

        if (salary.compareTo(minimumSalary) < 0) {
            BigDecimal belowMargin = minimumSalary.subtract(salary);
            return Optional.of(new SalaryIssue(employee, "Underpaid", belowMargin, minimumSalary, maximumSalary));
        } else if (salary.compareTo(maximumSalary) > 0) {
            BigDecimal aboveMargin = salary.subtract(maximumSalary);
            return Optional.of(new SalaryIssue(employee, "Overpaid", aboveMargin, minimumSalary, maximumSalary));
        }
        return Optional.empty();
    }

    public record SalaryIssue(Employee employee, String type, BigDecimal amount, BigDecimal expectedMin,
                              BigDecimal expectedMax) {
        @Override
        public String toString() {
            return String.format("%s is %s by $%s (Expected min: $%s, Expected max: $%s, Current: $%s)",
                    employee.getFullName(), type.toLowerCase(), amount,
                    expectedMin, expectedMax, employee.getSalary());
        }
    }

    public record ReportingLineIssue(Employee employee, Integer levels) {
        @Override
        public String toString() {
            return String.format("%s has %s extra levels",
                    employee.getFullName(), levels);
        }
    }
}
