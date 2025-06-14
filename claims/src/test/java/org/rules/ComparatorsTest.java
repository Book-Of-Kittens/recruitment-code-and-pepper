package org.rules;

import org.engine.ClaimProcessingService;
import org.engine.EngineTestConfiguration;
import org.junit.Test;

public class ComparatorsTest {
    @Test
    public void shouldDoThings() {
        RulesConfiguration.getComparatorsByType();

        ClaimProcessingService claimProcessingService = EngineTestConfiguration.getEngine();
        claimProcessingService.process();
    }

}
