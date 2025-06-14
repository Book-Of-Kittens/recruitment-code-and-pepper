package org.events;

import org.claims.Claim;

public record ClaimUpdatedEvent(Claim claim, EventType eventType) {
}
