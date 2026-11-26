package com.kevinandrey.competitive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CommonSubsequenceTest {

    private static final String BASES = "ACGT";

    @ParameterizedTest
    @CsvSource({
        "ABCBDAB, BDCABA, 4",
        "ACGT, ACGT, 4",
        "AAAA, CCCC, 0",
        "GATTACA, TACGATA, 4",
    })
    void referenceLcsMatchesKnownValues(String a, String b, int expected) {
        assertEquals(expected, CommonSubsequence.longestCommonSubsequence(a, b));
    }

    @Test
    void rejectsSamplesOfDifferentLength() {
        assertThrows(IllegalArgumentException.class, () -> CommonSubsequence.areBrothers("ACG", "AC"));
    }

    @Test
    void shortSamplesMustBeIdentical() {
        assertTrue(CommonSubsequence.areBrothers("ACGT", "ACGT"));
        assertFalse(CommonSubsequence.areBrothers("ACGT", "ACGA"));
    }

    @Test
    void agreesWithTheQuadraticLcsAroundTheThreshold() {
        Random random = new Random(102307);
        for (int round = 0; round < 400; round++) {
            int n = 100 + random.nextInt(500);
            String a = randomDna(random, n);
            String b = mutate(random, a, random.nextInt(n / 100 + 3));
            boolean expected = 100L * CommonSubsequence.longestCommonSubsequence(a, b) >= 99L * n;
            assertEquals(expected, CommonSubsequence.areBrothers(a, b), () -> a + " / " + b);
        }
    }

    @Test
    void handlesTheLargestInputQuickly() {
        Random random = new Random(7);
        int n = 100_000;
        String a = randomDna(random, n);
        String shifted = mutate(random, a, n / 100);
        String unrelated = randomDna(random, n);

        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            assertTrue(CommonSubsequence.areBrothers(a, shifted));
            assertFalse(CommonSubsequence.areBrothers(a, unrelated));
        });
    }

    private static String randomDna(Random random, int length) {
        StringBuilder dna = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            dna.append(BASES.charAt(random.nextInt(BASES.length())));
        }
        return dna.toString();
    }

    /** Deletes {@code edits} random characters and appends as many random ones. */
    private static String mutate(Random random, String dna, int edits) {
        StringBuilder mutated = new StringBuilder(dna);
        for (int i = 0; i < edits; i++) {
            mutated.deleteCharAt(random.nextInt(mutated.length()));
        }
        for (int i = 0; i < edits; i++) {
            mutated.insert(random.nextInt(mutated.length() + 1), BASES.charAt(random.nextInt(BASES.length())));
        }
        return mutated.toString();
    }
}
