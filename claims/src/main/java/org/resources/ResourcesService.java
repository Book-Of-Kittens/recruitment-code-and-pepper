package org.resources;

import org.claims.Claim;
import org.claims.ComplexityLevel;
import org.events.ClaimEventsBus;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;

import java.math.BigDecimal;
import java.util.Set;

public class ResourcesService {

    private final ResourcePersistence resourcePersistence;
    /* TODO: add thread safety hurray */

    public ResourcesService(ClaimEventsBus events, ResourcePersistence resourcePersistence) {
        this.resourcePersistence = resourcePersistence;

        events.subscribe(event -> {
            if (isClaimApprovedEvent(event)) updateForClaimApprovedEvent(event.claim());
        });
    }

    public ResourcesState get() {
        return resourcePersistence.getResourcesState(); /* TODO:a copy! */
    }

    public void resetDailyLimits() {
        ResourcesState oldState = resourcePersistence.getResourcesState();

        ResourcesState newState = new ResourcesState(
                oldState.processedIds(),
                BigDecimal.ZERO,
                0);
        resourcePersistence.setResourcesState(newState);
    }


    private boolean isClaimApprovedEvent(ClaimUpdatedEvent event) {
        return EventType.APPROVED == event.eventType();
    }

    private void updateForClaimApprovedEvent(Claim claim) {
        Set<String> processedIds = resourcePersistence.getResourcesState().processedIds();
        processedIds.add(claim.id());
        BigDecimal commonBudget = resourcePersistence.getResourcesState().budget().add(claim.amount());

        int commonHighComplexityCounter = resourcePersistence.getResourcesState().highComplexityCounter();
        if (ComplexityLevel.HIGH == claim.complexity()) commonHighComplexityCounter++;

        resourcePersistence.setResourcesState(new ResourcesState(processedIds, commonBudget, commonHighComplexityCounter));
    }

}
