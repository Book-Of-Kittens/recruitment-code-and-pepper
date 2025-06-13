package org.claims;

import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryClaimsRepositoryTest {

    private static final URL SAMPLES = InMemoryClaimsRepository.class.getResource("/samples/claims.csv");

    @Test
    public void sanity_test() {

        // given
        ClaimsRepository claimsRepository = new InMemoryClaimsRepository(SAMPLES);

        // when
        List<Claim> unprocessedClaims = claimsRepository.getAllUnprocessedClaims();

        // then
        assertThat(unprocessedClaims.size()).isEqualTo(50);


    }
}