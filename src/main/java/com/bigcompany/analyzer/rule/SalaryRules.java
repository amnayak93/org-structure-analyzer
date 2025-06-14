package com.bigcompany.analyzer.rule;

public record SalaryRules(double minSalaryMultiplier, double maxSalaryMultiplier) {

    private final static double DEFAULT_MIN_SALARY_MULTIPLIER = 1.2;
    private final static double DEFAULT_MAX_SALARY_MULTIPLIER = 1.5;

    public static SalaryRules getDefaultRules() {
        return new SalaryRules(DEFAULT_MIN_SALARY_MULTIPLIER, DEFAULT_MAX_SALARY_MULTIPLIER);
    }
}
