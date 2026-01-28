package io.github.luchersol;

import java.text.Normalizer;
import java.util.Map;

public class String2 {
    public static String swapCharacters(String str, Map<Character, Character> replacements) {
        str = Normalizer.normalize(str, Normalizer.Form.NFC);
        StringBuilder result = new StringBuilder(str.length());
        for (char letter : str.toCharArray()) {
            Character r = replacements.get(letter);
            result.append(r != null ? r : letter);
        }
        return result.toString();
    }
}
