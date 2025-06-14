package org.engine;

import org.claims.Claim;
import org.claims.ClaimApprovalService;
import org.claims.WaitListService;

import java.util.Comparator;
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
        waitLists.forEach(WaitListService::ingestIncoming);

        boolean finished = false;

        do {
            finished = matchClaimAndService();
        } while (!finished);

        // TODO :)


        waitLists.forEach(WaitListService::reingestPostponed);
        return processedClaims;
    }

    private boolean matchClaimAndService() {

        Claim claim = getNextClaim();
        if (null == claim) return true;

        ClaimApprovalService serviceForTheClaim = findApprovalService(claim);
        Boolean approved = serviceForTheClaim.approve(claim);

        if (approved) removeClaim(claim);
        else postponeClaim(claim);

        return false;
    }


    private void postponeClaim(Claim claim) {
        System.out.println("postponed: " + printClaim(claim));
        waitLists.stream().filter(list -> list.isClaimProcessed(claim))
                .forEach(list -> list.postponeClaim(claim));
    }

    private void removeClaim(Claim claim) {
        System.out.println("approved: " + printClaim(claim));
        waitLists.stream().filter(list -> list.isClaimProcessed(claim))
                .forEach(list -> list.removeClaim(claim));
    }

    private ClaimApprovalService findApprovalService(Claim claim) {
        assert (approvalService.tryConsume(claim));/* TODO: nice exception */
        return approvalService;
    }


    private Claim getNextClaim() {
        List<WaitListService> byPriority = waitLists.stream().sorted(Comparator.comparing(WaitListService::getPriority)).toList();

        Claim claim = null;

        for (WaitListService waitList : waitLists) {
            if (null == claim) claim = waitList.getClaimForProcessing();
        }
        return claim;
    }


    public String printClaim(Claim claim) {
        return claim.id() + " " + claim.amount() + " " + claim.complexity(); /* TOOD: remove */
    }
}
