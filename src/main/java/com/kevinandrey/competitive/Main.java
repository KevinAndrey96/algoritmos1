package com.kevinandrey.competitive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.function.BiConsumer;

/** Runs one solution against standard input, in the exact format of its judge. */
public final class Main {

    static final int USAGE_ERROR = 2;

    private static final Map<String, BiConsumer<BufferedReader, PrintWriter>> PROBLEMS =
            new TreeMap<>(Map.of(
                    "burger-time", BurgerTime::solve,
                    "common-subsequence", CommonSubsequence::solve,
                    "informants", Informants::solve,
                    "integer-prefix", IntegerPrefix::solve));

    private Main() {
    }

    /**
     * Entry point: {@code java -jar competitive.jar <problem> < input.txt}.
     *
     * @param args the problem name
     */
    public static void main(String[] args) {
        Reader stdin = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        int status = run(args, stdin, System.out, System.err);
        if (status != 0) {
            System.exit(status);
        }
    }

    static int run(String[] args, Reader input, PrintStream out, PrintStream err) {
        if (args.length != 1 || !PROBLEMS.containsKey(args[0])) {
            err.println("uso: java -jar competitive.jar <problema> < entrada.txt");
            err.println("problemas: " + String.join(", ", PROBLEMS.keySet()));
            return USAGE_ERROR;
        }
        StringWriter buffer = new StringWriter();
        try (PrintWriter writer = new PrintWriter(buffer)) {
            PROBLEMS.get(args[0]).accept(new BufferedReader(input), writer);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            err.println("entrada inválida: " + e.getMessage());
            return USAGE_ERROR;
        }
        out.print(buffer);
        out.flush();
        return 0;
    }
}
