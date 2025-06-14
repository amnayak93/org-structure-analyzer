package com.bigcompany.analyzer;

import com.bigcompany.analyzer.io.EmployeeCsvReader;
import com.bigcompany.analyzer.model.Employee;
import com.bigcompany.analyzer.model.issue.AnalysisResult;
import com.bigcompany.analyzer.model.issue.ReportingLineAnalysisIssue;
import com.bigcompany.analyzer.model.issue.SalaryAnalysisIssue;
import com.bigcompany.analyzer.service.OrganizationStructureAnalyzer;

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

            OrganizationStructureAnalyzer analyzer = OrganizationStructureAnalyzer.withDefaultRules();
            AnalysisResult analysisResult = analyzer.analyze(employees);

            printSalaryIssues(analysisResult.salaryAnalysisIssues());
            printReportingLineIssues(analysisResult.reportingLineAnalysisIssues());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printSalaryIssues(List<SalaryAnalysisIssue> issues) {
        System.out.println("=== SALARY ANALYSIS ===");

        if (issues.isEmpty()) {
            System.out.println("All managers have appropriate salaries");
        } else {
            System.out.println("Found " + issues.size() + " salary issue(s):");
            for (SalaryAnalysisIssue issue : issues) {
                System.out.println("  • " + issue);
            }
        }
        System.out.println();
    }

    private static void printReportingLineIssues(List<ReportingLineAnalysisIssue> reportingLevelIssues) {
        System.out.println("=== REPORTING ANALYSIS ===");

        if (reportingLevelIssues == null || reportingLevelIssues.isEmpty()) {
            System.out.println("no issues found in reporting line");
        } else {
            System.out.println("Found " + reportingLevelIssues.size() + " reporting level issue(s):");
            for (ReportingLineAnalysisIssue issue : reportingLevelIssues) {
                System.out.println("  • " + issue);
            }
        }
        System.out.println();
    }
}
