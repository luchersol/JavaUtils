package io.github.luchersol;

import java.util.function.Function;

public class Checker {

    public static void check(boolean condition, Function<String, ? extends Exception> f, String message) throws Throwable {
        if(!condition) throw f.apply(message);
    }
    
    public static void check(boolean condition, String message) {
        if(!condition) throw new IllegalArgumentException(message);
    }

    public static void check(boolean condition) {
        if(!condition) throw new IllegalArgumentException("Check error");
    }

}
