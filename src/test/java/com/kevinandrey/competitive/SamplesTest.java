package com.kevinandrey.competitive;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SamplesTest {

    @ParameterizedTest(name = "{0} con {1}")
    @CsvSource({
        "burger-time, burger-time",
        "common-subsequence, common-subsequence-1",
        "common-subsequence, common-subsequence-2",
        "informants, informants",
        "integer-prefix, integer-prefix-1",
        "integer-prefix, integer-prefix-2",
    })
    void judgeSamplesProduceTheExpectedOutput(String problem, String sample) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        int status;
        try (InputStream input = resource(sample + ".in")) {
            status = Main.run(
                    new String[] {problem},
                    new InputStreamReader(input, StandardCharsets.UTF_8),
                    new PrintStream(out, true, StandardCharsets.UTF_8),
                    new PrintStream(err, true, StandardCharsets.UTF_8));
        }

        assertEquals(0, status, err.toString(StandardCharsets.UTF_8));
        try (InputStream expected = resource(sample + ".out")) {
            assertEquals(
                    new String(expected.readAllBytes(), StandardCharsets.UTF_8),
                    out.toString(StandardCharsets.UTF_8).replace(System.lineSeparator(), "\n"));
        }
    }

    private static InputStream resource(String name) {
        InputStream stream = SamplesTest.class.getResourceAsStream("/samples/" + name);
        if (stream == null) {
            throw new IllegalStateException("missing sample " + name);
        }
        return stream;
    }
}
