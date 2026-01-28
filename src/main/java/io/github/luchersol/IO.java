package io.github.luchersol;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IO {
    
    /**
     * Writes a string representation of the specified object and then writes
     * a line separator to the standard output.
     *
     * <p> The effect is as if {@link java.io.PrintStream#println(Object) println(obj)}
     * had been called on {@code System.out}.
     *
     * @param obj the object to print, may be {@code null}
     */
    public static void println(Object obj) {
        System.out.println(obj);
    }

    /**
     * Writes a line separator to the standard output.
     *
     * <p> The effect is as if {@link java.io.PrintStream#println() println()}
     * had been called on {@code System.out}.
     */
    public static void println() {
        System.out.println();
    }

    /**
     * Writes a string representation of the specified object to the
     * standard output.
     *
     * <p> The effect is as if {@link java.io.PrintStream#print(Object) print(obj)}
     * had been called on {@code System.out}.
     *
     * @param obj the object to print, may be {@code null}
     */
    public static void print(Object obj) {
        var out = System.out;
        out.print(obj);
        out.flush();
    }

    /**
     * Reads a single line of text from the standard input.
     * <p>
     * One line is read from the decoded input as if by
     * {@link java.io.BufferedReader#readLine() BufferedReader.readLine()}
     * and then the result is returned.
     * <p>
     * If necessary, this method first sets up charset decoding, as described in
     * above in the class specification.
     *
     * @return a string containing the line read from the standard input, not
     * including any line separator characters. Returns {@code null} if an
     * end of stream has been reached without having read any characters.
     *
     * @throws IOError if an I/O error occurs
     */
    public static String readln() {
        try {
            return reader().readLine();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    /**
     * Writes a prompt and then reads a line of input.
     * <p>
     * Writes a prompt as if by calling {@link #print print}, and then reads a single
     * line of text as if by calling {@link #readln() readln}.
     * <p>
     * If necessary, this method first sets up charset decoding, as described in
     * above in the class specification.
     *
     * @param prompt the prompt string, may be {@code null}
     *
     * @return a string containing the line read from the standard input, not
     * including any line separator characters. Returns {@code null} if an
     * end of stream has been reached without having read any characters.
     *
     * @throws IOError if an I/O error occurs
     */
    public static String readln(String prompt) {
        print(prompt);
        return readln();
    }

    /**
     * The BufferedReader used by readln(). Initialized under a class lock by
     * the reader() method. All access should be through the reader() method.
     */
    private static BufferedReader br;

    /**
     * On the first call, creates an InputStreamReader to decode characters from
     * System.in, wraps it in a BufferedReader, and returns the BufferedReader.
     * These objects are cached and returned by subsequent calls.
     *
     * @return the internal BufferedReader instance
     */
    static synchronized BufferedReader reader() {
        if (br == null) {
            String enc = System.getProperty("stdin.encoding", "");
            Charset cs = Charset.forName(enc, StandardCharsets.UTF_8);
            br = new BufferedReader(new InputStreamReader(System.in, cs));
        }
        return br;
    }
}
