package org.claims;

import org.events.ClaimEventsQueue;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;

import java.util.function.Predicate;

public class ClaimApprovalService {

    private final Predicate<Claim> approveCondition;
    private final ClaimEventsQueue eventsQueue;

    public ClaimApprovalService(Predicate<Claim> approveCondition, ClaimEventsQueue eventsQueue) {
        this.approveCondition = approveCondition;
        this.eventsQueue = eventsQueue;
    }

    public void consider(Claim claim) {

        boolean result = approveCondition.test(claim);

        if (result) eventsQueue.raiseEvent(new ClaimUpdatedEvent(claim, EventType.APPROVED));
        else eventsQueue.raiseEvent(new ClaimUpdatedEvent(claim, EventType.APPROVAL_POSTPONED));
    }
}
