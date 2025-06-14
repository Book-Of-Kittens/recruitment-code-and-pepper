package org.rules;

import org.claims.Claim;

import java.util.function.Predicate;

public class ExamplePredicate implements UpdatablePredicate {
    int dailyClaims = 0;

    @Override
    public Predicate<Claim> predicate() {
        return claim -> dailyClaims < 5;
    }

}
