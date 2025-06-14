package org.resources;

import org.claims.Claim;
import org.claims.ComplexityLevel;
import org.events.ClaimEventsQueue;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class InMemoryResourcesService implements ResourcesService {
    /* TODO: add thread safety hurray */
    ResourcesState resourcesState = new ResourcesState(new HashSet<>(), BigDecimal.ZERO, 0);

    public InMemoryResourcesService(ClaimEventsQueue events) {

        events.subscribe(event -> {
            if (isClaimApprovedEvent(event)) updateForClaimApprovedEvent(event.claim());
        });

    }

    private boolean isClaimApprovedEvent(ClaimUpdatedEvent event) {
        return EventType.APPROVED == event.eventType();
    }

    private void updateForClaimApprovedEvent(Claim claim) {
        Set<String> processedIds = resourcesState.processedIds();
        processedIds.add(claim.id());
        BigDecimal commonBudget = resourcesState.budget().add(claim.amount());

        int commonHighComplexityCounter = resourcesState.highComplexityCounter();
        if (ComplexityLevel.HIGH == claim.complexity()) commonHighComplexityCounter++;

        resourcesState = new ResourcesState(processedIds, commonBudget, commonHighComplexityCounter);
    }

    @Override
    public ResourcesState get() {
        return resourcesState; /* TODO:a copy! */
    }
}
