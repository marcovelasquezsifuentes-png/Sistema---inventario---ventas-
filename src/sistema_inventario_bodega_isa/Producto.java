package sistema_inventario_bodega_isa;

public class Producto {

    public static final String UNIDAD = "UNIDAD";
    public static final String KILOGRAMO = "KILOGRAMO";

    private String codigo;
    private String nombre;
    private double precioCompra;
    private double precioVenta;
    private double stock;
    private String unidadMedida;

    public Producto(String codigo, String nombre, double precioCompra,
                    double precioVenta, String unidadMedida) {
        this(codigo, nombre, precioCompra, precioVenta, 0.0, unidadMedida);
    }

    public Producto(String codigo, String nombre, double precioCompra,
                    double precioVenta, double stock, String unidadMedida) {
        setCodigo(codigo);
        setNombre(nombre);
        setPrecioCompra(precioCompra);
        setPrecioVenta(precioVenta);
        setUnidadMedida(unidadMedida);
        setStockInicial(stock);
    }

    private void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede estar vacio.");
        }
        this.codigo = codigo.trim().toUpperCase();
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    public void setPrecioCompra(double precioCompra) {
        validarNumeroPositivo(precioCompra, "El precio de compra");
        this.precioCompra = redondearDinero(precioCompra);
    }

    public void setPrecioVenta(double precioVenta) {
        validarNumeroPositivo(precioVenta, "El precio de venta");
        this.precioVenta = redondearDinero(precioVenta);
    }

    private void setUnidadMedida(String unidadMedida) {
        if (unidadMedida == null) {
            throw new IllegalArgumentException("La unidad de medida es obligatoria.");
        }

        String unidad = unidadMedida.trim().toUpperCase();
        if (!unidad.equals(UNIDAD) && !unidad.equals(KILOGRAMO)) {
            throw new IllegalArgumentException(
                    "La unidad de medida debe ser UNIDAD o KILOGRAMO.");
        }
        this.unidadMedida = unidad;
    }

    private void setStockInicial(double stock) {
        if (!Double.isFinite(stock) || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        validarCantidadSegunUnidad(stock, true);
        this.stock = stock;
    }

    public void aumentarStock(double cantidad) {
        validarCantidadSegunUnidad(cantidad, false);
        this.stock += cantidad;
    }

    public void disminuirStock(double cantidad) {
        validarCantidadSegunUnidad(cantidad, false);
        if (cantidad > this.stock) {
            throw new IllegalArgumentException("No existe stock suficiente.");
        }
        this.stock -= cantidad;
    }

    public boolean hayStockSuficiente(double cantidad) {
        try {
            validarCantidadSegunUnidad(cantidad, false);
            return cantidad <= this.stock;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean esCantidadValida(double cantidad) {
        try {
            validarCantidadSegunUnidad(cantidad, false);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private void validarCantidadSegunUnidad(double cantidad, boolean permiteCero) {
        if (!Double.isFinite(cantidad)
                || (permiteCero ? cantidad < 0 : cantidad <= 0)) {
            throw new IllegalArgumentException("La cantidad ingresada no es valida.");
        }

        if (UNIDAD.equals(this.unidadMedida)
                && cantidad != Math.rint(cantidad)) {
            throw new IllegalArgumentException(
                    "Los productos por unidad requieren cantidades enteras.");
        }
    }

    private static void validarNumeroPositivo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException(campo + " debe ser mayor que cero.");
        }
    }

    public static double redondearDinero(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public double getStock() {
        return stock;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void mostrarInformacion() {
        System.out.printf("%s - %s | Stock: %.2f %s | Venta: S/ %.2f%n",
                codigo, nombre, stock, unidadMedida, precioVenta);
    }
}
