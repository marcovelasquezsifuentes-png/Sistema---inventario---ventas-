package sistema_inventario_bodega_isa;

import java.time.LocalDate;
import java.util.ArrayList;

public class PagoDeuda {

    private final String codigoPago;
    private final LocalDate fecha;
    private final Cliente cliente;
    private final double importe;

    public PagoDeuda(String codigoPago, Cliente cliente, double importe) {
        this(codigoPago, LocalDate.now(), cliente, importe);
    }

    public PagoDeuda(String codigoPago, LocalDate fecha,
                     Cliente cliente, double importe) {
        if (codigoPago == null || codigoPago.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El codigo del pago no puede estar vacio.");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Debe indicar un cliente.");
        }
        if (!Double.isFinite(importe) || importe <= 0) {
            throw new IllegalArgumentException(
                    "El importe debe ser mayor que cero.");
        }

        this.codigoPago = codigoPago.trim().toUpperCase();
        this.fecha = fecha;
        this.cliente = cliente;
        this.importe = Producto.redondearDinero(importe);
    }

    public boolean puedeAplicarse(ArrayList<Venta> ventas,
                                  ArrayList<PagoDeuda> pagos) {
        double saldoPendiente = cliente.calcularSaldo(ventas, pagos);
        return saldoPendiente > 0 && importe <= saldoPendiente + 0.001;
    }

    public String getCodigoPago() {
        return codigoPago;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public double getImporte() {
        return importe;
    }
}
