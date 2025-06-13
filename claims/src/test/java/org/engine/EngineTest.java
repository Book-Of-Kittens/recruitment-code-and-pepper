package org.engine;

import org.claims.ClaimsRepository;
import org.claims.InMemoryClaimsRepository;
import org.junit.Test;
import org.progress.InMemoryProgressRepository;
import org.progress.ProgressRepository;

import java.net.URL;

public class EngineTest {

    private static final URL SAMPLES = InMemoryClaimsRepository.class.getResource("/samples/claims.csv");

    @Test
    public void should_be_created() {

        ClaimsRepository claimsRepository = new InMemoryClaimsRepository(SAMPLES);
        ProgressRepository progressRepository = new InMemoryProgressRepository();

        Engine engine = new Engine(claimsRepository, progressRepository);
    }
}