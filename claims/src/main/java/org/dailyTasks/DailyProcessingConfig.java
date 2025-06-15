package org.dailyTasks;

import org.approval.ClaimApprovalConfig;
import org.approval.ClaimApprovalService;
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
        ResourcesService resourcesService = new ResourcesService(events, resourcePersistence);
        ClaimApprovalService claimApprovalService = ClaimApprovalConfig.getClaimApprovalService(resourcesService, events);
        List<WaitListService> waitListServices = WaitListConfig.getWaitListServices(events);

        DailyTask resourceRefresh = new DailyResourceRefreshTask(resourcesService);
        DailyTask claimProcessing = new DailyClaimProcessingTask(waitListServices, claimApprovalService);
        DailyTask waitListMaintenance = new DailyWaitListMaintenanceTask(waitListServices);
        return List.of(resourceRefresh, claimProcessing, waitListMaintenance);
    }

}
