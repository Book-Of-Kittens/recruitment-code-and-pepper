package org.claims;

import java.util.List;

public interface ClaimsRepository {
    List<Claim> getAllUnprocessedClaims();
    void setClaimAsProcessed(String claimId);
}
