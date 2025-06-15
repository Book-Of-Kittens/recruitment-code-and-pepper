package org.approval;

import org.claims.Claim;
import org.events.ClaimEventsBus;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;

import java.util.function.Predicate;

public class ClaimApprovalService {

    private final Predicate<Claim> approveCondition;
    private final ClaimEventsBus events;

    public ClaimApprovalService(Predicate<Claim> approveCondition, ClaimEventsBus events) {
        this.approveCondition = approveCondition;
        this.events = events;
    }

    public void consider(Claim claim) {
        boolean result = approveCondition.test(claim);

        if (result)
            events.raiseEvent(new ClaimUpdatedEvent(claim, EventType.APPROVED));
        else
            events.raiseEvent(new ClaimUpdatedEvent(claim, EventType.POSTPONED));
    }
}
