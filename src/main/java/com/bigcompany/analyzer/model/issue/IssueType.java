package com.bigcompany.analyzer.model.issue;

public enum IssueType {

    SALARY_UNDERPAID("Salary issue - underpaid manager"),
    SALARY_OVERPAID("Salary issue - overpaid manager"),
    REPORTING_LINE_ISSUE("Reporting line issue - employee reporting line exceeds company's threshold");

    private final String issueDescription;

    IssueType(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getIssueDescription() {
        return issueDescription;
    }
}
