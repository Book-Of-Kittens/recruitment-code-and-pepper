package org.rules;

import org.claims.Claim;
import org.claims.ComplexityLevel;
import org.resources.ResourcesService;

import java.util.function.Predicate;

public class DailyComplexityPredicate implements UpdatablePredicate {

    final private int limit;
    private final ResourcesService resourcesService;

    public DailyComplexityPredicate(int dailyComplexClaimLimit, ResourcesService resourcesService) {
        this.limit = dailyComplexClaimLimit;
        this.resourcesService = resourcesService;
    }

    private static boolean isComplex(Claim claim) {
        return ComplexityLevel.HIGH == claim.complexity();
    }

    @Override
    public void updateResources(Claim claim) {
        if (isComplex(claim)) {
            resourcesService.updateDailyComplexClaimsCounter();
        }
    }

    @Override
    public void resetResource() {
        resourcesService.resetComplexityCounter();
    }

    @Override
    public Predicate<Claim> getPredicate() {
        return claim -> !isComplex(claim) || resourcesService.getDailyComplexClaimsCounter() < limit;
    }

}
