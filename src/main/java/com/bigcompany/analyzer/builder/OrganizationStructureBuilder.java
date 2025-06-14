package com.bigcompany.analyzer.builder;

import com.bigcompany.analyzer.model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrganizationStructureBuilder {

    public static void buildOrgStructure(List<Employee> employees) {
        Map<Long, Employee> employeeMap = buildEmployeeMap(employees);

        employees.forEach(employee ->
                Optional.ofNullable(employeeMap.get(employee.getManagerId()))
                        .ifPresent(manager -> manager.addReportingEmployee(employee))
        );
    }

    public static Map<Long, Integer> calculateHierarchyLevels(List<Employee> employees) {
        Map<Long, Integer> employeeHierarchyLevelMap = new HashMap<>();
        Employee ceo = findCEO(employees);
        List<Employee> currentEmployeeLevel = List.of(ceo);

        int level = 0;
        while (!currentEmployeeLevel.isEmpty()) {
            for (Employee employee : currentEmployeeLevel) {
                employeeHierarchyLevelMap.put(employee.getId(), level);
            }
            currentEmployeeLevel = currentEmployeeLevel
                    .stream()
                    .flatMap(employee -> employee.getReportingEmployees().stream())
                    .toList();
            level++;
        }
        return employeeHierarchyLevelMap;
    }

    private static Employee findCEO(List<Employee> employees) {
        return employees.stream()
                .filter(Employee::isCEO)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No CEO found in the Company"));
    }

    public static Map<Long, Employee> buildEmployeeMap(List<Employee> employees) {
        return employees.stream()
                .collect(
                        Collectors.toMap(
                                Employee::getId,
                                employee -> employee,
                                (existing, replacement) -> existing
                        )
                );
    }
}
