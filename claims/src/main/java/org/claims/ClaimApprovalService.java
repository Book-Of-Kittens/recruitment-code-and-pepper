package org.claims;

import org.rules.UpdatablePredicate;

import java.util.List;

public class ClaimApprovalService {

    private final List<UpdatablePredicate> approveCondition;
    private final List<UpdatablePredicate> consumeCondition;

    public ClaimApprovalService(List<UpdatablePredicate> approvalPredicates, List<UpdatablePredicate> consumeCondition) {
        this.approveCondition = approvalPredicates;
        this.consumeCondition = consumeCondition;
    }
}
