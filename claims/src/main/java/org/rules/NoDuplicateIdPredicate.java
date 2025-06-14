package org.rules;

import org.claims.Claim;
import org.resources.ResourcesService;

import java.util.function.Predicate;

public class NoDuplicateIdPredicate implements UpdatablePredicate {

    private final ResourcesService resourcesService;

    public NoDuplicateIdPredicate(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    public Predicate<Claim> predicate() {
        return claim -> !resourcesService.get().processedIds().contains(claim.id());
    }
}
