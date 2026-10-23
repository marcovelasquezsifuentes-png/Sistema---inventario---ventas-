package sistema_inventario_bodega_isa;

import java.util.ArrayList;

public class Cliente {

    private String codigo;
    private String nombre;
    private double saldoInicial;

    public Cliente(String codigo, String nombre) {
        this(codigo, nombre, 0.0);
    }

    public Cliente(String codigo, String nombre, double saldoInicial) {
        setCodigo(codigo);
        setNombre(nombre);
        setSaldoInicial(saldoInicial);
    }

    private void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El codigo del cliente no puede estar vacio.");
        }
        this.codigo = codigo.trim().toUpperCase();
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del cliente no puede estar vacio.");
        }
        this.nombre = nombre.trim();
    }

    private void setSaldoInicial(double saldoInicial) {
        if (!Double.isFinite(saldoInicial) || saldoInicial < 0) {
            throw new IllegalArgumentException(
                    "El saldo inicial debe ser un numero no negativo.");
        }
        this.saldoInicial = Producto.redondearDinero(saldoInicial);
    }

    public double calcularSaldo(ArrayList<Venta> ventas,
                                ArrayList<PagoDeuda> pagos) {
        if (ventas == null || pagos == null) {
            throw new IllegalArgumentException(
                    "Las listas de ventas y pagos no pueden ser nulas.");
        }

        double saldo = saldoInicial;

        for (Venta venta : ventas) {
            if (venta == null) {
                throw new IllegalArgumentException(
                        "La lista de ventas contiene un elemento nulo.");
            }

            if (venta.isConfirmada() && venta.esFiado()
                    && venta.getCliente() != null
                    && codigo.equals(venta.getCliente().getCodigo())) {
                saldo += venta.calcularTotal();
            }
        }

        for (PagoDeuda pago : pagos) {
            if (pago == null) {
                throw new IllegalArgumentException(
                        "La lista de pagos contiene un elemento nulo.");
            }

            if (codigo.equals(pago.getCliente().getCodigo())) {
                saldo -= pago.getImporte();
            }
        }

        return Producto.redondearDinero(saldo);
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSaldoInicial() {
        return saldoInicial;
    }
}
