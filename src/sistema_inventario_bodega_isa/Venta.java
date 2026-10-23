package sistema_inventario_bodega_isa;

import java.time.LocalDate;
import java.util.ArrayList;

public class Venta {

    public static final String CONTADO = "CONTADO";
    public static final String FIADO = "FIADO";

    private int numero;
    private LocalDate fecha;
    private String modalidad;
    private Cliente cliente;
    private final ArrayList<DetalleVenta> detalles;
    private boolean confirmada;

    public Venta(int numero, String modalidad, Cliente cliente) {
        this(numero, LocalDate.now(), modalidad, cliente);
    }

    public Venta(int numero, LocalDate fecha, String modalidad, Cliente cliente) {
        setNumero(numero);
        setFecha(fecha);
        setModalidadYCliente(modalidad, cliente);
        this.detalles = new ArrayList<>();
        this.confirmada = false;
    }

    private void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException(
                    "El numero de venta debe ser mayor que cero.");
        }
        this.numero = numero;
    }

    private void setFecha(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        this.fecha = fecha;
    }

    private void setModalidadYCliente(String modalidad, Cliente cliente) {
        if (modalidad == null) {
            throw new IllegalArgumentException("La modalidad es obligatoria.");
        }

        String valor = modalidad.trim().toUpperCase();
        if (!valor.equals(CONTADO) && !valor.equals(FIADO)) {
            throw new IllegalArgumentException(
                    "La modalidad debe ser CONTADO o FIADO.");
        }
        if (valor.equals(FIADO) && cliente == null) {
            throw new IllegalArgumentException(
                    "Una venta al fiado requiere un cliente.");
        }

        this.modalidad = valor;
        this.cliente = cliente;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        if (confirmada) {
            throw new IllegalStateException("La venta ya fue confirmada.");
        }
        if (detalle == null) {
            throw new IllegalArgumentException("El detalle no puede ser nulo.");
        }
        if (contieneProducto(detalle.getProducto().getCodigo())) {
            throw new IllegalArgumentException(
                    "El producto ya fue agregado a la venta.");
        }
        detalles.add(detalle);
    }

    private boolean contieneProducto(String codigoProducto) {
        for (DetalleVenta detalle : detalles) {
            if (detalle.getProducto().getCodigo().equalsIgnoreCase(codigoProducto)) {
                return true;
            }
        }
        return false;
    }

    public double calcularTotal() {
        double total = 0;
        for (DetalleVenta detalle : detalles) {
            total += detalle.calcularSubtotal();
        }
        return Producto.redondearDinero(total);
    }

    public void confirmarVenta() {
        if (confirmada) {
            throw new IllegalStateException("La venta ya fue confirmada.");
        }
        if (detalles.isEmpty()) {
            throw new IllegalStateException(
                    "La venta debe contener al menos un detalle.");
        }

        for (DetalleVenta detalle : detalles) {
            if (!detalle.getProducto().hayStockSuficiente(detalle.getCantidad())) {
                throw new IllegalStateException(
                        "Stock insuficiente para "
                        + detalle.getProducto().getNombre() + ".");
            }
        }

        for (DetalleVenta detalle : detalles) {
            detalle.getProducto().disminuirStock(detalle.getCantidad());
        }
        this.confirmada = true;
    }

    public boolean esFiado() {
        return FIADO.equals(modalidad);
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getModalidad() {
        return modalidad;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ArrayList<DetalleVenta> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public boolean isConfirmada() {
        return confirmada;
    }
}
