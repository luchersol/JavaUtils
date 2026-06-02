package io.github.luchersol.csv;

public class Producto {
    @CsvColumn(index = 0)
    private int id;

    @CsvColumn(index = 1)
    private String nombre;

    @CsvColumn(index = 2)
    private double precio;

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + "]";
    }

    
}
