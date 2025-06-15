package org.rules;

import org.claims.Claim;
import org.claims.ComplexityLevel;

import java.util.Comparator;

public class ClaimComparators {

    public static Comparator<Claim> forMedical() {
        return earlierDeadlineFirst().thenComparing(smallerAmountFirst().reversed());
        // older deadline, bigger amount
    }

    public static Comparator<Claim> forVehicle() {
        return highComplexityFirst().thenComparing(earlierDeadlineFirst()).thenComparing(smallerAmountFirst().reversed());
        //   bigger complexity, older deadline, bigger amount;
    }

    public static Comparator<Claim> forProperty() {
        return smallerAmountFirst().thenComparing(earlierDeadlineFirst());
        //  smaller amount, bigger deadline;
    }

    // Partial:
    static Comparator<Claim> earlierDeadlineFirst() {
        return Comparator.comparing(Claim::deadline);
    }

    static Comparator<Claim> smallerAmountFirst() {
        return Comparator.comparing(Claim::amount);
    }

    static Comparator<Claim> highComplexityFirst() {
        return Comparator.comparing(claim ->
                ComplexityLevel.HIGH != claim.complexity());
    }

}
