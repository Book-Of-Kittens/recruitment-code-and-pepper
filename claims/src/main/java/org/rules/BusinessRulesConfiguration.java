package org.rules;

import org.claims.Claim;
import org.claims.ClaimType;
import org.resources.ResourcesService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class BusinessRulesConfiguration {

    public static Map<ClaimType, Comparator<Claim>> comparatorsByType = Map.of(
            ClaimType.MEDICAL, ClaimComparators.forMedical(),
            ClaimType.VEHICLE, ClaimComparators.forVehicle(),
            ClaimType.PROPERTY, ClaimComparators.forProperty());

    public static Predicate<Claim> commonApprovalPredicate(ResourcesService resourcesService) {
        return new DailyComplexityPredicate(2, resourcesService)
                .and(new DailyExpensesPredicate(BigDecimal.valueOf(15000L), resourcesService))
                .and(new NoDuplicateIdPredicate(resourcesService));
    }

    public static List<Predicate<Claim>> ofType(ClaimType claimType) {
        return List.of(claim -> claimType == claim.type());
    }

    /* TODO: make things package local */
}
