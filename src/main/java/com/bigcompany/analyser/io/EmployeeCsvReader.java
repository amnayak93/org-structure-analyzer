package com.bigcompany.analyser.io;

import com.bigcompany.analyser.model.Employee;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCsvReader implements FileReader<Employee> {

    @Override
    public List<Employee> read(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
            String line = reader.readLine();
            if (line == null) {
                throw new IllegalArgumentException("File is empty");
            }

            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Employee employee = parseLine(line);
                    employees.add(employee);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error on line " + lineNumber + ": " + e.getMessage());
                }
            }
        }

        if (employees.isEmpty()) {
            throw new IllegalArgumentException("No employees found in file");
        }

        return employees;
    }

    Employee parseLine(String line) {
        String[] elements = line.split(",", -1);
        if (elements.length != 5) {
            throw new IllegalArgumentException("Expected 5 columns, but found " + elements.length);
        }

        try {
            Long id = parseLong(elements[0].trim());
            String firstName = parseString(elements[1].trim(), "First name");
            String lastName = parseString(elements[2].trim(), "Last name");
            BigDecimal salary = parseBigDecimal(elements[3].trim());
            Long managerId = parseOptionalLong(elements[4].trim());

            return new Employee(id, firstName, lastName, salary, managerId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format: " + e.getMessage());
        }
    }

    private Long parseLong(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Employee ID" + " cannot be empty");
        }
        long result = Long.parseLong(value);
        if (result <= 0) {
            throw new IllegalArgumentException("ID" + " must be positive");
        }
        return result;
    }

    private String parseString(String value, String fieldName) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return value;
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Salary" + " cannot be empty");
        }
        BigDecimal result = new BigDecimal(value);
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Salary" + " cannot be negative");
        }
        return result;
    }

    private Long parseOptionalLong(String value) {
        return value.isEmpty() ? null : Long.parseLong(value);
    }
}
