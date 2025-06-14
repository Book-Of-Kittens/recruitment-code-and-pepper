package org.dailyProcessing;

import org.claims.*;
import org.events.ClaimEventsQueue;
import org.resources.InMemoryResourcesService;
import org.resources.ResourcesService;
import org.rules.BusinessRulesConfiguration;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class DailyProcessingTestContext {
    public final ClaimEventsQueue queue = new ClaimEventsQueue();

    public ResourcesService resourcesService = new InMemoryResourcesService(queue);

    public ClaimApprovalService claimApprovalService = ClaimConfig.getClaimApprovalService(resourcesService, queue);

    public List<WaitListService> waitListServices = getWaitListServices(queue);
    public DailyProcessingService dailyProcessingService = new DailyProcessingService(claimApprovalService, waitListServices);

    private static List<WaitListService> getWaitListServices(ClaimEventsQueue queue) {

        return List.of(
                getWaitlistService(queue,
                        BusinessRulesConfiguration.ofType(ClaimType.MEDICAL),
                        BusinessRulesConfiguration.comparatorsByType.get(ClaimType.MEDICAL)),
                getWaitlistService(queue,
                        BusinessRulesConfiguration.ofType(ClaimType.VEHICLE),
                        BusinessRulesConfiguration.comparatorsByType.get(ClaimType.VEHICLE)),
                getWaitlistService(queue,
                        BusinessRulesConfiguration.ofType(ClaimType.PROPERTY),
                        BusinessRulesConfiguration.comparatorsByType.get(ClaimType.PROPERTY))
        );
    }

    private static WaitListService getWaitlistService(ClaimEventsQueue queue, Predicate<Claim> consumePredicate, Comparator<Claim> orderSource) {

        return new InMemoryWaitListService(
                queue,
                consumePredicate,
                orderSource);
    }
}
