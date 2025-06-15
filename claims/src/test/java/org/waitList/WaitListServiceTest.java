package org.waitList;

import org.claims.Claim;
import org.claims.ClaimType;
import org.claims.SampleFromFile;
import org.events.ClaimEventsBus;
import org.events.EventsConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.events.EventTestUtils.putNewClaimsOnEventBus;

public class WaitListServiceTest {

    private static List<Claim> getAllClaimsForProcessing(WaitListService service) {
        List<Claim> claims = new ArrayList<>();
        while (service.hasClaimsToProcess()) {
            Claim claim = service.getClaimForProcessing();
            claims.add(claim);
        }
        return claims;
    }

    @Test
    public void shouldWorkOnExampleData() {
        ClaimEventsBus events = EventsConfig.createEventBus();
        List<WaitListService> waitListServices = WaitListConfig.getWaitListServices(events);

        List<Claim> claimList = SampleFromFile.with(SampleFromFile.LONG_LIST);
        //    List<Claim> shortList = SampleFromFile.with(SampleFromFile.SHORT_LIST);
        putNewClaimsOnEventBus(events, claimList);

        assertThat(waitListServices.size()).isEqualTo(3);

        List<Claim> medicalClaims = getAllClaimsForProcessing(waitListServices.get(0));
        List<Claim> vehicleClaims = getAllClaimsForProcessing(waitListServices.get(1));
        List<Claim> propertyClaims = getAllClaimsForProcessing(waitListServices.get(2));

        // THEN
        /* should only ingest configured claim types */
        assertThat(medicalClaims).map(Claim::type).containsOnly(ClaimType.MEDICAL);
        assertThat(vehicleClaims).map(Claim::type).containsOnly(ClaimType.VEHICLE);
        assertThat(propertyClaims).map(Claim::type).containsOnly(ClaimType.PROPERTY);
    }

}
