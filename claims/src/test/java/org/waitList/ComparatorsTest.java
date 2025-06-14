package org.waitList;

import org.claims.Claim;
import org.claims.ComplexityLevel;
import org.junit.Test;
import org.rules.ClaimComparators;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.claims.ComplexityLevel.HIGH;
import static org.claims.ComplexityLevel.MEDIUM;

public class ComparatorsTest {

    private static final LocalDate oldDeadline = LocalDate.parse("2015-02-20");
    private static final LocalDate midDeadLine = LocalDate.parse("2015-02-21");
    private static final LocalDate newDeadline = LocalDate.parse("2015-02-22");

    private static final BigDecimal biggerAmount = BigDecimal.valueOf(100.03d);
    private static final BigDecimal midAmount = BigDecimal.valueOf(100.02d);
    private static final BigDecimal smallerAmount = BigDecimal.valueOf(100.01d);

    private static final List<Claim> claims = List.of(
            claim(smallerAmount, oldDeadline, MEDIUM),
            claim(smallerAmount, newDeadline, MEDIUM),
            claim(midAmount, midDeadLine, HIGH),
            claim(biggerAmount, oldDeadline, MEDIUM),
            claim(biggerAmount, newDeadline, MEDIUM)
    );

    private static Claim claim(BigDecimal amount, LocalDate deadline, ComplexityLevel complexity) {
        return new Claim(null, null, amount, deadline, complexity);
    }

    @Test
    public void testMedicalClaimsComparator() {
        Comparator<Claim> claimComparator = ClaimComparators.forMedical();


        List<Claim> result = claims.stream().sorted(claimComparator).toList();

        // older deadline first, then bigger amount
        // Dla MEDICAL: najpierw starsze roszczenia (deadline), potem większa kwota.
        // TODO: verify assumptions
        List<Claim> expectedResult = List.of(
                claim(biggerAmount, oldDeadline, MEDIUM),
                claim(smallerAmount, oldDeadline, MEDIUM),

                claim(midAmount, midDeadLine, HIGH),

                claim(biggerAmount, newDeadline, MEDIUM),
                claim(smallerAmount, newDeadline, MEDIUM)
        );

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testVehicleClaimsComparator() {
        Comparator<Claim> claimComparator = ClaimComparators.forVehicle();
        List<Claim> result = claims.stream().sorted(claimComparator).toList();

        // VEHICLE: najpierw roszczenia wysokiej złożoności, potem termin (znów starszy deadline ma pierwszeństwo?), potem kwota (znowu rosnąco?).
        // high complexity first, then older deadline, then bigger amount
        // TODO: verify assumptions

        List<Claim> expectedResult = List.of(

                claim(midAmount, midDeadLine, HIGH),

                claim(biggerAmount, oldDeadline, MEDIUM),
                claim(smallerAmount, oldDeadline, MEDIUM),

                claim(biggerAmount, newDeadline, MEDIUM),
                claim(smallerAmount, newDeadline, MEDIUM)
        );

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void testPropertyClaimsComparator() {
        Comparator<Claim> claimComparator = ClaimComparators.forProperty();
        List<Claim> result = claims.stream().sorted(claimComparator).toList();

        // small amount first, then older deadline
        // PROPERTY: najpierw kwota malejąco, potem deadline.(znów starszy deadline ma pierwszeństwo?)
        // TODO: verify assumptions

        List<Claim> expectedResult = List.of(
                claim(smallerAmount, oldDeadline, MEDIUM),
                claim(smallerAmount, newDeadline, MEDIUM),

                claim(midAmount, midDeadLine, HIGH),

                claim(biggerAmount, oldDeadline, MEDIUM),
                claim(biggerAmount, newDeadline, MEDIUM)
        );

        assertThat(result).isEqualTo(expectedResult);
    }

}
