package org.dailyProcessing;

import org.claims.Claim;
import org.claims.TestConfiguration;
import org.events.ClaimEventsQueue;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.junit.Test;
import org.resources.ResourcesService;
import org.resources.SampleFromFile;

import java.util.List;

public class DailyProcessingServiceTest {

    @Test
    public void shouldProcessSomething() {

        ClaimEventsQueue queue = TestConfiguration.queue;
        printFrom(queue);
        ResourcesService resources = TestConfiguration.resourcesService;
        DailyProcessingService dailyProcessingService = TestConfiguration.DailyProcessingService(resources);


        // throw data onto waiting lists
        List<Claim> claims = SampleFromFile.withDefaultData();
        claims.stream().map(claim -> new ClaimUpdatedEvent(claim, EventType.NEW)).forEach(queue::raiseEvent);


        dailyProcessingService.processDay();


    }

    private void printFrom(ClaimEventsQueue queue) {
        queue.subscribe(event -> {
            System.out.println(event.eventType().name() + ": CLAIM " + event.claim().prettyFormat());
        });
    }


}