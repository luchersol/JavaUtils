package io.github.luchersol.csv;

import java.util.Comparator;

public class CvsManagerTest {
    
    public static void main(String[] args) throws Exception {
        CsvManagerConfig<Producto> csvManagerConfig = CsvManagerConfig.<Producto>builder()
            .path("src/main/java/io/github/luchersol/csv/ejemplo.csv")
            .clazz(Producto.class)
            .build();

        CsvManager<Producto> productos = CsvManager.readCsv(csvManagerConfig);
        productos.filter(p -> p.precio > 2)
                 .sort(Comparator.comparing(p -> p.precio))
                 .writeCsv("src/main/java/io/github/luchersol/csv/new_ejemplo.csv");
    }
}
