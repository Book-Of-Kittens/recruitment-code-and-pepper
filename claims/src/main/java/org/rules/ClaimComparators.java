package org.rules;

import org.claims.Claim;
import java.util.Comparator;

public class ClaimComparators {

    public static  Comparator<Claim> forMedical(){
        return byDeadline().thenComparing(byAmount());
        // older deadline, bigger amount
    }

    public static  Comparator<Claim> forVehicle(){
        return byComplexity().thenComparing(byDeadline()).thenComparing(byAmount());
     //   bigger complexity, older deadline, bigger amount;
    }

    public static  Comparator<Claim> forProperty(){
        return byAmount().thenComparing(byDeadline());
        //  smaller amount, bigger deadline;
    }

// Partial:
    static  Comparator<Claim> byDeadline(){
        return Comparator.comparing(Claim::deadline); // TODO: check order
    }

    static  Comparator<Claim> byAmount(){
        return Comparator.comparing(Claim::amount); // TODO: check order
    }

    static  Comparator<Claim> byComplexity(){
        return Comparator.comparing(claim ->
                ComplexityLevel.HIGH.name().equals(claim.complexity())); // TODO: check order
    }

}
