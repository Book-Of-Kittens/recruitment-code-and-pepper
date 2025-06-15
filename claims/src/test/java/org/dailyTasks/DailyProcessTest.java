package org.dailyTasks;

import org.claims.Claim;
import org.claims.SampleFromFile;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.events.EventTestUtils.putNewClaimsOnEventBus;

public class DailyProcessTest {

    @Test
    public void testSingleDayOutput() {
        // GIVEN
        DailyProcessTestContext context = new DailyProcessTestContext();
        List<Claim> inputClaims = SampleFromFile.with(SampleFromFile.LONG_LIST);
        putNewClaimsOnEventBus(context.events, inputClaims);

        // WHEN
        List<ClaimUpdatedEvent> updates = DailyTasksTestUtils.runDay(context);

        // THEN
        DailyTasksTestUtils.standardDailyOutputAssertions(updates);
        // assert that all claims had been considered for processing in a day
        assertThat(updates).filteredOn(EventType.APPROVED.isOfType().or(EventType.POSTPONED.isOfType())).map(ClaimUpdatedEvent::claim).hasSameElementsAs(inputClaims);

    }

    @Test
    public void testMultipleDaysOutput() {
        // GIVEN
        DailyProcessTestContext context = new DailyProcessTestContext();

        List<Claim> input = SampleFromFile.with(SampleFromFile.LONG_LIST);
        putNewClaimsOnEventBus(context.events, input);

        // WHEN
        Map<Integer, List<ClaimUpdatedEvent>> updatesByDay = DailyTasksTestUtils.runDailyProcessInALoop(context);

        // THEN
        LocalDate date = LocalDate.parse("2025-06-02");
        for (Map.Entry<Integer, List<ClaimUpdatedEvent>> daySet : updatesByDay.entrySet()) {
            DailyTasksTestUtils.prettyPrint(daySet.getValue(), date);
            DailyTasksTestUtils.standardDailyOutputAssertions(daySet.getValue());
            date = date.plusDays(1);
        }

        // assert that all claims have been approved for processing after enough days
        List<ClaimUpdatedEvent> allEvents = updatesByDay.values().stream().flatMap(Collection::stream).toList();
        assertThat(allEvents).filteredOn(EventType.APPROVED.isOfType()).map(ClaimUpdatedEvent::claim).hasSameElementsAs(input);
    }


}