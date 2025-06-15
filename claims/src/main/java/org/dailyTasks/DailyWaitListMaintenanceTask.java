package org.dailyTasks;

import org.waitList.WaitListService;

import java.util.List;

public class DailyWaitListMaintenanceTask implements DailyTask {
    private final List<WaitListService> waitListServices;

    public DailyWaitListMaintenanceTask(List<WaitListService> waitListServices) {
        this.waitListServices = waitListServices;
    }

    @Override
    public void run() {
        waitListServices.forEach(WaitListService::placePostponedBackOnTheWaitList);
    }
}
