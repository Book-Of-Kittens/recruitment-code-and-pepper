package org.dailyProcessing;

import org.events.ClaimEventsBus;
import org.resources.InMemoryResourcePersistence;
import org.resources.ResourcePersistence;

public class DailyProcessingTestContext {
    public final ResourcePersistence resourcePersistence = new InMemoryResourcePersistence();
    public final ClaimEventsBus events = new ClaimEventsBus();
    public final DailyProcessingService dailyProcessingService = DailyProcessingConfig.getDailyProcessingService(resourcePersistence, events);
}
