package sistema_inventario_bodega_isa;

public class DetalleCompra {

    private Producto producto;
    private double cantidad;
    private double costoUnitario;

    public DetalleCompra(Producto producto, double cantidad, double costoUnitario) {
        setProducto(producto);
        setCantidad(cantidad);
        setCostoUnitario(costoUnitario);
    }

    private void setProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }
        this.producto = producto;
    }

    private void setCantidad(double cantidad) {
        if (!producto.esCantidadValida(cantidad)) {
            throw new IllegalArgumentException(
                    "La cantidad no es valida para la unidad del producto.");
        }
        this.cantidad = cantidad;
    }

    private void setCostoUnitario(double costoUnitario) {
        if (!Double.isFinite(costoUnitario) || costoUnitario <= 0) {
            throw new IllegalArgumentException(
                    "El costo unitario debe ser mayor que cero.");
        }
        this.costoUnitario = Producto.redondearDinero(costoUnitario);
    }

    public Producto getProducto() {
        return producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getCostoUnitario() {
        return costoUnitario;
    }

    public double calcularSubtotal() {
        return Producto.redondearDinero(cantidad * costoUnitario);
    }
}
