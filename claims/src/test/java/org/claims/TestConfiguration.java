package org.claims;

import org.engine.DailyProcessingService;
import org.resources.InMemoryResourcesService;
import org.resources.IncomingClaimsService;
import org.resources.ResourcesService;
import org.rules.ExamplePredicate;
import org.rules.NoDuplicateIdPredicate;
import org.rules.UpdatablePredicate;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TestConfiguration {

    public static ResourcesService resourcesService() {
        return new InMemoryResourcesService();
    }

    public static IncomingClaimsService incomingClaimsService() {
        return new IncomingClaimsService(); /* TODO: only in memory implementation */
    }

    public static DailyProcessingService DailyProcessingService(ResourcesService resourcesService, IncomingClaimsService incomingClaimsService) {
        ClaimApprovalService approval = SplitResponsibilityConfig.getClaimApprovalService(resourcesService);
        List<WaitlistService> waitLists = SplitResponsibilityConfig.getWaitListServices(incomingClaimsService);
        return new DailyProcessingService(approval, waitLists); /* TODO: use real config when possible */
    }

    private static Map<ClaimType, Comparator<Claim>> comparatorsByType() {
        return Map.of(ClaimType.MEDICAL, Comparator.comparing(Claim::id));
    }

    private static List<UpdatablePredicate> examplePredicate() {
        return List.of(new ExamplePredicate(), new NoDuplicateIdPredicate(new InMemoryResourcesService()));
    }

}
