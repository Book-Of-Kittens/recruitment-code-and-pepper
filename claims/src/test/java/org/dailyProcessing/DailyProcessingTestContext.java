package org.dailyProcessing;

import org.events.ClaimEventsBus;
import org.events.EventsConfig;
import org.resources.InMemoryResourcePersistence;
import org.resources.ResourcePersistence;

public class DailyProcessingTestContext {
    public final ResourcePersistence resourcePersistence = new InMemoryResourcePersistence();
    public final ClaimEventsBus events = EventsConfig.createEventBus();
    public final DailyProcessingService dailyProcessingService = DailyProcessingConfig.getDailyProcessingService(resourcePersistence, events);
}
