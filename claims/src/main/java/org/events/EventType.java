package org.events;

import java.util.function.Predicate;

public enum EventType {
    APPROVED, APPROVAL_POSTPONED, NEW;

    public Predicate<ClaimUpdatedEvent> isOfType() {
        return event -> this == event.eventType();
    }
}
