package com.bigcompany.analyzer;

import com.bigcompany.analyzer.model.Employee;
import com.bigcompany.analyzer.model.issue.AnalysisResult;
import com.bigcompany.analyzer.model.issue.ReportingLineAnalysisIssue;
import com.bigcompany.analyzer.model.issue.SalaryAnalysisIssue;
import com.bigcompany.analyzer.rule.OrganizationRules;
import com.bigcompany.analyzer.service.OrganizationStructureAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationStructureAnalyzerTest {

    private OrganizationStructureAnalyzer analyzer;
    private AnalysisResult result;

    @BeforeEach
    void setUp() {
        OrganizationRules organizationRules = OrganizationRules.defaultRules();
        analyzer = new OrganizationStructureAnalyzer(organizationRules);
        List<Employee> testEmployees = createTestEmployees();
        result = analyzer.analyze(testEmployees);
    }

    private List<Employee> createTestEmployees() {
        return Arrays.asList(
                new Employee(123L, "Joe", "Doe", new BigDecimal("60000"), null),

                // level 1
                new Employee(124L, "Martin", "Chekov", new BigDecimal("45000"), 123L),
                new Employee(125L, "Bob", "Ronstad", new BigDecimal("47000"), 123L),
                new Employee(306L, "Mo", "Salah", new BigDecimal("55000"), 123L),

                // level 2
                new Employee(300L, "Alice", "Hasacat", new BigDecimal("50000"), 124L),
                new Employee(309L, "Jon", "Snow", new BigDecimal("65000"), 124L),

                // level 3
                new Employee(305L, "Brett", "Hardleaf", new BigDecimal("34000"), 300L),
                new Employee(307L, "Eden", "Hazard", new BigDecimal("32000"), 300L),
                new Employee(308L, "Phoebe", "Buffay", new BigDecimal("38000"), 300L),
                new Employee(313L, "Chandler", "Bing", new BigDecimal("29000"), 309L),

                // level 4
                new Employee(310L, "Harry", "Potter", new BigDecimal("28000"), 305L),

                // level 5
                new Employee(311L, "Cristiano", "Ronaldo", new BigDecimal("31000"), 310L),

                // level 6
                new Employee(312L, "Lionel", "Messi", new BigDecimal("29000"), 311L)
        );
    }

    @Test
    @DisplayName("Should identify underpaid managers")
    void testShouldIdentifyUnderpaidManagers() {
        List<SalaryAnalysisIssue> underpaidManagers = result.salaryAnalysisIssues()
                .stream()
                .filter(salaryAnalysisIssue -> salaryAnalysisIssue.getIssueType().name().equals("SALARY_UNDERPAID"))
                .toList();

        assertFalse(underpaidManagers.isEmpty());

        assertTrue(underpaidManagers.stream()
                .anyMatch(salaryAnalysisIssue -> salaryAnalysisIssue.getEmployee().getFullName().equals("Harry Potter")));
    }

    @Test
    @DisplayName("Should identify overpaid managers")
    void testShouldIdentifyOverPaidManagers() {
        List<SalaryAnalysisIssue> overpaidManagers = result.salaryAnalysisIssues()
                .stream()
                .filter(salaryAnalysisIssue -> salaryAnalysisIssue.getIssueType().name().equals("SALARY_OVERPAID"))
                .toList();

        assertFalse(overpaidManagers.isEmpty());

        assertTrue(overpaidManagers.stream()
                .anyMatch(salaryAnalysisIssue -> salaryAnalysisIssue.getEmployee().getFullName().equals("Jon Snow")));
    }

    @Test
    @DisplayName("Should identify reporting line issues")
    void testShouldIdentifyReportingLineIssues() {
        List<ReportingLineAnalysisIssue> reportingLineAnalysisIssues = result.reportingLineAnalysisIssues();
        assertFalse(reportingLineAnalysisIssues.isEmpty());
        assertEquals(reportingLineAnalysisIssues.size(), 1);
        assertTrue(reportingLineAnalysisIssues.stream()
                .anyMatch(reportingLineAnalysisIssue -> reportingLineAnalysisIssue.getEmployee().getFullName().equals("Lionel Messi")));
    }
}
