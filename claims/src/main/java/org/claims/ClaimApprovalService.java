package org.claims;

import org.events.ClaimEventsQueue;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.rules.UpdatablePredicate;

import java.util.List;

public class ClaimApprovalService {

    private final List<UpdatablePredicate> approveCondition;
    private final ClaimEventsQueue eventsQueue;
    // note: conditions if a service is able to process a claim might also be added in a similar way.

    public ClaimApprovalService(List<UpdatablePredicate> approvalPredicates, ClaimEventsQueue eventsQueue) {
        this.approveCondition = approvalPredicates;
        this.eventsQueue = eventsQueue;
    }

    public void consider(Claim claim) {
        List<UpdatablePredicate> failedPredicates = approveCondition.stream()
                .filter(p -> !p.predicate().test(claim))
                .toList();

        boolean result = failedPredicates.isEmpty();

        if (result) eventsQueue.raiseEvent(new ClaimUpdatedEvent(claim, EventType.APPROVED));
        else eventsQueue.raiseEvent(new ClaimUpdatedEvent(claim, EventType.APPROVAL_POSTPONED));
    }
}
