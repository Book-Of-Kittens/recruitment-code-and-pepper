package org.waitList;


import org.claims.Claim;

public interface WaitListService {

    Claim getClaimForProcessing();

    void placePostponedBackOnTheWaitList();

    int getPriority();

    boolean hasClaimsToProcess();
}
