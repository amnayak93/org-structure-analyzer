package com.bigcompany.analyzer.service.impl;

import com.bigcompany.analyzer.model.Employee;
import com.bigcompany.analyzer.model.issue.SalaryAnalysisIssue;
import com.bigcompany.analyzer.rule.SalaryRules;
import com.bigcompany.analyzer.service.AnalysisService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SalaryAnalysisService implements AnalysisService<SalaryAnalysisIssue> {
    private final SalaryRules salaryRules;

    public SalaryAnalysisService(SalaryRules salaryRules) {
        this.salaryRules = salaryRules;
    }

    @Override
    public List<SalaryAnalysisIssue> analyseIssues(List<Employee> employees) {
        return employees.stream()
                .filter(Employee::hasDirectReports)
                .map(this::analyseSalaryIssue)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<SalaryAnalysisIssue> analyseSalaryIssue(Employee manager) {
        BigDecimal averageReportingEmpSalary = manager.getAverageReportingEmployeesSalary();
        BigDecimal minExpectedSalary = averageReportingEmpSalary.multiply(BigDecimal.valueOf(salaryRules.minSalaryMultiplier()));
        BigDecimal maxExpectedSalary = averageReportingEmpSalary.multiply(BigDecimal.valueOf(salaryRules.maxSalaryMultiplier()));
        BigDecimal currentSalary = manager.getSalary();

        if (currentSalary.compareTo(minExpectedSalary) < 0) {
            BigDecimal margin = minExpectedSalary.subtract(currentSalary);
            return Optional.of(SalaryAnalysisIssue.underpaidManager(manager, margin, minExpectedSalary, maxExpectedSalary));
        } else if (currentSalary.compareTo(maxExpectedSalary) > 0) {
            BigDecimal margin = currentSalary.subtract(maxExpectedSalary);
            return Optional.of(SalaryAnalysisIssue.overPaidManager(manager, margin, minExpectedSalary, maxExpectedSalary));
        }

        return Optional.empty();
    }
}
