package com.kevinandrey.competitive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/** Whitespace separated tokens read lazily, as judges feed standard input. */
final class Tokens {

    private final BufferedReader reader;
    private StringTokenizer line = new StringTokenizer("");

    Tokens(BufferedReader reader) {
        this.reader = reader;
    }

    boolean hasNext() {
        try {
            while (!line.hasMoreTokens()) {
                String next = reader.readLine();
                if (next == null) {
                    return false;
                }
                line = new StringTokenizer(next);
            }
            return true;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("unexpected end of input");
        }
        return line.nextToken();
    }

    int nextInt() {
        return Integer.parseInt(next());
    }
}
