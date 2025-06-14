package org.rules;

import org.claims.Claim;
import org.resources.ResourcesService;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class DailyExpensesPredicate implements UpdatablePredicate {

    private final BigDecimal dailyLimit;
    private final ResourcesService resourcesService;

    public DailyExpensesPredicate(BigDecimal dailyLimit, ResourcesService resourcesService) {
        this.dailyLimit = dailyLimit;
        this.resourcesService = resourcesService;
    }

    @Override
    public void updateResources(Claim claim) {
        resourcesService.addExpenses(claim.amount());
    }

    @Override
    public void resetResource() {
        resourcesService.resetDailyExpenses();
    }

    @Override
    public Predicate<Claim> predicate() {
        return claim -> resourcesService.getDailyExpenses().add(claim.amount()).compareTo(dailyLimit) >= 0;
    }
}
