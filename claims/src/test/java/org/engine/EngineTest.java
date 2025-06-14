package org.engine;

import org.claims.Claim;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EngineTest {

    @Test
    public void shouldProcessSomething() {

        Engine engine = EngineTestConfiguration.getEngine();
        engine.process();
    }

    private static void prettyPrint(LocalDate day, List<Claim> claims){
        System.out.println("=="+day.format(DateTimeFormatter.ISO_DATE)+"==");
        claims.forEach(claim -> System.out.println(claim.id() +" "+claim.amount()+" "+claim.complexity()));
    }
}