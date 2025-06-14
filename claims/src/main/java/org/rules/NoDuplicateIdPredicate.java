package org.rules;

import org.claims.Claim;
import org.resources.ResourcesService;

import java.util.function.Predicate;

public class NoDuplicateIdPredicate implements UpdatablePredicate{

    private final ResourcesService resourcesService;

    public NoDuplicateIdPredicate(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    public void updateResources(Claim claim){
        resourcesService.addProcessedId(claim.id());
    }

    @Override
    public void resetResource() {
        /* we don't want to forget this list */
    }

    public Predicate<Claim> getPredicate(){
       return claim -> !resourcesService.isIdProcessed(claim.id());
   }
}
