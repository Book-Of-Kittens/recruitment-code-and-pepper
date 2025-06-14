package org.rules;

import org.engine.Engine;
import org.engine.EngineTestConfiguration;
import org.junit.Test;

public class ComparatorsTest {
    @Test
    public void shouldDoThings() {
        RulesConfiguration.getComparatorsByType();

        Engine engine = EngineTestConfiguration.getEngine();
        engine.process();
    }

}
