package com.kevinandrey.competitive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kevinandrey.competitive.Informants.Assertion;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class InformantsTest {

    @Test
    void solvesTheStatementExample() {
        List<Assertion> survey = List.of(
                new Assertion(1, 2, true),
                new Assertion(1, 4, false),
                new Assertion(2, 3, false),
                new Assertion(3, 1, true),
                new Assertion(3, 4, true));

        assertEquals(2, Informants.maximumReliable(4, survey));
    }

    @Test
    void everyoneIsReliableWithoutAnswers() {
        assertEquals(20, Informants.maximumReliable(20, List.of()));
    }

    @Test
    void informantsDistrustingThemselvesAreNeverReliable() {
        assertEquals(1, Informants.maximumReliable(2, List.of(new Assertion(1, 1, false))));
    }

    @Test
    void vouchingForAnUnreliableInformantDragsTheVoucherDown() {
        List<Assertion> survey = List.of(new Assertion(1, 2, true), new Assertion(2, 2, false));

        assertEquals(1, Informants.maximumReliable(3, survey));
    }

    @Test
    void validatesItsInput() {
        assertThrows(IllegalArgumentException.class, () -> Informants.maximumReliable(0, List.of()));
        assertThrows(IllegalArgumentException.class, () -> Informants.maximumReliable(21, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> Informants.maximumReliable(2, List.of(new Assertion(3, 1, true))));
        assertThrows(IllegalArgumentException.class,
                () -> Informants.maximumReliable(2, List.of(new Assertion(1, 0, true))));
    }

    @Test
    void gosperEnumeratesSubsetsOfTheSameSizeInOrder() {
        List<Integer> subsets = new ArrayList<>();
        for (int set = 0b0011; set < 1 << 4; set = Informants.nextWithSameBits(set)) {
            subsets.add(set);
        }

        assertEquals(List.of(0b0011, 0b0101, 0b0110, 0b1001, 0b1010, 0b1100), subsets);
    }

    @Test
    void agreesWithExhaustiveSearchOnRandomSurveys() {
        Random random = new Random(11659);
        for (int round = 0; round < 500; round++) {
            int informants = 1 + random.nextInt(10);
            List<Assertion> survey = new ArrayList<>();
            int answers = random.nextInt(3 * informants);
            for (int i = 0; i < answers; i++) {
                survey.add(new Assertion(
                        1 + random.nextInt(informants), 1 + random.nextInt(informants), random.nextBoolean()));
            }
            assertEquals(exhaustive(informants, survey), Informants.maximumReliable(informants, survey));
        }
    }

    private static int exhaustive(int informants, List<Assertion> survey) {
        int best = 0;
        for (int set = 0; set < 1 << informants; set++) {
            if (holds(set, survey)) {
                best = Math.max(best, Integer.bitCount(set));
            }
        }
        return best;
    }

    private static boolean holds(int set, List<Assertion> survey) {
        for (Assertion assertion : survey) {
            boolean speakerReliable = (set >> (assertion.informant() - 1) & 1) == 1;
            boolean subjectReliable = (set >> (assertion.subject() - 1) & 1) == 1;
            if (speakerReliable && subjectReliable != assertion.reliable()) {
                return false;
            }
        }
        return true;
    }
}
