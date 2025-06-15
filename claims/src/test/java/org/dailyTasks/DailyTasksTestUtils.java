package org.dailyTasks;

import org.claims.Claim;
import org.claims.ClaimType;
import org.claims.ComplexityLevel;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.rules.ClaimComparators;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyTasksTestUtils {

    public static List<ClaimUpdatedEvent> runDay(DailyProcessTestContext context) {
        List<ClaimUpdatedEvent> updates = new ArrayList<>();
        context.events.subscribe(updates::add);
        context.dailyProcessingService.run();

        return updates.stream().toList(); // WARNING: otherwise, the list will still be modified, for example, by updates!
    }

    public static void standardDailyOutputAssertions(List<ClaimUpdatedEvent> allEvents) {
        List<ClaimUpdatedEvent> processingChoiceEvents = allEvents.stream().filter(EventType.NEW.isOfType().negate()).toList();

        assertEachClaimConsideredOnlyOnce(processingChoiceEvents);
        assertComplexityLimitNotExceeded(processingChoiceEvents);
        assertProcessingOrder(processingChoiceEvents);
    }

    private static void assertEachClaimConsideredOnlyOnce(List<ClaimUpdatedEvent> events) {
        assertThat(events).filteredOn(EventType.APPROVED.isOfType().or(EventType.POSTPONED.isOfType())).map(ClaimUpdatedEvent::claim).doesNotHaveDuplicates();
    }

    private static void assertProcessingOrder(List<ClaimUpdatedEvent> updates) {
        List<Claim> medicalClaims = updates.stream().filter(EventType.NEW.isOfType().negate()).map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.MEDICAL == c.type()).toList();
        assertThat(medicalClaims).isSortedAccordingTo(ClaimComparators.forMedical());

        List<Claim> vehicleClaims = updates.stream().filter(EventType.NEW.isOfType().negate()).map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.VEHICLE == c.type()).toList();
        assertThat(vehicleClaims).isSortedAccordingTo(ClaimComparators.forVehicle());

        List<Claim> propertyClaims = updates.stream().filter(EventType.NEW.isOfType().negate()).map(ClaimUpdatedEvent::claim).filter(c -> ClaimType.PROPERTY == c.type()).toList();
        assertThat(propertyClaims).isSortedAccordingTo(ClaimComparators.forProperty());
    }

    private static void assertComplexityLimitNotExceeded(List<ClaimUpdatedEvent> result) {
        List<ClaimUpdatedEvent> complexClaimsApprovals = result.stream().filter(EventType.APPROVED.isOfType()).filter(u -> u.claim().complexity() == ComplexityLevel.HIGH).toList();
        assertThat(complexClaimsApprovals).hasSizeLessThanOrEqualTo(2);
    }


    public static void prettyPrint(Integer day, List<ClaimUpdatedEvent> updates) {

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
