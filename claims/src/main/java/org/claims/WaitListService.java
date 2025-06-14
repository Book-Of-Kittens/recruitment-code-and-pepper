package org.claims;


public interface WaitListService {

    Claim getClaim();

    void postponeClaim(Claim claim);

    void removeClaim(Claim claim);
}
