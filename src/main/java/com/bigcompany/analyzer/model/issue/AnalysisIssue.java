package com.bigcompany.analyzer.model.issue;

import com.bigcompany.analyzer.model.Employee;

public abstract class AnalysisIssue {

    protected final Employee employee;

    protected AnalysisIssue(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public abstract IssueType getIssueType();

    public abstract String getDescription();
}
