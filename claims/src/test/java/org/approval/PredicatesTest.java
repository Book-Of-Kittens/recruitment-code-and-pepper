package org.approval;

import org.claims.Claim;
import org.claims.SampleFromFile;
import org.dailyTasks.DailyProcessTestContext;
import org.dailyTasks.DailyTasksTestUtils;
import org.events.ClaimUpdatedEvent;
import org.events.EventType;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.events.EventTestUtils.putNewClaimsOnEventBus;

public class PredicatesTest {

    @Test
    public void testSingleDayOutput() {
        // GIVEN
        DailyProcessTestContext context = new DailyProcessTestContext();
        List<Claim> inputClaims = SampleFromFile.with(SampleFromFile.LONG_LIST);
        putNewClaimsOnEventBus(context.events, inputClaims);

        // WHEN
        List<ClaimUpdatedEvent> updates = DailyTasksTestUtils.runDay(context);

        // THEN
        DailyTasksTestUtils.prettyPrint(0, updates);
        DailyTasksTestUtils.standardDailyOutputAssertions(updates);
        // assert that all claims had been considered for processing in a day
        assertThat(updates).filteredOn(EventType.APPROVED.isOfType().or(EventType.POSTPONED.isOfType())).map(ClaimUpdatedEvent::claim).hasSameElementsAs(inputClaims);

    }

}
