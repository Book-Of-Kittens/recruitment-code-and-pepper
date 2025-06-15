package org.dailyTasks;

import org.claims.Claim;
import org.claims.ClaimType;
import org.claims.ComplexityLevel;
import org.claims.SampleFromFile;
import org.events.ClaimEventsBus;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.junit.Test;
import org.rules.ClaimComparators;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyProcessTest {

    /* TODO:test day by day */

    @Test
    public void testDailyProcessOutput() {
        // GIVEN
        DailyProcessTestContext context = new DailyProcessTestContext();

        List<ClaimUpdatedEvent> updates = new ArrayList<>();
        context.events.subscribe(updates::add);

        populateWithExampleData(context.events);

        // WHEN
        context.dailyProcessingService.run();

        // THEN
        assertComplexityLimitNotExceeded(updates);
        assertAllNewGotProcessed(updates);
        assertProcessingOrder(updates);
    }

    private void assertProcessingOrder(List<ClaimUpdatedEvent> updates) {
        List<Claim> medicalClaims = updates.stream()
                .filter(EventType.NEW.isOfType().negate())
                .map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.MEDICAL == c.type()).toList();
        assertThat(medicalClaims).isSortedAccordingTo(ClaimComparators.forMedical());

        List<Claim> vehicleClaims = updates.stream()
                .filter(EventType.NEW.isOfType().negate())
                .map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.VEHICLE == c.type()).toList();
        assertThat(vehicleClaims).isSortedAccordingTo(ClaimComparators.forVehicle());

        List<Claim> propertyClaims = updates.stream()
                .filter(EventType.NEW.isOfType().negate())
                .map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.PROPERTY == c.type()).toList();
        assertThat(propertyClaims).isSortedAccordingTo(ClaimComparators.forProperty());
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