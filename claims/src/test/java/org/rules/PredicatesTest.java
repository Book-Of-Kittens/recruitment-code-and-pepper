package org.rules;

import org.claims.Claim;
import org.claims.SampleFromFile;
import org.junit.Test;
import org.resources.InMemoryResourcesService;
import org.resources.ResourcesService;

import java.util.List;

public class PredicatesTest {
    @Test
    public void shouldDoThings() {
        ResourcesService resourcesService = new InMemoryResourcesService();
        List<UpdatablePredicate> predicateList = RulesConfiguration.getPredicateList(resourcesService);
        List<Claim> sample = SampleFromFile.withDefaultData();

    }


}
