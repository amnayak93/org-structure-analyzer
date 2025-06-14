package com.bigcompany.analyzer.service;

import com.bigcompany.analyzer.builder.OrganizationStructureBuilder;
import com.bigcompany.analyzer.model.Employee;
import com.bigcompany.analyzer.model.issue.AnalysisResult;
import com.bigcompany.analyzer.model.issue.ReportingLineAnalysisIssue;
import com.bigcompany.analyzer.model.issue.SalaryAnalysisIssue;
import com.bigcompany.analyzer.rule.OrganizationRules;
import com.bigcompany.analyzer.service.impl.ReportingLineAnalysisService;
import com.bigcompany.analyzer.service.impl.SalaryAnalysisService;

import java.util.List;

public class OrganizationStructureAnalyzer {
    private final AnalysisService<SalaryAnalysisIssue> salaryAnalysisService;
    private final AnalysisService<ReportingLineAnalysisIssue> reportingLineAnalysisService;

    public OrganizationStructureAnalyzer(OrganizationRules organizationRules) {
        this.salaryAnalysisService = new SalaryAnalysisService(organizationRules.salaryRules());
        this.reportingLineAnalysisService = new ReportingLineAnalysisService(organizationRules.reportingLineRules());
    }

    public static OrganizationStructureAnalyzer withDefaultRules() {
        return new OrganizationStructureAnalyzer(OrganizationRules.defaultRules());
    }

    public AnalysisResult analyze(List<Employee> employees) {
        OrganizationStructureBuilder.buildOrgStructure(employees);
        List<SalaryAnalysisIssue> salaryAnalysisIssues = salaryAnalysisService.analyseIssues(employees);
        List<ReportingLineAnalysisIssue> reportingLineAnalysisIssues = reportingLineAnalysisService.analyseIssues(employees);

        List<SalaryAnalysisIssue> underPaidSalaryIssues = salaryAnalysisIssues
                .stream()
                .filter(salaryAnalysisIssue -> salaryAnalysisIssue.getIssueType().name().equals("SALARY_UNDERPAID"))
                .toList();

        List<SalaryAnalysisIssue> overPaidSalaryIssues = salaryAnalysisIssues
                .stream()
                .filter(salaryAnalysisIssue -> salaryAnalysisIssue.getIssueType().name().equals("SALARY_OVERPAID"))
                .toList();

        return new AnalysisResult(underPaidSalaryIssues, overPaidSalaryIssues, reportingLineAnalysisIssues);
    }
}
