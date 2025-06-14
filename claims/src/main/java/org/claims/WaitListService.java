package org.claims;


public interface WaitListService {

    Claim getClaimForProcessing();

    void reingestPostponed();

    int getPriority();

    boolean hasClaimsToProcess();
}
