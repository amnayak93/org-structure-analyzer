package com.bigcompany.analyzer.model.issue;

import java.util.List;

public record AnalysisResult(List<SalaryAnalysisIssue> underPaidSalaryIssues,
                             List<SalaryAnalysisIssue> overPaidSalaryIssues, List<ReportingLineAnalysisIssue> reportingLineAnalysisIssues) {
}
