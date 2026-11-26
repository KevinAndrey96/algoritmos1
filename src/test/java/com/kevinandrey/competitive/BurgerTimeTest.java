package com.kevinandrey.competitive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BurgerTimeTest {

    @ParameterizedTest
    @CsvSource({
        "RD, 1",
        "DR, 1",
        "Z, 0",
        "R....R.D, 2",
        "D.R.Z.R.D, 0",
        "R...D...R, 4",
        "RRRR, -1",
        "...., -1",
    })
    void findsTheClosestRestaurantAndDrugstore(String highway, int expected) {
        assertEquals(expected, BurgerTime.minimumDistance(highway));
    }

    @Test
    void rejectsUnknownPlaces() {
        assertThrows(IllegalArgumentException.class, () -> BurgerTime.minimumDistance("R.X.D"));
    }

    @Test
    void agreesWithTheQuadraticDefinitionOnRandomHighways() {
        Random random = new Random(11661);
        String alphabet = "RDZ......";
        for (int round = 0; round < 2_000; round++) {
            StringBuilder highway = new StringBuilder();
            int length = 1 + random.nextInt(40);
            for (int i = 0; i < length; i++) {
                highway.append(alphabet.charAt(random.nextInt(alphabet.length())));
            }
            assertEquals(bruteForce(highway), BurgerTime.minimumDistance(highway), highway::toString);
        }
    }

    private static int bruteForce(CharSequence highway) {
        int best = -1;
        for (int i = 0; i < highway.length(); i++) {
            for (int j = 0; j < highway.length(); j++) {
                if (hasRestaurant(highway.charAt(i)) && hasDrugstore(highway.charAt(j))) {
                    int distance = Math.abs(i - j);
                    best = best < 0 ? distance : Math.min(best, distance);
                }
            }
        }
        return best;
    }

    private static boolean hasRestaurant(char place) {
        return place == 'R' || place == 'Z';
    }

    private static boolean hasDrugstore(char place) {
        return place == 'D' || place == 'Z';
    }
}
