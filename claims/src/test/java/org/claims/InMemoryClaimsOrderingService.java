package org.claims;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryClaimsOrderingService implements ClaimsOrderingService {

  /* I know that I was expected to use queue, but it just doesn't seem efficient here.
    as far as I understand the requirements, every day the engine has to iterate through all unresolved claims anyway. */

    private final Map<String, Claim> claimsById = new HashMap<>();
    private final Map<String, Comparator<Claim>> comparators;

    public InMemoryClaimsOrderingService(Map<String, Comparator<Claim>> comparators) {
        this.comparators = comparators;
    }

    @Override
    public List<Claim> getClaimsInOrder() {
        Map<String, List<Claim>> claimsByType = claimsById.values().stream().collect(Collectors.groupingBy(Claim::type));
        claimsByType.forEach((type, list) -> list.sort(comparators.get(type)));

        /* I don't know how to compare claims from different types, so I am doing this the easiest way */
        /* I assume that in real life, there would be some business rule or balancing requirement */
        /* I might add the balancing option if I will have time */
        return claimsByType.values().stream().flatMap(Collection::stream).toList();
    }

    @Override
    public void removeClaim(Claim claim) {
        claimsById.remove(claim.id());
    }

    @Override
    public void addClaims(List<Claim> claims) {
        claims.forEach(claim -> claimsById.put(claim.id(), claim));
    }

    @Override
    public boolean isEmpty() {
        return claimsById.isEmpty();
    }


}
