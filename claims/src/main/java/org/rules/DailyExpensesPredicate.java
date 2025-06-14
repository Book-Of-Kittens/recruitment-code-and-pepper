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
    public Predicate<Claim> predicate() {
        return claim -> resourcesService.get().budget().add(claim.amount()).compareTo(dailyLimit) >= 0;
    }
}
