package org.engine;

import org.claims.Claim;
import org.claims.TestConfiguration;
import org.junit.Test;
import org.resources.IncomingClaimsService;
import org.resources.ResourcesService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DailyProcessingServiceTest {

    private static void prettyPrint(LocalDate day, List<Claim> claims) {
        System.out.println("==" + day.format(DateTimeFormatter.ISO_DATE) + "==");
        claims.forEach(claim -> System.out.println(claim.id() + " " + claim.amount() + " " + claim.complexity().name()));
    }

    @Test
    public void shouldProcessSomething() {


        ResourcesService resources = TestConfiguration.resourcesService();
        IncomingClaimsService incomingClaimsService = TestConfiguration.incomingClaimsService();
        DailyProcessingService dailyProcessingService = TestConfiguration.DailyProcessingService(resources, incomingClaimsService);
        dailyProcessingService.processDay();


    }
}