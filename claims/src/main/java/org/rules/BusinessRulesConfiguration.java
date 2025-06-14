package org.rules;

import org.claims.Claim;
import org.claims.ClaimType;
import org.resources.ResourcesService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BusinessRulesConfiguration {

    public static Map<ClaimType, Comparator<Claim>> comparatorsByType = Map.of(
            ClaimType.MEDICAL, ClaimComparators.forMedical(),
            ClaimType.VEHICLE, ClaimComparators.forVehicle(),
            ClaimType.PROPERTY, ClaimComparators.forProperty());

    public static List<UpdatablePredicate> approvalPredicates(ResourcesService resourcesService) {
        return List.of(new DailyComplexityPredicate(2, resourcesService),
                new DailyExpensesPredicate(BigDecimal.valueOf(15000L), resourcesService),
                new NoDuplicateIdPredicate(resourcesService));
    }

    public static List<UpdatablePredicate> all() {
        return List.of(new StatelessPredicate(_ -> true));
    }

    public static List<UpdatablePredicate> ofType(ClaimType claimType) {
        return List.of(new StatelessPredicate(claim -> claimType == claim.type()));
    }

    /* TODO: make things package local */
}
