package org.claims;

import org.approval.ClaimApprovalService;
import org.waitList.WaitListService;

import java.util.Comparator;
import java.util.List;

public class ClaimProcessor {

    public static final Comparator<WaitListService> BY_PRIORITY = Comparator.comparing(WaitListService::getPriority).reversed();
    private final ClaimApprovalService approvalService;
    private final List<WaitListService> waitLists;

    public ClaimProcessor(ClaimApprovalService approvalService,
                          List<WaitListService> waitLists) {
        this.approvalService = approvalService;
        this.waitLists = waitLists;
    }

    public void processClaim() {
        Claim claim = chooseClaim();
        approvalService.consider(claim);
    }

    private Claim chooseClaim() {
        return waitLists.stream()
                .filter(WaitListService::hasClaimsToProcess)
                .min(BY_PRIORITY)
                .map(WaitListService::getClaimForProcessing).orElse(null);
    }
}
