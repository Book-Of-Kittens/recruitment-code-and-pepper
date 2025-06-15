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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyProcessTest {

    private static final int BREAK_LOOP_LIMIT = 50;

    private static List<ClaimUpdatedEvent> runDay(DailyProcessTestContext context) {
        List<ClaimUpdatedEvent> updates = new ArrayList<>();
        context.events.subscribe(updates::add);
        context.dailyProcessingService.run();

        return updates.stream().toList(); // WARNING: otherwise, the list will still be modified, for example, by updates!
    }

    @Test
    public void testSingleDayOutput() {
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

    @Test
    public void testMultipleDaysOutput() {
        // GIVEN
        DailyProcessTestContext context = new DailyProcessTestContext();
        Map<Integer, List<ClaimUpdatedEvent>> updatesByDay = new HashMap<>();
        populateWithExampleData(context.events);

        int day = 0;
        boolean finished = false;
        while (!finished) {
            List<ClaimUpdatedEvent> dayEvents = runDay(context);
            if (dayEvents.isEmpty()) finished = true;
            updatesByDay.put(day, dayEvents);
            day++;
            if (day > BREAK_LOOP_LIMIT) finished = true;
        }
        // THEN

        for (Map.Entry<Integer, List<ClaimUpdatedEvent>> daySet : updatesByDay.entrySet()) {
            prettyPrint(daySet.getKey(), daySet.getValue());

            assertComplexityLimitNotExceeded(daySet.getValue());
            assertProcessingOrder(daySet.getValue());
        }

    }

    private void prettyPrint(Integer day, List<ClaimUpdatedEvent> updates) {

        System.out.println("---- " + day + " -----");
        List<ClaimUpdatedEvent> newClaims = updates.stream().filter(u -> EventType.NEW == u.eventType()).toList();
        List<ClaimUpdatedEvent> approved = updates.stream().filter(u -> EventType.APPROVED == u.eventType()).toList();
        List<ClaimUpdatedEvent> postponed = updates.stream().filter(u -> EventType.POSTPONED == u.eventType()).toList();

        System.out.println("considered: " + updates.size() + " NEW: " + newClaims.size() + " APPROVED: " + approved.size() + " POSTPONED: " + postponed.size());

        List<ClaimUpdatedEvent> medical = updates.stream().filter(u -> ClaimType.MEDICAL == u.claim().type()).toList();
        List<ClaimUpdatedEvent> vehicle = updates.stream().filter(u -> ClaimType.VEHICLE == u.claim().type()).toList();
        List<ClaimUpdatedEvent> property = updates.stream().filter(u -> ClaimType.PROPERTY == u.claim().type()).toList();

        System.out.println("considered: MEDICAL: " + medical.size() + " VEHICLE: " + vehicle.size() + " PROPERTY: " + property.size());

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