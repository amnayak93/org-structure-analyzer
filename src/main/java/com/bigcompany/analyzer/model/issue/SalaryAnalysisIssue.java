package com.bigcompany.analyzer.model.issue;

import com.bigcompany.analyzer.model.Employee;

import java.math.BigDecimal;

public class SalaryAnalysisIssue extends AnalysisIssue {

    private final IssueType issueType;
    private final BigDecimal margin;
    private final BigDecimal expectedMinSalary;
    private final BigDecimal expectedMaxSalary;

    public SalaryAnalysisIssue(Employee employee, IssueType issueType, BigDecimal margin, BigDecimal expectedMinSalary, BigDecimal expectedMaxSalary) {
        super(employee);
        this.issueType = issueType;
        this.margin = margin;
        this.expectedMinSalary = expectedMinSalary;
        this.expectedMaxSalary = expectedMaxSalary;
    }

    public static SalaryAnalysisIssue underpaidManager(Employee manager, BigDecimal margin, BigDecimal expectedMinSalary, BigDecimal expectedMaxSalary) {
        return new SalaryAnalysisIssue(manager, IssueType.SALARY_UNDERPAID, margin, expectedMinSalary, expectedMaxSalary);
    }

    public static SalaryAnalysisIssue overPaidManager(Employee manager, BigDecimal margin, BigDecimal expectedMinSalary, BigDecimal expectedMaxSalary) {
        return new SalaryAnalysisIssue(manager, IssueType.SALARY_OVERPAID, margin, expectedMinSalary, expectedMaxSalary);
    }

    public BigDecimal getMargin() {
        return margin;
    }

    public BigDecimal getExpectedMinSalary() {
        return expectedMinSalary;
    }

    public BigDecimal getExpectedMaxSalary() {
        return expectedMaxSalary;
    }

    @Override
    public IssueType getIssueType() {
        return issueType;
    }

    @Override
    public String toString() {
        return getDescription();
    }

    @Override
    public String getDescription() {
        String salaryIssue = issueType == IssueType.SALARY_UNDERPAID ? "underpaid" : "overpaid";
        return String.format("%s is %s by $%s (Expected Salary Range: $%s - $%s, Current Salary: $%s)",
                employee.getFullName(), salaryIssue, margin, expectedMinSalary, expectedMaxSalary, employee.getSalary());
    }
}
