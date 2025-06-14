package org.rules;

import org.claims.Claim;
import org.claims.TestConfiguration;
import org.junit.Test;
import org.resources.ResourcesService;

import java.util.function.Predicate;

public class PredicatesTest {
    @Test
    public void shouldDoThings() {
        ResourcesService resourcesService = TestConfiguration.resourcesService;
        Predicate<Claim> claimPredicate = BusinessRulesConfiguration.commonApprovalPredicate(resourcesService);
        /* TODO: test configured service instead */
    }


}
