package org.rules;

import org.claims.Claim;
import org.resources.ResourcesService;

import java.util.function.Predicate;

public class DailyComplexityPredicate implements UpdatablePredicate {

    final private int limit;
    private final ResourcesService resourcesService;

    public DailyComplexityPredicate(int dailyComplexClaimLimit, ResourcesService resourcesService) {
        this.limit = dailyComplexClaimLimit;
        this.resourcesService = resourcesService;
    }
    @Override
    public void updateResources(Claim claim){
        if (isComplex(claim)){
            resourcesService.updateDailyComplexClaimsCounter();
        }
    }

    @Override
    public void resetResource() {
        resourcesService.resetComplexityCounter();
    }

    @Override
   public Predicate<Claim> getPredicate(){
       return claim -> !isComplex(claim) ||  resourcesService.getDailyComplexClaimsCounter()<limit;
   }

   private static boolean isComplex(Claim claim){
        return ComplexityLevel.HIGH.name().equals(claim.complexity());
   }

}
