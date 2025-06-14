package org.engine;

import org.claims.Claim;
import org.claims.ClaimApprovalService;
import org.claims.WaitListService;

import java.util.LinkedList;
import java.util.List;

public class DailyProcessingService {

    private final ClaimApprovalService approvalService;
    private final List<WaitListService> waitLists;

    public DailyProcessingService(ClaimApprovalService approvalService,
                                  List<WaitListService> waitLists) {
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
