package com.kevinandrey.competitive;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * UVa 11659, Informants: informants assert that others (or themselves) are reliable or not. A
 * reliable informant only tells the truth; an unreliable one may say anything. Find the largest
 * set of informants that can all be reliable at the same time.
 *
 * <p>A set S is consistent when every member's assertions hold: everyone a member vouches for is
 * in S and nobody a member distrusts is. With at most 20 informants every subset can be checked
 * using bit masks, O(2^n n), visiting larger subsets first to stop at the first consistent one.
 */
public final class Informants {

    static final int MAX_INFORMANTS = 20;

    /**
     * One survey answer.
     *
     * @param informant who speaks, from 1
     * @param subject who is judged, from 1
     * @param reliable whether the subject is said to be reliable
     */
    public record Assertion(int informant, int subject, boolean reliable) {
    }

    private Informants() {
    }

    /**
     * Returns the maximum number of informants that can be reliable together.
     *
     * @param informants number of informants, 1 to 20
     * @param assertions survey answers
     * @return size of the largest consistent set
     */
    public static int maximumReliable(int informants, List<Assertion> assertions) {
        if (informants < 1 || informants > MAX_INFORMANTS) {
            throw new IllegalArgumentException("informants must be between 1 and " + MAX_INFORMANTS);
        }
        int[] trusted = new int[informants];
        int[] distrusted = new int[informants];
        for (Assertion assertion : assertions) {
            checkRange(assertion.informant(), informants);
            checkRange(assertion.subject(), informants);
            int bit = 1 << (assertion.subject() - 1);
            if (assertion.reliable()) {
                trusted[assertion.informant() - 1] |= bit;
            } else {
                distrusted[assertion.informant() - 1] |= bit;
            }
        }
        for (int size = informants; size > 0; size--) {
            for (int set = (1 << size) - 1; set < 1 << informants; set = nextWithSameBits(set)) {
                if (consistent(set, trusted, distrusted)) {
                    return size;
                }
            }
        }
        return 0;
    }

    private static boolean consistent(int set, int[] trusted, int[] distrusted) {
        for (int member = 0; member < trusted.length; member++) {
            boolean included = (set & (1 << member)) != 0;
            if (included && ((set & trusted[member]) != trusted[member] || (set & distrusted[member]) != 0)) {
                return false;
            }
        }
        return true;
    }

    /** Gosper's hack: the next larger integer with the same number of set bits. */
    static int nextWithSameBits(int set) {
        int lowest = set & -set;
        int ripple = set + lowest;
        return ripple | (((set ^ ripple) >>> 2) / lowest);
    }

    private static void checkRange(int informant, int informants) {
        if (informant < 1 || informant > informants) {
            throw new IllegalArgumentException("informant " + informant + " is out of range");
        }
    }

    /** Reads cases until the line "0 0" and prints one maximum per case. */
    static void solve(BufferedReader input, PrintWriter output) {
        Tokens tokens = new Tokens(input);
        while (tokens.hasNext()) {
            int informants = tokens.nextInt();
            int answers = tokens.nextInt();
            if (informants == 0 && answers == 0) {
                return;
            }
            List<Assertion> assertions = new ArrayList<>(answers);
            for (int index = 0; index < answers; index++) {
                int informant = tokens.nextInt();
                int subject = tokens.nextInt();
                assertions.add(new Assertion(informant, Math.abs(subject), subject > 0));
            }
            output.println(maximumReliable(informants, assertions));
        }
    }
}
