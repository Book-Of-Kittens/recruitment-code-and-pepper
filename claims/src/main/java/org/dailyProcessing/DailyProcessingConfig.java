package org.dailyProcessing;

import org.approval.ClaimApprovalService;
import org.approval.ClaimConfig;
import org.events.ClaimEventsBus;
import org.resources.ResourcePersistence;
import org.resources.ResourcesService;
import org.waitList.WaitListConfig;
import org.waitList.WaitListService;

import java.util.List;

public class DailyProcessingConfig {

    public static DailyProcessingService getDailyProcessingService(ResourcePersistence resourcePersistence, ClaimEventsBus events) {

        ResourcesService resourcesService = new ResourcesService(events, resourcePersistence);
        ClaimApprovalService claimApprovalService = ClaimConfig.getClaimApprovalService(resourcesService, events);
        List<WaitListService> waitListServices = WaitListConfig.getWaitListServices(events);

        return new DailyProcessingService(claimApprovalService, waitListServices);
    }
}
