package org.dailyTasks;

import org.resources.ResourcesService;

public class DailyResourceRefreshTask implements DailyTask {
    private final ResourcesService resourcesService;

    public DailyResourceRefreshTask(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @Override
    public void run() {
        resourcesService.resetDailyLimits();
    }
}
