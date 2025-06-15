package org.events;

import org.claims.Claim;

import java.util.List;

public class EventTestUtils {

    public static void putNewClaimsOnEventBus(ClaimEventsBus events, List<Claim> claimList) {
        claimList.stream().map(claim -> new ClaimUpdatedEvent(claim, EventType.NEW))
                .forEach(events::raiseEvent);

    }
}
