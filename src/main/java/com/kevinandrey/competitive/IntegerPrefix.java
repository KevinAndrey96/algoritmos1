package com.kevinandrey.competitive;

import java.io.BufferedReader;
import java.io.PrintWriter;

/**
 * Problem I of the 2019 ICPC Universidad Nacional de Colombia contest (Codeforces Gym 102307I):
 * print the longest non-empty prefix made only of digits, or -1 if there is none.
 *
 * <p>The prefix is printed as written, leading zeros included, and may be far longer than any
 * numeric type, so it is never parsed as a number.
 */
public final class IntegerPrefix {

    static final String NO_PREFIX = "-1";

    private IntegerPrefix() {
    }

    /**
     * Returns the longest numeric prefix, or "-1".
     *
     * @param text text without spaces
     * @return the prefix or "-1"
     */
    public static String longestNumericPrefix(String text) {
        int end = 0;
        while (end < text.length() && text.charAt(end) >= '0' && text.charAt(end) <= '9') {
            end++;
        }
        return end == 0 ? NO_PREFIX : text.substring(0, end);
    }

    static void solve(BufferedReader input, PrintWriter output) {
        output.println(longestNumericPrefix(new Tokens(input).next()));
    }
}
