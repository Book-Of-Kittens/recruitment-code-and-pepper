package org.rules;

import org.claims.Claim;

import java.util.function.Predicate;

public class AlwaysTruePredicate implements UpdatablePredicate {

    @Override
    public Predicate<Claim> predicate() {
        return _ -> true;
    }

    @Override
    public void updateResources(Claim claim) {

    }

    @Override
    public void resetResource() {

    }
}
