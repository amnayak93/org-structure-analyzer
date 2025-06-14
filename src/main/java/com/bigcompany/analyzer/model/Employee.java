package com.bigcompany.analyzer.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Employee {
    private final Long id;
    private final String firstName;
    private final String lastName;
    private final BigDecimal salary;
    private final Long managerId;
    private final List<Employee> reportingEmployees = new ArrayList<>();

    public Employee(Long id, String firstName, String lastName, BigDecimal salary, Long managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public Long getManagerId() {
        return managerId;
    }

    public String getFullName(){
        return firstName.concat(" ").concat(lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(id, employee.id) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && Objects.equals(salary, employee.salary) && Objects.equals(managerId, employee.managerId) && Objects.equals(reportingEmployees, employee.reportingEmployees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public boolean hasDirectReports() {
        return !reportingEmployees.isEmpty();
    }

    public BigDecimal getAverageReportingEmployeesSalary() {
        if (!hasDirectReports()) {
            return BigDecimal.ZERO;
        }

        BigDecimal total = reportingEmployees
                .stream()
                .map(Employee::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(reportingEmployees.size()), 2, RoundingMode.HALF_UP);
    }

    public boolean isCEO() {
        return getManagerId() == null;
    }

    void addReportingEmployee(Employee employee) {
        reportingEmployees.add(employee);
    }

    public List<Employee> getReportingEmployees() {
        return reportingEmployees;
    }
}
