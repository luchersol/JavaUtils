package io.github.luchersol.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class GenericCsvMapper {

    private static final String SEPARATOR = ",";

    /* =========================
       LECTURA → OBJETOS
       ========================= */
    public static <T> List<T> readCsv(String filePath, Class<T> clazz) throws Exception {
        List<T> result = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(SEPARATOR, -1);
                T obj = mapToObject(values, clazz);
                result.add(obj);
            }
        }

        return result;
    }

    private static <T> T mapToObject(String[] values, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Field field : clazz.getDeclaredFields()) {
            CsvColumn column = field.getAnnotation(CsvColumn.class);
            if (column == null) continue;

            int index = column.index();
            if (index >= values.length) continue;

            field.setAccessible(true);
            Object converted = convert(values[index], field.getType());
            field.set(instance, converted);
        }

        return instance;
    }

    /* =========================
       ESCRITURA ← OBJETOS
       ========================= */
    public static <T> void writeCsv(String filePath, List<T> data) throws Exception {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath))) {

            for (T obj : data) {
                bw.write(objectToLine(obj));
                bw.newLine();
            }
        }
    }

    private static <T> String objectToLine(T obj) throws Exception {
        Class<?> clazz = obj.getClass();

        List<Field> fields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(CsvColumn.class))
                .sorted(Comparator.comparingInt(
                        f -> f.getAnnotation(CsvColumn.class).index()
                ))
                .toList();

        List<String> values = new ArrayList<>();

        for (Field f : fields) {
            f.setAccessible(true);
            Object value = f.get(obj);
            values.add(value != null ? value.toString() : "");
        }

        return String.join(SEPARATOR, values);
    }

    /* =========================
       CONVERSIÓN DE TIPOS
       ========================= */
    private static Object convert(String value, Class<?> type) {
        if (type == String.class) return value;
        if (type == int.class || type == Integer.class) return Integer.parseInt(value);
        if (type == long.class || type == Long.class) return Long.parseLong(value);
        if (type == double.class || type == Double.class) return Double.parseDouble(value);
        if (type == boolean.class || type == Boolean.class) return Boolean.parseBoolean(value);

        return value; // fallback
    }

    /* =========================
       API FUNCIONAL SOBRE OBJETOS
       ========================= */

    public static <T> Stream<T> stream(List<T> data) {
        return data.stream();
    }

    public static <T> List<T> filter(List<T> data, java.util.function.Predicate<T> predicate) {
        return data.stream().filter(predicate).toList();
    }

    public static <T> List<T> sort(List<T> data, Comparator<T> comparator) {
        return data.stream().sorted(comparator).toList();
    }
}