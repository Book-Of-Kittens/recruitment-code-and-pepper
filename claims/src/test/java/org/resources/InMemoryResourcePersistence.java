package org.resources;

import java.math.BigDecimal;
import java.util.HashSet;

public class InMemoryResourcePersistence implements ResourcePersistence {

    ResourcesState resourcesState = new ResourcesState(new HashSet<>(), BigDecimal.ZERO, 0);

    public ResourcesState getResourcesState() {
        return resourcesState;
    }

    public void setResourcesState(ResourcesState resourcesState) {
        this.resourcesState = resourcesState;
    }
}
