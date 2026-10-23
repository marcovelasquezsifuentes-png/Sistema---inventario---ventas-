package sistema_inventario_bodega_isa;

import java.time.LocalDate;
import java.util.ArrayList;

public class CompraMercaderia {

    private int numero;
    private LocalDate fecha;
    private final ArrayList<DetalleCompra> detalles;
    private boolean confirmada;

    public CompraMercaderia(int numero) {
        this(numero, LocalDate.now());
    }

    public CompraMercaderia(int numero, LocalDate fecha) {
        setNumero(numero);
        setFecha(fecha);
        this.detalles = new ArrayList<>();
        this.confirmada = false;
    }

    private void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException(
                    "El numero de compra debe ser mayor que cero.");
        }
        this.numero = numero;
    }

    private void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        this.fecha = fecha;
    }

    public void agregarDetalle(DetalleCompra detalle) {
        if (confirmada) {
            throw new IllegalStateException("La compra ya fue confirmada.");
        }
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle no puede ser nulo.");
        }
        if (contieneProducto(detalle.getProducto().getCodigo())) {
            throw new IllegalArgumentException(
                    "El producto ya fue agregado a la compra.");
        }
        detalles.add(detalle);
    }

    private boolean contieneProducto(String codigoProducto) {
        for (DetalleCompra detalle : detalles) {
            if (detalle.getProducto().getCodigo().equalsIgnoreCase(codigoProducto)) {
                return true;
            }
        }
        return false;
    }

    public double calcularTotal() {
        double total = 0;
        for (DetalleCompra detalle : detalles) {
            total += detalle.calcularSubtotal();
        }
        return Producto.redondearDinero(total);
    }

    public void confirmarCompra() {
        if (confirmada) {
            throw new IllegalStateException("La compra ya fue confirmada.");
        }
        if (detalles.isEmpty()) {
            throw new IllegalStateException(
                    "La compra debe contener al menos un detalle.");
        }

        for (DetalleCompra detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.aumentarStock(detalle.getCantidad());
            producto.setPrecioCompra(detalle.getCostoUnitario());
        }
        this.confirmada = true;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public ArrayList<DetalleCompra> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public boolean isConfirmada() {
        return confirmada;
    }
}
