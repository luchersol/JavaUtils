package io.github.luchersol.csv;

import java.io.BufferedWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class CsvManager<T> {

    private static final String DEFAULT_SEPARATOR = ",";

    // =========================
    // STATE
    // =========================
    private final String path;
    private final List<T> values;
    private final List<Column> columns;

    // =========================
    // METADATA COLUMN
    // =========================
    private record Column(
            Field field,
            String name,
            int index,
            boolean required,
            String defaultValue,
            String format
    ) {}

    // =========================
    // CONSTRUCTOR
    // =========================
    private CsvManager(String path, List<T> values, Class<?> clazz) {
        this.path = path;
        this.values = values;
        this.columns = resolveColumns(clazz);
    }

    // =========================
    // READ CSV
    // =========================
    public static <T> CsvManager<T> readCsv(
            String filePath,
            Class<T> clazz,
            String separator,
            boolean skipHeader
    ) throws Exception {

        List<T> data = Files.lines(Paths.get(filePath))
                .skip(skipHeader ? 1 : 0)
                .map(line -> parseLine(line, clazz, separator))
                .toList();

        return new CsvManager<>(filePath, data, clazz);
    }

    public static <T> CsvManager<T> readCsv(String filePath, Class<T> clazz) throws Exception {
        return readCsv(filePath, clazz, DEFAULT_SEPARATOR, true);
    }

    public static <T> CsvManager<T> readCsv(String filePath, Class<T> clazz, boolean skipHeader) throws Exception {
        return readCsv(filePath, clazz, DEFAULT_SEPARATOR, skipHeader);
    }

    // =========================
    // PARSER
    // =========================
    private static <T> T parseLine(String line, Class<T> clazz, String sep) {
        try {
            String[] parts = line.split(sep, -1);
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                CsvColumn col = field.getAnnotation(CsvColumn.class);
                if (col == null) continue;

                field.setAccessible(true);

                int idx = col.index();
                String raw;

                if (idx >= parts.length || parts[idx].isBlank()) {
                    if (!col.defaultValue().isBlank()) {
                        raw = col.defaultValue();
                    } else if (col.required()) {
                        throw new IllegalArgumentException(
                                "Missing required column index " + idx + " for field " + field.getName()
                        );
                    } else {
                        raw = "";
                    }
                } else {
                    raw = parts[idx];
                }

                field.set(instance, convert(raw, field.getType()));
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // WRITE CSV
    // =========================
    public static <T> void writeCsv(String filePath, Collection<T> data, String sep) throws Exception {
        if (data.isEmpty()) return;

        Class<?> clazz = data.iterator().next().getClass();
        List<Column> cols = resolveColumns(clazz);

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(filePath))) {
            for (T obj : data) {
                bw.write(toLine(obj, cols, sep));
                bw.newLine();
            }
        }
    }

    public static <T> void writeCsv(String filePath, Collection<T> data) throws Exception {
        writeCsv(filePath, data, DEFAULT_SEPARATOR);
    }

    // =========================
    // SERIALIZER
    // =========================
    private static <T> String toLine(T obj, List<Column> cols, String sep) {
        try {
            List<String> out = new ArrayList<>();

            for (Column c : cols) {
                Object value = c.field().get(obj);

                if (value == null) {
                    out.add("");
                    continue;
                }

                if (!c.format().isBlank()) {
                    out.add(String.format(c.format(), value));
                } else {
                    out.add(value.toString());
                }
            }

            return String.join(sep, out);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // COLUMN RESOLUTION
    // =========================
    private static List<Column> resolveColumns(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(f -> {
                    CsvColumn c = f.getAnnotation(CsvColumn.class);
                    if (c == null) return null;

                    String name = c.name().isBlank() ? f.getName() : c.name();
                    f.setAccessible(true);

                    return new Column(
                            f,
                            name,
                            c.index(),
                            c.required(),
                            c.defaultValue(),
                            c.format()
                    );
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingInt(Column::index))
                .toList();
    }

    // =========================
    // TYPE CONVERTER
    // =========================
    private static final Map<Class<?>, Function<String, ?>> CONVERTERS = Map.of(
            String.class, Function.identity(),
            int.class, Integer::parseInt,
            Integer.class, Integer::parseInt,
            long.class, Long::parseLong,
            Long.class, Long::parseLong,
            double.class, Double::parseDouble,
            Double.class, Double::parseDouble,
            boolean.class, Boolean::parseBoolean,
            Boolean.class, Boolean::parseBoolean
    );

    private static Object convert(String value, Class<?> type) {
        Function<String, ?> fn = CONVERTERS.get(type);
        return fn != null ? fn.apply(value) : value;
    }

    // =========================
    // API
    // =========================
    public CsvManager<T> map(Function<T, T> fn) {
        return new CsvManager<>(path, values.stream().map(fn).toList(), getClazz());
    }

    public CsvManager<T> filter(Predicate<T> pred) {
        return new CsvManager<>(path, values.stream().filter(pred).toList(), getClazz());
    }

    public CsvManager<T> mapIf(Function<T, T> fn, Predicate<T> pred) {
        return new CsvManager<>(path,
                values.stream().map(v -> pred.test(v) ? fn.apply(v) : v).toList(),
                getClazz());
    }

    public CsvManager<T> sort() {
        return new CsvManager<>(path,
                values.stream().sorted().toList(),
                getClazz());
    }

    public CsvManager<T> sort(Comparator<T> cmp) {
        return new CsvManager<>(path,
                values.stream().sorted(cmp).toList(),
                getClazz());
    }

    // =========================
    // ACCESS
    // =========================
    public T get(int index) {
        return values.get(index);
    }

    public int size() {
        return values.size();
    }

    // =========================
    // SHOW TABLE
    // =========================
    public void show() {
        if (values.isEmpty()) {
            System.out.println("(sin datos)");
            return;
        }

        printHeader();
        printSeparator();
        printRows();
    }

    private void printHeader() {
        for (Column c : columns) {
            System.out.printf("%-20s", c.name());
        }
        System.out.println();
    }

    private void printSeparator() {
        System.out.println("-".repeat(columns.size() * 20));
    }

    private void printRows() {
        try {
            for (T obj : values) {
                for (Column c : columns) {
                    Object v = c.field().get(obj);
                    System.out.printf("%-20s", v != null ? v : "");
                }
                System.out.println();
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // HELP
    // =========================
    @SuppressWarnings("unchecked")
    private Class<T> getClazz() {
        return (Class<T>) values.get(0).getClass();
    }
}