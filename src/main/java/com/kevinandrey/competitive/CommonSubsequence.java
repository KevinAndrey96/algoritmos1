package com.kevinandrey.competitive;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * Problem C of the 2019 ICPC Universidad Nacional de Colombia contest (Codeforces Gym 102307C):
 * two DNA strings of equal length n (up to 10^5) are brothers when they share a common
 * subsequence of length at least 0.99 n.
 *
 * <p>A general LCS is O(n^2), too slow. But a common subsequence that long discards at most
 * k = floor(n / 100) characters of each string, so the search runs over deletion counts instead:
 * {@code reach[i][j]} is the longest common prefix matched after skipping i characters of A and j
 * of B, extended greedily while characters agree. That is O(k^2 + n k) time.
 */
public final class CommonSubsequence {

    static final String BROTHERS = "Long lost brothers D:";
    static final String NOT_BROTHERS = "Not brothers :(";

    private CommonSubsequence() {
    }

    /**
     * Decides whether the strings share a common subsequence of length at least 99% of n.
     *
     * @param a first string
     * @param b second string, of the same length
     * @return true if they are brothers
     */
    public static boolean areBrothers(String a, String b) {
        if (a.length() != b.length()) {
            throw new IllegalArgumentException("both samples must have the same length");
        }
        int n = a.length();
        int budget = n / 100;
        int[][] reach = new int[budget + 2][budget + 2];
        int best = 0;
        for (int skippedA = 0; skippedA <= budget; skippedA++) {
            for (int skippedB = 0; skippedB <= budget; skippedB++) {
                int matched = reach[skippedA][skippedB];
                while (skippedA + matched < n
                        && skippedB + matched < n
                        && a.charAt(skippedA + matched) == b.charAt(skippedB + matched)) {
                    matched++;
                }
                reach[skippedA][skippedB] = matched;
                reach[skippedA + 1][skippedB] = Math.max(reach[skippedA + 1][skippedB], matched);
                reach[skippedA][skippedB + 1] = Math.max(reach[skippedA][skippedB + 1], matched);
                best = Math.max(best, matched);
            }
        }
        return 100L * best >= 99L * n;
    }

    /**
     * Classic O(n m) dynamic programming, kept as a reference for the tests.
     *
     * @param a first string
     * @param b second string
     * @return the length of the longest common subsequence
     */
    public static int longestCommonSubsequence(String a, String b) {
        int[] previous = new int[b.length() + 1];
        int[] current = new int[b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                current[j] = a.charAt(i - 1) == b.charAt(j - 1)
                        ? previous[j - 1] + 1
                        : Math.max(previous[j], current[j - 1]);
            }
            int[] swap = previous;
            previous = current;
            current = swap;
        }
        return previous[b.length()];
    }

    static void solve(BufferedReader input, PrintWriter output) {
        Tokens tokens = new Tokens(input);
        output.println(areBrothers(tokens.next(), tokens.next()) ? BROTHERS : NOT_BROTHERS);
    }
}
