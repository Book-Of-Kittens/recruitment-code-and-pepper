package org.resources;

import java.math.BigDecimal;

public interface ResourcesService {

    boolean isIdProcessed(String id);

    void addProcessedId(String id);

    BigDecimal getDailyExpenses();

    void addExpenses(BigDecimal amount);

    void resetDailyExpenses();

    int getDailyComplexClaimsCounter();

    void updateDailyComplexClaimsCounter();

    void resetComplexityCounter();

}
