package sistema_inventario_bodega_isa;

import java.util.ArrayList;
import java.util.Scanner;

public class Sistema_Inventario_Bodega_ISA {

    private static final Scanner teclado = new Scanner(System.in);

    private static final Inventario inventario = new Inventario();
    private static final ArrayList<CompraMercaderia> compras = new ArrayList<>();
    private static final ArrayList<Venta> ventas = new ArrayList<>();
    private static final ArrayList<Cliente> clientes = new ArrayList<>();
    private static final ArrayList<PagoDeuda> pagos = new ArrayList<>();

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");

            try {
                switch (opcion) {
                    case 1:
                        registrarProducto();
                        break;
                    case 2:
                        registrarCompra();
                        break;
                    case 3:
                        registrarCliente();
                        break;
                    case 4:
                        registrarVenta();
                        break;
                    case 5:
                        registrarPago();
                        break;
                    case 6:
                        mostrarResumen();
                        break;
                    case 0:
                        System.out.println("Programa finalizado.");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private static void mostrarMenu() {
        System.out.println("\nBODEGA ISA");
        System.out.println("1. Registrar producto");
        System.out.println("2. Registrar compra de mercaderia");
        System.out.println("3. Registrar cliente");
        System.out.println("4. Registrar venta");
        System.out.println("5. Registrar pago de deuda");
        System.out.println("6. Mostrar resumen");
        System.out.println("0. Salir");
    }

    private static void registrarProducto() {
        System.out.println("\nREGISTRAR PRODUCTO");
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");
        double precioCompra = leerDouble("Precio de compra: S/ ");
        double precioVenta = leerDouble("Precio de venta: S/ ");
        double stock = leerDouble("Stock inicial: ");

        System.out.println("1. Unidad");
        System.out.println("2. Kilogramo");
        int tipo = leerEntero("Tipo de medida: ");
        String unidad;

        if (tipo == 1) {
            unidad = Producto.UNIDAD;
        } else if (tipo == 2) {
            unidad = Producto.KILOGRAMO;
        } else {
            throw new IllegalArgumentException("Tipo de medida no valido.");
        }

        Producto producto = new Producto(codigo, nombre, precioCompra,
                precioVenta, stock, unidad);
        inventario.registrarProducto(producto);

        System.out.println("Producto registrado correctamente.");
    }

    private static void registrarCompra() {
        System.out.println("\nREGISTRAR COMPRA");
        Producto producto = buscarProductoSolicitado();
        double cantidad = leerDouble("Cantidad comprada: ");
        double costo = leerDouble("Costo unitario: S/ ");

        DetalleCompra detalle = new DetalleCompra(producto, cantidad, costo);
        CompraMercaderia compra = new CompraMercaderia(compras.size() + 1);
        compra.agregarDetalle(detalle);

        System.out.printf("Total de la compra: S/ %.2f%n", compra.calcularTotal());

        if (confirmar("Confirmar compra (S/N): ")) {
            compra.confirmarCompra();
            compras.add(compra);
            System.out.println("Compra registrada y stock actualizado.");
        } else {
            System.out.println("Compra cancelada.");
        }
    }

    private static void registrarCliente() {
        System.out.println("\nREGISTRAR CLIENTE");
        String codigo = leerTexto("Codigo: ");
        String nombre = leerTexto("Nombre: ");

        if (buscarCliente(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con ese codigo.");
        }

        Cliente cliente = new Cliente(codigo, nombre);
        clientes.add(cliente);

        System.out.println("Cliente registrado correctamente.");
    }

    private static void registrarVenta() {
        System.out.println("\nREGISTRAR VENTA");
        Producto producto = buscarProductoSolicitado();
        double cantidad = leerDouble("Cantidad vendida: ");

        System.out.println("1. Contado");
        System.out.println("2. Fiado");
        int tipo = leerEntero("Tipo de venta: ");

        String modalidad;
        Cliente cliente = null;

        if (tipo == 1) {
            modalidad = Venta.CONTADO;
        } else if (tipo == 2) {
            modalidad = Venta.FIADO;
            cliente = buscarClienteSolicitado();
        } else {
            throw new IllegalArgumentException("Tipo de venta no valido.");
        }

        DetalleVenta detalle = new DetalleVenta(producto, cantidad);
        Venta venta = new Venta(ventas.size() + 1, modalidad, cliente);
        venta.agregarDetalle(detalle);

        System.out.printf("Total de la venta: S/ %.2f%n", venta.calcularTotal());

        if (confirmar("Confirmar venta (S/N): ")) {
            venta.confirmarVenta();
            ventas.add(venta);
            System.out.println("Venta registrada y stock actualizado.");

            if (venta.esFiado()) {
                System.out.printf("Deuda actual de %s: S/ %.2f%n",
                        cliente.getNombre(), cliente.calcularSaldo(ventas, pagos));
            }
        } else {
            System.out.println("Venta cancelada.");
        }
    }

    private static void registrarPago() {
        System.out.println("\nREGISTRAR PAGO DE DEUDA");
        Cliente cliente = buscarClienteSolicitado();
        double saldo = cliente.calcularSaldo(ventas, pagos);

        System.out.printf("Deuda pendiente: S/ %.2f%n", saldo);

        if (saldo <= 0) {
            System.out.println("El cliente no tiene deuda pendiente.");
            return;
        }

        double importe = leerDouble("Importe del pago: S/ ");
        String codigoPago = "P" + (pagos.size() + 1);
        PagoDeuda pago = new PagoDeuda(codigoPago, cliente, importe);

        if (pago.puedeAplicarse(ventas, pagos)) {
            pagos.add(pago);
            System.out.println("Pago registrado correctamente.");
            System.out.printf("Nuevo saldo: S/ %.2f%n",
                    cliente.calcularSaldo(ventas, pagos));
        } else {
            System.out.println("El pago no puede ser mayor que la deuda.");
        }
    }

    private static void mostrarResumen() {
        System.out.println("\nRESUMEN GENERAL");
        inventario.mostrarInventario();

        System.out.println("\nClientes y deudas:");
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
        } else {
            for (Cliente cliente : clientes) {
                System.out.printf("%s - %s | Deuda: S/ %.2f%n",
                        cliente.getCodigo(), cliente.getNombre(),
                        cliente.calcularSaldo(ventas, pagos));
            }
        }

        System.out.println("\nCompras registradas: " + compras.size());
        System.out.println("Ventas registradas: " + ventas.size());
        System.out.println("Pagos registrados: " + pagos.size());
    }

    private static Producto buscarProductoSolicitado() {
        if (inventario.getCantidadProductos() == 0) {
            throw new IllegalStateException("Primero debe registrar un producto.");
        }

        inventario.mostrarInventario();
        String codigo = leerTexto("Codigo del producto: ");
        Producto producto = inventario.buscarProducto(codigo);

        if (producto == null) {
            throw new IllegalArgumentException("Producto no encontrado.");
        }
        return producto;
    }

    private static Cliente buscarClienteSolicitado() {
        if (clientes.isEmpty()) {
            throw new IllegalStateException("Primero debe registrar un cliente.");
        }

        String codigo = leerTexto("Codigo del cliente: ");
        Cliente cliente = buscarCliente(codigo);

        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado.");
        }
        return cliente;
    }

    private static Cliente buscarCliente(String codigo) {
        for (Cliente cliente : clientes) {
            if (cliente.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return cliente;
            }
        }
        return null;
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }

    private static boolean confirmar(String mensaje) {
        return leerTexto(mensaje).equalsIgnoreCase("S");
    }
}
