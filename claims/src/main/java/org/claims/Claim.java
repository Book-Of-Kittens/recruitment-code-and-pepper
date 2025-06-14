package org.claims;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Claim(String id,
                    ClaimType type,
                    BigDecimal amount,
                    LocalDate deadline, // NOTE: in the real world, we would probably need a ZonedDateTime to keep track of different time zones.
                    ComplexityLevel complexity
) {
}
