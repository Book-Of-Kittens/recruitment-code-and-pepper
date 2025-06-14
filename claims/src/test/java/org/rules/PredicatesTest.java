package org.rules;

import org.claims.TestConfiguration;
import org.junit.Test;
import org.resources.ResourcesService;

import java.util.List;

public class PredicatesTest {
    @Test
    public void shouldDoThings() {
        ResourcesService resourcesService = TestConfiguration.resourcesService;
        List<UpdatablePredicate> predicateList = BusinessRulesConfiguration.approvalPredicates(resourcesService);
        /* TODO: test configured service instead */
    }


}
