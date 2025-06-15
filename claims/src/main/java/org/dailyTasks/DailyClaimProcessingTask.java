package org.dailyTasks;

import org.claims.ClaimProcessor;
import org.waitList.WaitListService;

import java.util.List;

public class DailyClaimProcessingTask implements DailyTask {

    private final List<WaitListService> waitListServices;
    private final ClaimProcessor processClaimService;

    public DailyClaimProcessingTask(List<WaitListService> waitListServices,
                                    ClaimProcessor processClaimService) {
        this.waitListServices = waitListServices;
        this.processClaimService = processClaimService;
    }

    @Override
    public void run() {

        while (hasClaimsToProcess()) {
            processClaimService.processClaim();
        }

    }

    private boolean hasClaimsToProcess() {
        return waitListServices.stream().anyMatch(WaitListService::hasClaimsToProcess);
    }
}
