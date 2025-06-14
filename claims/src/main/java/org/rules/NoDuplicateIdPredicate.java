package org.rules;

import org.claims.Claim;
import org.resources.ResourcesService;

import java.util.function.Predicate;

public class NoDuplicateIdPredicate implements Predicate<Claim> {

    private final ResourcesService resourcesService;

    public NoDuplicateIdPredicate(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    @Override
    public boolean test(Claim claim) {
        return !resourcesService.get().processedIds().contains(claim.id());
    }
}
