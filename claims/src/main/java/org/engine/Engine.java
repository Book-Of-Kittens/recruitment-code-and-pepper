package org.engine;

import org.claims.Claim;
import org.claims.ClaimsOrderingService;
import org.resources.ResourcesService;
import org.rules.ApprovalService;

import java.util.*;

public class Engine {

    private final ClaimsOrderingService claimsOrderingService;
    private final ApprovalService approvalService;

    public Engine(ClaimsOrderingService claimsOrderingService,
                  ApprovalService approvalService) {
        this.claimsOrderingService = claimsOrderingService;
        this.approvalService = approvalService;
    }

    public void process(){

        while (!claimsOrderingService.isEmpty()) {
            processDay();
        }
    }

    public List<Claim> processDay(){

        System.out.println("--");
        approvalService.resetPredicates(); /* TODO */

        List<Claim> processedClaims = new LinkedList<>();
        List<Claim> claimsInOrder = claimsOrderingService.getClaimsInOrder();
        for (Claim claim : claimsInOrder) {
            if (approvalService.shouldProcess(claim)){

                approvalService.updatePredicates(claim);
                processedClaims.add(claim);
                claimsOrderingService.removeClaim(claim);

                processClaim(claim);
            }
        }
        return processedClaims;
    }


    public void processClaim(Claim claim){
        System.out.println(claim.id() +" "+claim.amount()+" "+claim.complexity());
    }
}
