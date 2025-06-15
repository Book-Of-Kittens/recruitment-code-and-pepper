package org.dailyTasks;

import org.claims.Claim;
import org.claims.ClaimType;
import org.claims.ComplexityLevel;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.rules.ClaimComparators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyTasksTestUtils {

    private static final int BREAK_LOOP_LIMIT = 50;

    public static Map<Integer, List<ClaimUpdatedEvent>> runDailyProcessInALoop(DailyProcessTestContext context) {
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

    public static List<ClaimUpdatedEvent> runDay(DailyProcessTestContext context) {
        List<ClaimUpdatedEvent> updates = new ArrayList<>();
        context.events.subscribe(updates::add);
        context.dailyProcessingService.run();

        return updates.stream().toList(); // WARNING: otherwise, the list will still be modified, for example, by updates!
    }

    public static void standardDailyOutputAssertions(List<ClaimUpdatedEvent> allEvents) {
        List<ClaimUpdatedEvent> processingChoiceEvents = allEvents.stream().filter(EventType.NEW.isOfType().negate()).toList();

        assertEachClaimConsideredOnlyOnce(processingChoiceEvents);
        assertAmountLimitNotExceeded(processingChoiceEvents);
        assertComplexityLimitNotExceeded(processingChoiceEvents);
        assertProcessingOrder(processingChoiceEvents);
    }

    private static void assertEachClaimConsideredOnlyOnce(List<ClaimUpdatedEvent> events) {
        assertThat(events).filteredOn(EventType.APPROVED.isOfType().or(EventType.POSTPONED.isOfType())).map(ClaimUpdatedEvent::claim).doesNotHaveDuplicates();
    }

    private static void assertProcessingOrder(List<ClaimUpdatedEvent> allUpdates) {
        List<Claim> processedClaims = allUpdates.stream().filter(EventType.NEW.isOfType().negate()).map(ClaimUpdatedEvent::claim).toList();
        List<Claim> medicalClaims = processedClaims.stream().filter(c -> ClaimType.MEDICAL == c.type()).toList();
        assertThat(medicalClaims).isSortedAccordingTo(ClaimComparators.forMedical());

        List<Claim> vehicleClaims = processedClaims.stream().filter(c -> ClaimType.VEHICLE == c.type()).toList();
        assertThat(vehicleClaims).isSortedAccordingTo(ClaimComparators.forVehicle());

        List<Claim> propertyClaims = processedClaims.stream().filter(c -> ClaimType.PROPERTY == c.type()).toList();
        assertThat(propertyClaims).isSortedAccordingTo(ClaimComparators.forProperty());
    }

    private static void assertComplexityLimitNotExceeded(List<ClaimUpdatedEvent> result) {
        List<ClaimUpdatedEvent> complexClaimsApprovals = result.stream().filter(EventType.APPROVED.isOfType()).filter(u -> u.claim().complexity() == ComplexityLevel.HIGH).toList();
        assertThat(complexClaimsApprovals).hasSizeLessThanOrEqualTo(2);
    }

    private static void assertAmountLimitNotExceeded(List<ClaimUpdatedEvent> result) {
        List<ClaimUpdatedEvent> approvedClaims = result.stream().filter(EventType.APPROVED.isOfType()).toList();
        BigDecimal sumOfClaimAmounts = approvedClaims.stream().map(ClaimUpdatedEvent::claim).map(Claim::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(sumOfClaimAmounts).isLessThanOrEqualTo(BigDecimal.valueOf(15000L));
    }

    public static String forPrint(Claim claim) {
        return claim.id() + ": " + claim.amount() + " (" + claim.complexity().name() + ")";
    }

    public static void prettyPrint(List<ClaimUpdatedEvent> updates, LocalDate date) {

        System.out.println("=== Dzień " + date + " ===");
        List<Claim> approved = updates.stream()
                .filter(u -> EventType.APPROVED == u.eventType())
                .map(ClaimUpdatedEvent::claim)
                .toList();
        approved.forEach(claim -> System.out.println(forPrint(claim)));
        System.out.println();
    }
}
