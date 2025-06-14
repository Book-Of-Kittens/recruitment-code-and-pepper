package org.engine;

import org.claims.Claim;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ClaimProcessingServiceTest {

    private static void prettyPrint(LocalDate day, List<Claim> claims) {
        System.out.println("==" + day.format(DateTimeFormatter.ISO_DATE) + "==");
        claims.forEach(claim -> System.out.println(claim.id() + " " + claim.amount() + " " + claim.complexity().name()));
    }

    @Test
    public void shouldProcessSomething() {

        ClaimProcessingService claimProcessingService = EngineTestConfiguration.getEngine();
        claimProcessingService.process();
    }
}