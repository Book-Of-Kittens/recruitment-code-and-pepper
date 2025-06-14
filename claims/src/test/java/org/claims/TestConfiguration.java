package org.claims;

import org.engine.DailyProcessingService;
import org.resources.InMemoryIncomingClaimsService;
import org.resources.InMemoryResourcesService;
import org.resources.IncomingClaimsService;
import org.resources.ResourcesService;
import org.rules.BusinessRulesConfiguration;
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
        return new InMemoryIncomingClaimsService(); /* TODO: only in memory implementation */
    }

    public static DailyProcessingService DailyProcessingService(ResourcesService resourcesService, IncomingClaimsService incomingClaimsService) {
        ClaimApprovalService approval = ClaimConfig.getClaimApprovalService(resourcesService);
        List<WaitListService> waitLists = getWaitListServices(incomingClaimsService); /* TODO: use real config when possible */
        return new DailyProcessingService(approval, waitLists);
    }

    public static List<WaitListService> getWaitListServices(IncomingClaimsService incomingClaimsService) {

        return List.of(
                getWaitlistService(ClaimType.MEDICAL, incomingClaimsService),
                getWaitlistService(ClaimType.VEHICLE, incomingClaimsService),
                getWaitlistService(ClaimType.PROPERTY, incomingClaimsService)
        );
    }

    private static WaitListService getWaitlistService(ClaimType claimType,
                                                      IncomingClaimsService incomingClaimsService) {
        BusinessRulesConfiguration.comparatorsByType.get(claimType);
        BusinessRulesConfiguration.ofType(claimType);

        return new InMemoryWaitListService(
                BusinessRulesConfiguration.ofType(claimType),
                BusinessRulesConfiguration.comparatorsByType.get(claimType),
                incomingClaimsService);
    }

    private static Map<ClaimType, Comparator<Claim>> comparatorsByType() {
        return Map.of(ClaimType.MEDICAL, Comparator.comparing(Claim::id));
    }

    private static List<UpdatablePredicate> examplePredicate() {
        return List.of(new ExamplePredicate(), new NoDuplicateIdPredicate(new InMemoryResourcesService()));
    }

}
