package com.bigcompany.analyzer.model.issue;

import com.bigcompany.analyzer.model.Employee;

public class ReportingLineAnalysisIssue extends AnalysisIssue {

    private final IssueType issueType;
    private final int currentLevel;
    private final int extraLevels;

    public ReportingLineAnalysisIssue(Employee employee, IssueType issueType, int currentLevel, int extraLevels) {
        super(employee);
        this.issueType = issueType;
        this.currentLevel = currentLevel;
        this.extraLevels = extraLevels;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getExtraLevels() {
        return extraLevels;
    }

    @Override
    public IssueType getIssueType() {
        return issueType;
    }

    @Override
    public String getDescription() {
        return String.format("%s current level is %s and it has %s extra levels", employee.getFullName(), currentLevel, extraLevels);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
