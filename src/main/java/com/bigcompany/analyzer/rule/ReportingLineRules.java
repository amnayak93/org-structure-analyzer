package com.bigcompany.analyzer.rule;

public record ReportingLineRules(int maxHierarchyLevel) {

    private static final int DEFAULT_MAX_HIERARCHY_LEVEL = 5; //including CEO

    public static ReportingLineRules getDefaultRules() {
        return new ReportingLineRules(DEFAULT_MAX_HIERARCHY_LEVEL);
    }
}
