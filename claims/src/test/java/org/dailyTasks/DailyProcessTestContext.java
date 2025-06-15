package org.dailyTasks;

import org.events.ClaimEventsBus;
import org.events.EventsConfig;
import org.resources.InMemoryResourcePersistence;
import org.resources.ResourcePersistence;

public class DailyProcessTestContext {
    public final ResourcePersistence resourcePersistence = new InMemoryResourcePersistence();
    public final ClaimEventsBus events = EventsConfig.createEventBus();
    public final DailyTaskProcessor dailyProcessingService = DailyProcessingConfig.getDailyTaskProcessor(resourcePersistence, events);
}
