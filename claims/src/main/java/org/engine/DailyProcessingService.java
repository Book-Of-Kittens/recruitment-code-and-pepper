package org.engine;

import org.claims.Claim;
import org.claims.ClaimApprovalService;
import org.claims.WaitlistService;

import java.util.LinkedList;
import java.util.List;

public class DailyProcessingService {

    private final ClaimApprovalService approvalService;
    private final List<WaitlistService> waitLists;

    public DailyProcessingService(ClaimApprovalService approvalService,
                                  List<WaitlistService> waitLists) {
        this.approvalService = approvalService;
        this.waitLists = waitLists;
    }

    public List<Claim> processDay() {
        List<Claim> processedClaims = new LinkedList<>();
        // TODO :)
        return processedClaims;
    }

    public void processClaim(Claim claim) {
        System.out.println(claim.id() + " " + claim.amount() + " " + claim.complexity());
    }
}
