package org.dailyProcessing;

import org.claims.Claim;
import org.claims.ComplexityLevel;
import org.events.ClaimEventsBus;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.junit.Test;
import org.resources.SampleFromFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyProcessingServiceTest {

    @Test
    public void testDailyProcessOutput() {
        // GIVEN
        DailyProcessingTestContext context = new DailyProcessingTestContext();

        List<ClaimUpdatedEvent> updates = new ArrayList<>();
        context.events.subscribe(updates::add);

        populateWithExampleData(context.events);

        // WHEN
        context.dailyProcessingService.processDay();

        // THEN
        assertComplexityLimitNotExceeded(updates);
        assertAllNewGotProcessed(updates);
    }

    private void assertAllNewGotProcessed(List<ClaimUpdatedEvent> updates) {
        List<String> inputIds = updates.stream()
                .filter(EventType.NEW.isOfType())
                .map(ClaimUpdatedEvent::claim).map(Claim::id).toList();

        List<String> processedIds = updates.stream()
                .filter(EventType.NEW.isOfType().negate())
                .map(ClaimUpdatedEvent::claim).map(Claim::id).toList();
        assertThat(processedIds).hasSameElementsAs(inputIds);
    }

    private void assertComplexityLimitNotExceeded(List<ClaimUpdatedEvent> result) {
        List<ClaimUpdatedEvent> complexClaimsApprovals = result.stream()
                .filter(EventType.APPROVED.isOfType())
                .filter(u -> u.claim().complexity() == ComplexityLevel.HIGH).toList();
        assertThat(complexClaimsApprovals).hasSizeLessThanOrEqualTo(2);
    }

    private void populateWithExampleData(ClaimEventsBus events) {
        List<Claim> claims = SampleFromFile.withDefaultData();
        claims.stream().map(claim -> new ClaimUpdatedEvent(claim, EventType.NEW)).forEach(events::raiseEvent);
    }
}