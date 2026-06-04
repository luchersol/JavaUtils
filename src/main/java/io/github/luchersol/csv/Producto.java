package io.github.luchersol.csv;

public class Producto {
    @CsvColumn(index = 0)
    public int id;

    @CsvColumn(index = 1)
    public String nombre;

    @CsvColumn(index = 2)
    public double precio;

    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + "]";
    }

    
}
