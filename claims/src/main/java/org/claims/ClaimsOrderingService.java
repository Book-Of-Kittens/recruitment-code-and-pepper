package org.claims;

import java.util.List;

public interface ClaimsOrderingService {

    List<Claim> getClaimsInOrder();

    void removeClaim(Claim claim);

    void addClaims(List<Claim> claims);

    boolean isEmpty();
}
