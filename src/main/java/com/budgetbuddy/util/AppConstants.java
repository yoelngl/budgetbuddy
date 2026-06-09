package com.budgetbuddy.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class AppConstants {

    public static final String APP_NAME    = "BudgetBuddy";
    public static final String APP_VERSION = "1.0.0";
    public static final String CURRENCY    = "IDR";

    public static final int DASHBOARD_RECENT_LIMIT = 5;

    public static final String TX_TYPE_INCOME  = "INCOME";
    public static final String TX_TYPE_EXPENSE = "EXPENSE";

    public static final List<String> INCOME_SOURCES = Collections.unmodifiableList(
        Arrays.asList("Gaji", "Freelance", "Investasi", "Bisnis", "Bonus", "Lainnya")
    );

    private AppConstants() {
        throw new UnsupportedOperationException("Constants class");
    }
}
