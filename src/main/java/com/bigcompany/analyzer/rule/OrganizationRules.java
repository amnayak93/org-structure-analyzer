package com.bigcompany.analyzer.rule;

public record OrganizationRules(SalaryRules salaryRules, ReportingLineRules reportingLineRules) {

    public static OrganizationRules defaultRules() {
        return new OrganizationRules(SalaryRules.getDefaultRules(), ReportingLineRules.getDefaultRules());
    }
}