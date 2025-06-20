package org.rules;

import org.claims.Claim;
import org.claims.ComplexityLevel;
import org.resources.ResourcesService;

import java.util.function.Predicate;

public class DailyComplexityPredicate implements Predicate<Claim> {

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
    public boolean test(Claim claim) {

        return (!isComplex(claim)) || (resourcesService.get().highComplexityCounter() < limit);
    }
}
