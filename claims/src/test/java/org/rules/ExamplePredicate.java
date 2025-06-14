package org.rules;

import org.claims.Claim;

import java.util.function.Predicate;

public class ExamplePredicate implements UpdatablePredicate {
    int dailyClaims = 0;
    @Override
    public Predicate<Claim> getPredicate() {
        return claim -> dailyClaims<5;
    }

    @Override
    public void updateResources(Claim claim) {
        dailyClaims++;
    }

    @Override
    public void resetResource() {
        dailyClaims = 0;
    }
}
