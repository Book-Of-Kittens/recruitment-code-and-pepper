package org.dailyTasks;

import org.claims.Claim;
import org.claims.ClaimType;
import org.claims.ComplexityLevel;
import org.claims.SampleFromFile;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.junit.Test;
import org.rules.ClaimComparators;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.events.EventTestUtils.putNewClaimsOnEventBus;

public class DailyProcessTest {

    private static final int BREAK_LOOP_LIMIT = 50;

    @Test
    public void testSingleDayOutput() {
        // GIVEN
        DailyProcessTestContext context = new DailyProcessTestContext();
        List<Claim> inputClaims = SampleFromFile.with(SampleFromFile.LONG_LIST);
        putNewClaimsOnEventBus(context.events, inputClaims);

        // WHEN
        List<ClaimUpdatedEvent> updates = runDay(context);

        // THEN
        standardDailyOutputAssertions(updates);
        // assert that all claims had been considered for processing in a day
        assertThat(updates).filteredOn(EventType.APPROVED.isOfType().or(EventType.POSTPONED.isOfType())).map(ClaimUpdatedEvent::claim).hasSameElementsAs(inputClaims);

    }

    @Test
    public void testMultipleDaysOutput() {
        // GIVEN
        DailyProcessTestContext context = new DailyProcessTestContext();

        List<Claim> input = SampleFromFile.with(SampleFromFile.SHORT_LIST);
        putNewClaimsOnEventBus(context.events, input);

        // WHEN
        Map<Integer, List<ClaimUpdatedEvent>> updatesByDay = runDailyUpdatesInALoop(context);

        // THEN
        for (Map.Entry<Integer, List<ClaimUpdatedEvent>> daySet : updatesByDay.entrySet()) {
            prettyPrint(daySet.getKey(), daySet.getValue());
            standardDailyOutputAssertions(daySet.getValue());
        }

        // assert that all claims have been approved for processing after enough days
        List<ClaimUpdatedEvent> allEvents = updatesByDay.values().stream().flatMap(Collection::stream).toList();
        assertThat(allEvents).filteredOn(EventType.APPROVED.isOfType()).map(ClaimUpdatedEvent::claim).hasSameElementsAs(input);
    }

    private void standardDailyOutputAssertions(List<ClaimUpdatedEvent> allEvents) {
        List<ClaimUpdatedEvent> processingChoiceEvents = allEvents.stream().filter(EventType.NEW.isOfType().negate()).toList();

        assertEachClaimConsideredOnlyOnce(processingChoiceEvents);
        assertComplexityLimitNotExceeded(processingChoiceEvents);
        assertProcessingOrder(processingChoiceEvents);
    }

    private void assertEachClaimConsideredOnlyOnce(List<ClaimUpdatedEvent> events) {
        assertThat(events).filteredOn(EventType.APPROVED.isOfType().or(EventType.POSTPONED.isOfType())).map(ClaimUpdatedEvent::claim).doesNotHaveDuplicates();
    }

    private void assertProcessingOrder(List<ClaimUpdatedEvent> updates) {
        List<Claim> medicalClaims = updates.stream().filter(EventType.NEW.isOfType().negate()).map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.MEDICAL == c.type()).toList();
        assertThat(medicalClaims).isSortedAccordingTo(ClaimComparators.forMedical());

        List<Claim> vehicleClaims = updates.stream().filter(EventType.NEW.isOfType().negate()).map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.VEHICLE == c.type()).toList();
        assertThat(vehicleClaims).isSortedAccordingTo(ClaimComparators.forVehicle());

        List<Claim> propertyClaims = updates.stream().filter(EventType.NEW.isOfType().negate()).map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.PROPERTY == c.type()).toList();
        assertThat(propertyClaims).isSortedAccordingTo(ClaimComparators.forProperty());
    }

    private void assertComplexityLimitNotExceeded(List<ClaimUpdatedEvent> result) {
        List<ClaimUpdatedEvent> complexClaimsApprovals = result.stream().filter(EventType.APPROVED.isOfType()).filter(u -> u.claim().complexity() == ComplexityLevel.HIGH).toList();
        assertThat(complexClaimsApprovals).hasSizeLessThanOrEqualTo(2);
    }

    private Map<Integer, List<ClaimUpdatedEvent>> runDailyUpdatesInALoop(DailyProcessTestContext context) {
        Map<Integer, List<ClaimUpdatedEvent>> updatesByDay = new HashMap<>();
        int day = 0;
        boolean finished = false;
        while (!finished) {

            List<ClaimUpdatedEvent> dayEvents = runDay(context);

            if (dayEvents.isEmpty()) finished = true;
            updatesByDay.put(day, dayEvents);
            day++;
            if (day > BREAK_LOOP_LIMIT) finished = true;
        }
        return updatesByDay;
    }

    private List<ClaimUpdatedEvent> runDay(DailyProcessTestContext context) {
        List<ClaimUpdatedEvent> updates = new ArrayList<>();
        context.events.subscribe(updates::add);
        context.dailyProcessingService.run();

        return updates.stream().toList(); // WARNING: otherwise, the list will still be modified, for example, by updates!
    }

    private void prettyPrint(Integer day, List<ClaimUpdatedEvent> updates) {

        System.out.println("---- " + day + " -----");
        List<ClaimUpdatedEvent> newClaims = updates.stream().filter(u -> EventType.NEW == u.eventType()).toList();
        List<ClaimUpdatedEvent> approved = updates.stream().filter(u -> EventType.APPROVED == u.eventType()).toList();
        List<ClaimUpdatedEvent> postponed = updates.stream().filter(u -> EventType.POSTPONED == u.eventType()).toList();

        System.out.println("considered: " + updates.size() + " NEW: " + newClaims.size() + " APPROVED: " + approved.size() + " POSTPONED: " + postponed.size());

        List<ClaimUpdatedEvent> medical = approved.stream().filter(u -> ClaimType.MEDICAL == u.claim().type()).toList();
        List<ClaimUpdatedEvent> vehicle = approved.stream().filter(u -> ClaimType.VEHICLE == u.claim().type()).toList();
        List<ClaimUpdatedEvent> property = approved.stream().filter(u -> ClaimType.PROPERTY == u.claim().type()).toList();

        System.out.println("approved: MEDICAL: " + medical.size() + " VEHICLE: " + vehicle.size() + " PROPERTY: " + property.size());

    }

}