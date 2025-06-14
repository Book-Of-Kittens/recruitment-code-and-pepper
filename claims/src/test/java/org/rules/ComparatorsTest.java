package org.rules;

import org.claims.Claim;
import org.claims.ClaimType;
import org.junit.Test;

import java.util.Comparator;
import java.util.Map;

public class ComparatorsTest {
    @Test
    public void shouldDoThings() {
        Map<ClaimType, Comparator<Claim>> comparatorsByType = BusinessRulesConfiguration.comparatorsByType;
        /* TODO: test configured service instead */
    }

}
