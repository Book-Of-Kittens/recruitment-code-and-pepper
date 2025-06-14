package org.rules;

import org.claims.Claim;

import java.util.function.Predicate;

public record StatelessPredicate(Predicate<Claim> predicate) implements UpdatablePredicate {

    @Override
    public void updateResources(Claim claim) {
        // stateless, so no resources to update
    }

    @Override
    public void resetResource() {
        // stateless, so no resources to update
    }
}
