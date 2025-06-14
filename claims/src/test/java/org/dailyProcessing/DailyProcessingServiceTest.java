package org.dailyProcessing;

import org.claims.Claim;
import org.events.ClaimEventsQueue;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.junit.Test;
import org.resources.SampleFromFile;

import java.util.List;

public class DailyProcessingServiceTest {

    @Test
    public void shouldProcessSomething() {

        DailyProcessingTestContext context = new DailyProcessingTestContext();


        ClaimEventsQueue queue = context.queue;
        printFrom(queue);
        DailyProcessingService dailyProcessingService = context.dailyProcessingService;


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