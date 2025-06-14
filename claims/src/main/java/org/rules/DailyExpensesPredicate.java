package org.rules;

import org.claims.Claim;
import org.resources.ResourcesService;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class DailyExpensesPredicate implements Predicate<Claim> {

    private final BigDecimal dailyLimit;
    private final ResourcesService resourcesService;

    public DailyExpensesPredicate(BigDecimal dailyLimit, ResourcesService resourcesService) {
        this.dailyLimit = dailyLimit;
        this.resourcesService = resourcesService;
    }

    @Override
    public boolean test(Claim claim) {
        return resourcesService.get().budget().add(claim.amount()).compareTo(dailyLimit) <= 0;
    }
}
