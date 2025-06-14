package org.dailyProcessing;

import org.approval.ClaimApprovalService;
import org.claims.Claim;
import org.waitList.WaitListService;

import java.util.Comparator;
import java.util.List;

public class DailyProcessingService {

    public static final Comparator<WaitListService> BY_PRIORITY = Comparator.comparing(WaitListService::getPriority).reversed();
    private final ClaimApprovalService approvalService;
    private final List<WaitListService> waitLists;

    public DailyProcessingService(ClaimApprovalService approvalService,
                                  List<WaitListService> waitLists) {
        this.approvalService = approvalService;
        this.waitLists = waitLists;
    }

    public void processDay() {
        Claim claim;
        do {
            claim = chooseClaim();
            if (null != claim) {
                approvalService.consider(claim);
            }
        } while (null != claim);

        waitLists.forEach(WaitListService::placePostponedBackOnTheWaitList);
    }

    private Claim chooseClaim() {
        return waitLists.stream()
                .filter(WaitListService::hasClaimsToProcess)
                .min(BY_PRIORITY)
                .map(WaitListService::getClaimForProcessing).orElse(null);
    }

}
