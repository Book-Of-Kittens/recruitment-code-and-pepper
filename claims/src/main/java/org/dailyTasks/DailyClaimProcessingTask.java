package org.dailyTasks;

import org.approval.ClaimApprovalService;
import org.claims.Claim;
import org.waitList.WaitListService;

import java.util.Comparator;
import java.util.List;

public class DailyClaimProcessingTask implements DailyTask {

    public static final Comparator<WaitListService> BY_PRIORITY = Comparator.comparing(WaitListService::getPriority).reversed();
    private final List<WaitListService> waitListServices;
    private final ClaimApprovalService approvalService;


    public DailyClaimProcessingTask(List<WaitListService> waitListServices,
                                    ClaimApprovalService approvalService) {
        this.waitListServices = waitListServices;
        this.approvalService = approvalService;
    }

    @Override
    public void run() {

        while (hasClaimsToProcess()) {
            processClaim();
        }

    }

    public void processClaim() {
        Claim claim = chooseClaim();
        approvalService.consider(claim);
    }

    private Claim chooseClaim() {
        return waitListServices.stream()
                .filter(WaitListService::hasClaimsToProcess)
                .min(BY_PRIORITY)
                .map(WaitListService::getClaimForProcessing).orElse(null);
    }

    private boolean hasClaimsToProcess() {
        return waitListServices.stream().anyMatch(WaitListService::hasClaimsToProcess);
    }
}
