package org.claims;

import org.dailyProcessing.DailyProcessingService;
import org.events.ClaimEventsQueue;
import org.resources.InMemoryResourcesService;
import org.resources.ResourcesService;
import org.rules.BusinessRulesConfiguration;

import java.util.List;

public class TestConfiguration {

    public static final ClaimEventsQueue queue = new ClaimEventsQueue();
    public static ResourcesService resourcesService = new InMemoryResourcesService(queue);

    public static DailyProcessingService DailyProcessingService(ResourcesService resourcesService) {
        ClaimApprovalService approval = ClaimConfig.getClaimApprovalService(resourcesService, queue);
        List<WaitListService> waitLists = getWaitListServices(); /* TODO: use real config when possible */
        return new DailyProcessingService(approval, waitLists);
    }

    public static List<WaitListService> getWaitListServices() {

        return List.of(
                getWaitlistService(ClaimType.MEDICAL),
                getWaitlistService(ClaimType.VEHICLE),
                getWaitlistService(ClaimType.PROPERTY)
        );
    }

    private static WaitListService getWaitlistService(ClaimType claimType) {
        BusinessRulesConfiguration.comparatorsByType.get(claimType);
        BusinessRulesConfiguration.ofType(claimType);

        return new InMemoryWaitListService(
                queue,
                BusinessRulesConfiguration.ofType(claimType),
                BusinessRulesConfiguration.comparatorsByType.get(claimType));
    }

}
