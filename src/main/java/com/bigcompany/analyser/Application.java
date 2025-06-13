package com.bigcompany.analyser;

import com.bigcompany.analyser.io.EmployeeCsvReader;
import com.bigcompany.analyser.model.Employee;
import com.bigcompany.analyser.service.OrganizationAnalyzerService;

import java.util.List;

public class Application
{
    public static void main(String[] args) {

        String csvFilePath = "./src/main/resources/employees.csv";

        try {
            System.out.println("Reading employee data from: " + csvFilePath);
            EmployeeCsvReader reader = new EmployeeCsvReader();

            List<Employee> employees = reader.read(csvFilePath);
            System.out.println("Loaded " + employees.size() + " employees\n");

            OrganizationAnalyzerService analyzer = new OrganizationAnalyzerService(employees);

            printSalaryIssues(analyzer.findSalaryIssues());
            printReportingLineIssues(analyzer.findReportingLevelIssues());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printSalaryIssues(List<OrganizationAnalyzerService.SalaryIssue> issues) {
        System.out.println("=== SALARY ANALYSIS ===");

        if (issues.isEmpty()) {
            System.out.println("All managers have appropriate salaries");
        } else {
            System.out.println("Found " + issues.size() + " salary issue(s):");
            for (OrganizationAnalyzerService.SalaryIssue issue : issues) {
                System.out.println("  • " + issue);
            }
        }
        System.out.println();
    }

    private static void printReportingLineIssues(List<OrganizationAnalyzerService.ReportingLineIssue> reportingLevelIssues) {
        System.out.println("=== REPORTING ANALYSIS ===");

        if (reportingLevelIssues == null || reportingLevelIssues.isEmpty()) {
            System.out.println("no issues found in reporting line");
        } else {
            System.out.println("Found " + reportingLevelIssues.size() + " reporting level issue(s):");
            for (OrganizationAnalyzerService.ReportingLineIssue issue : reportingLevelIssues) {
                System.out.println("  • " + issue);
            }
        }
        System.out.println();
    }
}
