package gm;

public class Producto {
    private String identificador;
    private String descripcion;
    private int precio;

    public Producto(String identificador, String descripcion, int precio) {
        this.identificador = identificador;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Producto() {

    }

    public String getIdentificador() {
        return identificador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "Producto{" + "identificador='" + identificador + '\'' + ", descripcion='" + descripcion + '\'' + ", precio=" + precio + '}';
    }
}
