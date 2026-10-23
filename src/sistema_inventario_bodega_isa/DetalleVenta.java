package sistema_inventario_bodega_isa;

public class DetalleVenta {

    private Producto producto;
    private double cantidad;
    private double precioUnitario;

    public DetalleVenta(Producto producto, double cantidad) {
        this(producto, cantidad, producto == null ? 0 : producto.getPrecioVenta());
    }

    public DetalleVenta(Producto producto, double cantidad, double precioUnitario) {
        setProducto(producto);
        setCantidad(cantidad);
        setPrecioUnitario(precioUnitario);
    }

    private void setProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("Debe seleccionar un producto.");
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

    private void setPrecioUnitario(double precioUnitario) {
        if (!Double.isFinite(precioUnitario) || precioUnitario <= 0) {
            throw new IllegalArgumentException(
                    "El precio unitario debe ser mayor que cero.");
        }
        this.precioUnitario = Producto.redondearDinero(precioUnitario);
    }

    public Producto getProducto() {
        return producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double calcularSubtotal() {
        return Producto.redondearDinero(cantidad * precioUnitario);
    }
}
