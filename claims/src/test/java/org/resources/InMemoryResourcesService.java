package org.resources;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class InMemoryResourcesService implements ResourcesService {

    Set<String> processedIds = new HashSet<>();
    private BigDecimal commonBudget = BigDecimal.ZERO;
    private int commonHighComplexityCounter = 0;

    @Override
    public boolean isIdProcessed(String id) {
        return processedIds.contains(id);
    }

    @Override
    public void addProcessedId(String id) {
        processedIds.add(id);
    }

    @Override
    public BigDecimal getDailyExpenses() {
        return commonBudget;
    }

    @Override
    public int getDailyComplexClaimsCounter() {
        return commonHighComplexityCounter;
    }

    @Override
    public void updateDailyComplexClaimsCounter() {
        commonHighComplexityCounter++;
    }

    @Override
    public void resetComplexityCounter() {
        commonHighComplexityCounter = 0;
    }

    @Override
    public void addExpenses(BigDecimal amount) {
        commonBudget = commonBudget.add(amount);
    }

    @Override
    public void resetDailyExpenses() {
        commonBudget = BigDecimal.ZERO;
    }
    /* TODO: index data by day instead? */
}
