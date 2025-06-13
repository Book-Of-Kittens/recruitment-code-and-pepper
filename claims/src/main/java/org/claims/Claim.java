package org.claims;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Claim(String id,
                    String type,
                    BigDecimal amount,
                    LocalDate date, // NOTE: in the real world, we would probably need a ZonedDateTime to keep track of different time zones.
                    String complexity) {
}
