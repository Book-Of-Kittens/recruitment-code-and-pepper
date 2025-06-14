package org.waitList;

import org.claims.Claim;
import org.claims.ClaimType;
import org.events.ClaimEventsBus;
import org.rules.BusinessRulesConfiguration;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class WaitListConfig {

    public static List<WaitListService> getWaitListServices(ClaimEventsBus eventBus) {

        return List.of(
                getWaitlistService(eventBus,
                        BusinessRulesConfiguration.ofType(ClaimType.MEDICAL),
                        BusinessRulesConfiguration.comparatorsByType.get(ClaimType.MEDICAL)),
                getWaitlistService(eventBus,
                        BusinessRulesConfiguration.ofType(ClaimType.VEHICLE),
                        BusinessRulesConfiguration.comparatorsByType.get(ClaimType.VEHICLE)),
                getWaitlistService(eventBus,
                        BusinessRulesConfiguration.ofType(ClaimType.PROPERTY),
                        BusinessRulesConfiguration.comparatorsByType.get(ClaimType.PROPERTY))
        );
    }

    private static WaitListService getWaitlistService(ClaimEventsBus eventBus, Predicate<Claim> consumePredicate, Comparator<Claim> orderSource) {

        return new InMemoryWaitListService(
                eventBus,
                consumePredicate,
                orderSource);
    }
}
