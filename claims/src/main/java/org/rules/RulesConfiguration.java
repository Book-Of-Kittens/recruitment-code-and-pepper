package org.rules;

import org.claims.Claim;
import org.resources.ResourcesService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RulesConfiguration {

    public static Map<String, Comparator<Claim>> getComparatorsByType(){
        return Map.of(ClaimType.MEDICAL.name(), ClaimComparators.forMedical(),
                ClaimType.VEHICLE.name(), ClaimComparators.forVehicle(),
                ClaimType.PROPERTY.name(),ClaimComparators.forProperty());
    }

    public static List<UpdatablePredicate> getPredicateList(ResourcesService resourcesService){
        return List.of(new DailyComplexityPredicate(2, resourcesService),
                new DailyExpensesPredicate(BigDecimal.valueOf(15000l), resourcesService),
                new NoDuplicateIdPredicate(resourcesService));
    }







}
