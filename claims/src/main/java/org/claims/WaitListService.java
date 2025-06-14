package org.claims;


public interface WaitListService {

    Claim getClaimForProcessing();

    void postponeClaim(Claim claim);

    void removeClaim(Claim claim);

    void ingestIncoming();

    boolean tryConsume(Claim claim);

    void reingestPostponed();

    int getPriority();

    boolean isClaimProcessed(Claim claim);
}
