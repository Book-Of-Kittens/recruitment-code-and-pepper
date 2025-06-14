package org.claims;

import org.engine.ClaimProcessingService;
import org.resources.InMemoryResourcesService;
import org.resources.ResourcesService;
import org.rules.ApprovalService;
import org.rules.UpdatablePredicate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TestConfiguration {

    public static ClaimProcessingService getEngine() {
        return new ClaimProcessingService(orderingService(), approvalService());
    }

    private static ClaimsOrderingService orderingService() {
        return new InMemoryClaimsOrderingService(comparatorsByType());
    }

    private static Map<ClaimType, Comparator<Claim>> comparatorsByType() {
        return Map.of(ClaimType.MEDICAL, Comparator.comparing(Claim::id));
    }

    private static List<UpdatablePredicate> updatablePredicates() {
        return List.of();
    }

    private static ResourcesService resourcesService() {
        return new InMemoryResourcesService(); /* TODO: only in memory implementation */
    }

    private static ApprovalService approvalService() {
        return new ApprovalService(updatablePredicates());
    }

}
