package com.kevinandrey.competitive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MainTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    private int run(String input, String... args) {
        return Main.run(
                args,
                new StringReader(input),
                new PrintStream(out, true, StandardCharsets.UTF_8),
                new PrintStream(err, true, StandardCharsets.UTF_8));
    }

    @Test
    void unknownProblemsPrintTheUsage() {
        assertEquals(Main.USAGE_ERROR, run("", "fibonacci"));
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("burger-time, common-subsequence"));
    }

    @Test
    void missingArgumentsPrintTheUsage() {
        assertEquals(Main.USAGE_ERROR, run(""));
        assertTrue(err.toString(StandardCharsets.UTF_8).startsWith("uso:"));
    }

    @Test
    void truncatedInputIsReportedWithoutPartialOutput() {
        assertEquals(Main.USAGE_ERROR, run("2\nRD\n3\n", "burger-time"));
        assertEquals("", out.toString(StandardCharsets.UTF_8));
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("unexpected end of input"));
    }

    @Test
    void malformedNumbersAreReported() {
        assertEquals(Main.USAGE_ERROR, run("dos 1\n", "informants"));
        assertTrue(err.toString(StandardCharsets.UTF_8).startsWith("entrada inválida"));
    }

    @Test
    void highwaysOfTheWrongLengthAreReported() {
        assertEquals(Main.USAGE_ERROR, run("3\nRD\n0\n", "burger-time"));
        assertTrue(err.toString(StandardCharsets.UTF_8).contains("expected 3"));
    }
}
