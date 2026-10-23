package sistema_inventario_bodega_isa;

import java.util.ArrayList;

public class Inventario {

    private final ArrayList<Producto> productos;

    public Inventario() {
        this.productos = new ArrayList<>();
    }

    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        if (buscarProducto(producto.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con ese codigo.");
        }
        this.productos.add(producto);
    }

    public Producto buscarProducto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }

        for (Producto producto : productos) {
            if (producto.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return producto;
            }
        }
        return null;
    }

    public boolean hayDisponibilidad(String codigo, double cantidad) {
        Producto producto = buscarProducto(codigo);
        return producto != null && producto.hayStockSuficiente(cantidad);
    }

    public int getCantidadProductos() {
        return productos.size();
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<>(productos);
    }

    public void mostrarInventario() {
        if (productos.isEmpty()) {
            System.out.println("El inventario esta vacio.");
            return;
        }

        for (Producto producto : productos) {
            producto.mostrarInformacion();
        }
    }
}
