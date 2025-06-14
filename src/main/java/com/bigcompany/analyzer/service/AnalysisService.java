package com.bigcompany.analyzer.service;

import com.bigcompany.analyzer.model.Employee;
import com.bigcompany.analyzer.model.issue.AnalysisIssue;

import java.util.List;

public interface AnalysisService<T extends AnalysisIssue> {
    List<T> analyseIssues(List<Employee> employees);
}
