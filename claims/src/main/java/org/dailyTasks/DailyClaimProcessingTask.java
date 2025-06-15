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
        /*
        NOTE: the choice of the waitList and approval service pairing could be also separated into the exchangeable strategy.
        It might be also useful to know the top claims in waitlists before this choice is made. In such a case, either there
        would have to be a fallback in case a chosen, previously seen top claim gets removed by another process, (optimistic locking)
        or, we would need to be able to lock and then unlock all the waitLists during the time the choice is made.
        This is why I currently implemented only the easiest sensible looking algorithm.
        */
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
