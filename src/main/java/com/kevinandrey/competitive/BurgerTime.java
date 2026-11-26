package com.kevinandrey.competitive;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * UVa 11661, Burger Time?: minimum distance between a restaurant (R) and a drugstore (D) on a
 * highway, where Z holds both and dots are empty places.
 *
 * <p>The closest R-D pair is always formed by two consecutive non-empty places, so a single pass
 * that remembers the last non-empty place is enough: O(L) time and O(1) memory.
 */
public final class BurgerTime {

    private BurgerTime() {
    }

    /**
     * Returns the minimum distance, or -1 if the highway lacks a restaurant or a drugstore.
     *
     * @param highway places made of the characters R, D, Z and '.'
     * @return the minimum distance between a restaurant and a drugstore
     */
    public static int minimumDistance(CharSequence highway) {
        int best = Integer.MAX_VALUE;
        int lastPosition = -1;
        char lastKind = '.';
        for (int position = 0; position < highway.length(); position++) {
            char kind = highway.charAt(position);
            switch (kind) {
                case 'Z' -> {
                    return 0;
                }
                case 'R', 'D' -> {
                    if (lastPosition >= 0 && kind != lastKind) {
                        best = Math.min(best, position - lastPosition);
                    }
                    lastPosition = position;
                    lastKind = kind;
                }
                case '.' -> {
                    // empty place
                }
                default -> throw new IllegalArgumentException("unexpected place '" + kind + "'");
            }
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }

    /** Reads cases until L = 0 and prints one distance per case. */
    static void solve(BufferedReader input, PrintWriter output) {
        Tokens tokens = new Tokens(input);
        while (tokens.hasNext()) {
            int length = tokens.nextInt();
            if (length == 0) {
                return;
            }
            String highway = tokens.next();
            if (highway.length() != length) {
                throw new IllegalArgumentException(
                        "highway has " + highway.length() + " places, expected " + length);
            }
            output.println(minimumDistance(highway));
        }
    }
}
