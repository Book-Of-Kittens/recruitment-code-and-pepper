package org.rules;

import org.claims.Claim;

import java.util.List;
import java.util.Optional;

public class ApprovalService {

    private final List<UpdatablePredicate> predicates;

    public ApprovalService(List<UpdatablePredicate> predicates) {
        this.predicates = predicates;
    }

    public boolean shouldProcess(Claim claim) {
        Optional<UpdatablePredicate> failingPredicate = predicates.stream()
                .filter(predicate -> !predicate.getPredicate().test(claim)).findAny();
        return failingPredicate.isEmpty();
    }

    public void updatePredicates(Claim claim) {
        predicates.forEach(predicate -> predicate.updateResources(claim));
    }

    public void resetPredicates() {
        predicates.forEach(UpdatablePredicate::resetResource);
    }

}
