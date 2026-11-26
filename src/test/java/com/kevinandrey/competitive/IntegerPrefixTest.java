package com.kevinandrey.competitive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class IntegerPrefixTest {

    @ParameterizedTest
    @CsvSource({
        "23082019UNAL, 23082019",
        "_1234567890, -1",
        "007bond, 007",
        "42, 42",
        "-5, -1",
        "abc, -1",
    })
    void keepsTheLongestDigitPrefix(String text, String expected) {
        assertEquals(expected, IntegerPrefix.longestNumericPrefix(text));
    }

    @Test
    void prefixesLongerThanAnyNumericTypeAreKeptVerbatim() {
        String digits = "9".repeat(200);

        assertEquals(digits, IntegerPrefix.longestNumericPrefix(digits + "x"));
    }
}
