package com.bigcompany.analyzer.service.impl;

import com.bigcompany.analyzer.model.OrganizationStructureBuilder;
import com.bigcompany.analyzer.model.Employee;
import com.bigcompany.analyzer.model.issue.IssueType;
import com.bigcompany.analyzer.model.issue.ReportingLineAnalysisIssue;
import com.bigcompany.analyzer.rule.ReportingLineRules;
import com.bigcompany.analyzer.service.AnalysisService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportingLineAnalysisService implements AnalysisService<ReportingLineAnalysisIssue> {
    private final ReportingLineRules reportingLineRules;

    public ReportingLineAnalysisService(ReportingLineRules reportingLineRules) {
        this.reportingLineRules = reportingLineRules;
    }

    @Override
    public List<ReportingLineAnalysisIssue> analyseIssues(List<Employee> employees) {
        Map<Long, Employee> employeeMap = OrganizationStructureBuilder.buildEmployeeMap(employees);
        Map<Long, Integer> reportingLevelMap = OrganizationStructureBuilder.calculateHierarchyLevels(employees);

        return reportingLevelMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > reportingLineRules.maxHierarchyLevel())
                .map(entry -> new ReportingLineAnalysisIssue(
                        employeeMap.get(entry.getKey()),
                        IssueType.REPORTING_LINE_ISSUE,
                        entry.getValue(),
                        entry.getValue() - reportingLineRules.maxHierarchyLevel()
                ))
                .collect(Collectors.toList());
    }
}
