package org.claims;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Claim(String id,
                    ClaimType type,
                    BigDecimal amount,
                    LocalDate deadline, // NOTE: in the real world, we would probably need a ZonedDateTime to keep track of different time zones.
                    ComplexityLevel complexity,
                    LocalDate postponeDate) { /* TODO: maybe a separate type depending on if if it's waiting or new? */

    public String prettyFormat() {
        return id + ", " + type.name() + ", " + complexity;
    } // TODO: remove when test get assertions
}
