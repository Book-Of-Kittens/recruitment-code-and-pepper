package org.dailyProcessing;

import org.resources.ResourcesService;
import org.waitList.WaitListService;

import java.util.List;

public class DailyProcessingService {

    private final List<WaitListService> waitLists;
    private final ProcessClaimService processClaimService;
    private final ResourcesService resourcesService;

    public DailyProcessingService(List<WaitListService> waitLists,
                                  ProcessClaimService processClaimService,
                                  ResourcesService resourcesService) {
        this.waitLists = waitLists;
        this.processClaimService = processClaimService;
        this.resourcesService = resourcesService;
    }

    public void processDay() {

        resourcesService.resetDailyLimits();

        while (hasClaimsToProcess()) {
            processClaimService.processClaim();
        }

        waitLists.forEach(WaitListService::placePostponedBackOnTheWaitList);
    }

    private boolean hasClaimsToProcess() {
        return waitLists.stream().anyMatch(WaitListService::hasClaimsToProcess);
    }
}
