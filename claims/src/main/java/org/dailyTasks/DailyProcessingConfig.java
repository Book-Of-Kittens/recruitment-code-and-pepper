package org.dailyTasks;

import org.approval.ClaimApprovalService;
import org.approval.ClaimConfig;
import org.claims.ClaimProcessor;
import org.events.ClaimEventsBus;
import org.resources.ResourcePersistence;
import org.resources.ResourcesService;
import org.waitList.WaitListConfig;
import org.waitList.WaitListService;

import java.util.List;

public class DailyProcessingConfig {

    public static DailyTaskProcessor getDailyTaskProcessor(ResourcePersistence resourcePersistence, ClaimEventsBus events) {

        return new DailyTaskProcessor(getDailyTasks(resourcePersistence, events));
    }

    public static List<DailyTask> getDailyTasks(ResourcePersistence resourcePersistence, ClaimEventsBus events) {
        ResourcesService resourcesService = new ResourcesService(events, resourcePersistence); /* doto: move */
        ClaimApprovalService claimApprovalService = ClaimConfig.getClaimApprovalService(resourcesService, events);
        List<WaitListService> waitListServices = WaitListConfig.getWaitListServices(events);
        ClaimProcessor processClaimService = new ClaimProcessor(claimApprovalService, waitListServices); /* TODO: simplify this service?*/


        DailyTask resourceRefresh = getDailyResourceRefreshTask(resourcesService);
        DailyTask claimProcessing = new DailyClaimProcessingTask(waitListServices, processClaimService);
        DailyTask waitListMaintenance = getDailyWaitListMaintenanceTask(waitListServices);
        return List.of(resourceRefresh, claimProcessing, waitListMaintenance);
    }


    public static DailyResourceRefreshTask getDailyResourceRefreshTask(ResourcesService resourcesService) {

        return new DailyResourceRefreshTask(resourcesService);
    }

    public static DailyWaitListMaintenanceTask getDailyWaitListMaintenanceTask(List<WaitListService> waitListServices) {

        return new DailyWaitListMaintenanceTask(waitListServices);
    }
}
