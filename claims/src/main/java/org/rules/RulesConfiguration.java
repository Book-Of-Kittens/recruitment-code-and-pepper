package org.rules;

import org.claims.Claim;
import org.claims.ClaimType;
import org.resources.ResourcesService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RulesConfiguration {

    public static Map<ClaimType, Comparator<Claim>> getComparatorsByType() {
        return Map.of(ClaimType.MEDICAL, ClaimComparators.forMedical(),
                ClaimType.VEHICLE, ClaimComparators.forVehicle(),
                ClaimType.PROPERTY, ClaimComparators.forProperty());
    }

    public static List<UpdatablePredicate> getPredicateList(ResourcesService resourcesService) {
        return List.of(new DailyComplexityPredicate(2, resourcesService),
                new DailyExpensesPredicate(BigDecimal.valueOf(15000L), resourcesService),
                new NoDuplicateIdPredicate(resourcesService));
    }


}
