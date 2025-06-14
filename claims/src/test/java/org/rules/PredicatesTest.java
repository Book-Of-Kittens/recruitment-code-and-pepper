package org.rules;

import org.junit.Test;
import org.resources.InMemoryResourcesService;
import org.resources.ResourcesService;

import java.util.List;

public class PredicatesTest {
    @Test
    public void shouldDoThings() {
        ResourcesService resourcesService = new InMemoryResourcesService();
        List<UpdatablePredicate> predicateList = BusinessRulesConfiguration.approvalPredicates(resourcesService);
        /* TODO: test configured service instead */
    }


}
