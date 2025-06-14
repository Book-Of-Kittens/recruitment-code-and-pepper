package org.rules;

import org.claims.Claim;

import java.util.function.Predicate;

public interface UpdatablePredicate {

    Predicate<Claim> getPredicate();
    void updateResources(Claim claim);
    void resetResource();

}
