package org.rules;

import org.claims.Claim;

import java.util.function.Predicate;

public record StatelessPredicate(Predicate<Claim> predicate) implements UpdatablePredicate {


}
