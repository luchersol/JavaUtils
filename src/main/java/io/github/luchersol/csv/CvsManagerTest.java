package io.github.luchersol.csv;

public class CvsManagerTest {
    
    public static void main(String[] args) throws Exception {
        CsvManager<Producto> productos = CsvManager.readCsv("src/main/java/io/github/luchersol/csv/ejemplo.csv", Producto.class);
    }
}
