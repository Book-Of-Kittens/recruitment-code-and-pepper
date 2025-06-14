package org.dailyProcessing;

import org.claims.Claim;
import org.claims.ClaimApprovalService;
import org.claims.WaitListService;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class DailyProcessingService {

    public static final Comparator<WaitListService> WAIT_LIST_SERVICE_COMPARATOR = Comparator.comparing(WaitListService::getPriority).reversed();
    private final ClaimApprovalService approvalService;
    private final List<WaitListService> waitLists;

    public DailyProcessingService(ClaimApprovalService approvalService,
                                  List<WaitListService> waitLists) {
        this.approvalService = approvalService;
        this.waitLists = waitLists;
    }

    public List<Claim> processDay() {

        List<Claim> processedClaims = new LinkedList<>();

        Claim claim;
        do {
            claim = chooseClaim();
            if (null != claim) {
                approvalService.consider(claim);
            }
        } while (null != claim);

        waitLists.forEach(WaitListService::reingestPostponed);
        return processedClaims;
    }

    private Claim chooseClaim() {
        return waitLists.stream().filter(WaitListService::hasClaimsToProcess)
                .min(WAIT_LIST_SERVICE_COMPARATOR)
                .map(WaitListService::getClaimForProcessing).orElse(null);
    }

}
