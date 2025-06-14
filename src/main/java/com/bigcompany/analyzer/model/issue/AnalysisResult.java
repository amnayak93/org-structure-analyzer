package com.bigcompany.analyzer.model.issue;

import java.util.List;

public record AnalysisResult(List<SalaryAnalysisIssue> salaryAnalysisIssues,
                             List<ReportingLineAnalysisIssue> reportingLineAnalysisIssues) {
}
