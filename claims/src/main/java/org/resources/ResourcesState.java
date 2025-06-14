package org.resources;

import java.math.BigDecimal;
import java.util.Set;

public record ResourcesState(Set<String> processedIds, BigDecimal budget, int highComplexityCounter) {
}
