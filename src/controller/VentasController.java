package controller;

import model.builder.Factura;
import model.builder.FacturaBuilder;
import model.dao.ClienteDAO;
import model.dao.ProductoDAO;
import model.dao.VentaDAO;
import model.dto.ClienteDTO;
import model.dto.ProductoDTO;
import model.dto.VentaDTO;
import model.dto.VentaDetalleDTO;
import model.factory.DocumentoFactory;
import view.VentasView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class VentasController {

    private VentasView view;

    private ClienteDAO clienteDAO = new ClienteDAO();
    private ProductoDAO productoDAO = new ProductoDAO();
    private VentaDAO ventaDAO = new VentaDAO();

    private ClienteDTO clienteActual;
    private ProductoDTO productoActual;

    public VentasController(VentasView v) {
        this.view = v;
        init();
        prepararTabla();
    }

    private void init() {

        // Buscar producto al presionar ENTER en código
        view.txtCodigo.addActionListener(e -> buscarProductoPorCodigo());

        // Buscar cliente al presionar ENTER en DNI
        view.txtDniCliente.addActionListener(e -> buscarClientePorDni());

        // Si cambias menor/mayor, recalcula precio si ya hay producto cargado
        view.rbMenor.addActionListener(e -> recalcularPrecioSegunTipo());
        view.rbMayor.addActionListener(e -> recalcularPrecioSegunTipo());

        // Agregar al detalle
        view.btnAgregar.addActionListener(e -> agregarDetalle());

        // Eliminar item
        view.btnEliminarItem.addActionListener(e -> eliminarItem());

        // Actualizar item
        view.btnActualizarItem.addActionListener(e -> actualizarItem());

        // Generar comprobante
        view.btnGenerar.addActionListener(e -> generarComprobante());

    }

    private void prepararTabla() {
        DefaultTableModel m = (DefaultTableModel) view.tablaDetalle.getModel();
        m.setColumnIdentifiers(new Object[]{
            "CODIGO", "DESCRIPCION", "PRECIO", "CANT", "IMPORTE", "ID_PRODUCTO"
        });

        // ocultar ID_PRODUCTO
        view.tablaDetalle.getColumnModel().getColumn(5).setMinWidth(0);
        view.tablaDetalle.getColumnModel().getColumn(5).setMaxWidth(0);
        view.tablaDetalle.getColumnModel().getColumn(5).setWidth(0);
    }

    private void buscarProductoPorCodigo() {
        String cod = view.txtCodigo.getText().trim();
        if (cod.isEmpty()) {
            return;
        }

        ProductoDTO p = productoDAO.buscarPorCodigo(cod);
        if (p != null) {
            productoActual = p;

            view.txtDesc.setText(p.getDescripcion());
            view.txtStock.setText(String.valueOf(p.getStock()));

            double precio = view.rbMayor.isSelected() ? p.getPrecioMayor() : p.getPrecioUnit();
            view.txtPrecio.setText(String.valueOf(precio));

            view.txtCantidad.requestFocus();
        } else {
            JOptionPane.showMessageDialog(view, "Producto no encontrado");
            productoActual = null;
            limpiarCamposProducto();
        }
    }

    private void buscarClientePorDni() {
        String dni = view.txtDniCliente.getText().trim();
        if (dni.isEmpty()) {
            return;
        }

        ClienteDTO c = clienteDAO.buscarPorDni(dni);
        if (c != null) {
            clienteActual = c;
            view.txtNomCliente.setText(c.getNombre());
            view.txtDirCliente.setText(c.getDireccion());
        } else {
            JOptionPane.showMessageDialog(view, "Cliente no registrado");
            clienteActual = null;
            view.txtNomCliente.setText("");
            view.txtDirCliente.setText("");
        }
    }

    private void recalcularPrecioSegunTipo() {
        if (productoActual == null) {
            return;
        }
        double precio = view.rbMayor.isSelected()
                ? productoActual.getPrecioMayor()
                : productoActual.getPrecioUnit();
        view.txtPrecio.setText(String.valueOf(precio));
    }

    private void agregarDetalle() {
        if (productoActual == null) {
            JOptionPane.showMessageDialog(view, "Primero busca un producto");
            return;
        }

        int cant;
        try {
            cant = Integer.parseInt(view.txtCantidad.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Cantidad inválida");
            return;
        }

        if (cant <= 0) {
            JOptionPane.showMessageDialog(view, "Cantidad debe ser mayor a 0");
            return;
        }

        if (cant > productoActual.getStock()) {
            JOptionPane.showMessageDialog(view, "Stock insuficiente");
            return;
        }

        double precio = Double.parseDouble(view.txtPrecio.getText());
        double importe = cant * precio;

        DefaultTableModel m = (DefaultTableModel) view.tablaDetalle.getModel();
        m.addRow(new Object[]{
            productoActual.getCodigo(),
            productoActual.getDescripcion(),
            precio,
            cant,
            importe,
            productoActual.getId()
        });

        recalcularTotales();
        limpiarCamposProducto();
        view.txtCodigo.requestFocus();
    }

    private void eliminarItem() {
        int fila = view.tablaDetalle.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Selecciona un item");
            return;
        }
        DefaultTableModel m = (DefaultTableModel) view.tablaDetalle.getModel();
        m.removeRow(fila);
        recalcularTotales();
    }

    private void actualizarItem() {
        int fila = view.tablaDetalle.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view, "Selecciona un item");
            return;
        }

        DefaultTableModel m = (DefaultTableModel) view.tablaDetalle.getModel();

        String codigo = m.getValueAt(fila, 0).toString();
        ProductoDTO p = productoDAO.buscarPorCodigo(codigo);
        if (p == null) {
            JOptionPane.showMessageDialog(view, "Producto ya no existe");
            return;
        }

        int nuevaCant;
        try {
            nuevaCant = Integer.parseInt(JOptionPane.showInputDialog(view, "Nueva cantidad:"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Cantidad inválida");
            return;
        }

        if (nuevaCant <= 0 || nuevaCant > p.getStock()) {
            JOptionPane.showMessageDialog(view, "Cantidad inválida o supera stock");
            return;
        }

        double precio = Double.parseDouble(m.getValueAt(fila, 2).toString());
        double nuevoImporte = nuevaCant * precio;

        m.setValueAt(nuevaCant, fila, 3);
        m.setValueAt(nuevoImporte, fila, 4);

        recalcularTotales();
    }

    private void recalcularTotales() {
        DefaultTableModel m = (DefaultTableModel) view.tablaDetalle.getModel();
        double subtotal = 0;

        for (int i = 0; i < m.getRowCount(); i++) {
            Object val = m.getValueAt(i, 4); // IMPORTE
            if (val != null) {
                subtotal += Double.parseDouble(val.toString());
            }
        }

        double igv = subtotal * 0.18;
        double total = subtotal + igv;

        view.lblSubtotal.setText(String.format("S/ %.2f", subtotal));
        view.lblIgv.setText(String.format("S/ %.2f", igv));
        view.lblTotal.setText(String.format("S/ %.2f", total));
    }

    private void limpiarCamposProducto() {
        view.txtCodigo.setText("");
        view.txtDesc.setText("");
        view.txtStock.setText("");
        view.txtPrecio.setText("");
        view.txtCantidad.setText("");
        productoActual = null;
    }

    private void generarComprobante() {
        System.out.println("=== INICIANDO GENERACIÓN DE COMPROBANTE ===");
        DefaultTableModel m = (DefaultTableModel) view.tablaDetalle.getModel();

        // 1) Validaciones
        if (m.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view, "Agrega productos a la venta");
            return;
        }
        if (clienteActual == null) {
            JOptionPane.showMessageDialog(view, "Selecciona un cliente válido");
            return;
        }
        
        System.out.println("Validaciones pasadas. Cliente ID: " + clienteActual.getId());
        System.out.println("Productos en carrito: " + m.getRowCount());

        // 2) Totales desde tabla
        double subtotal = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            Object val = m.getValueAt(i, 4);
            if (val != null) {
                subtotal += Double.parseDouble(val.toString());
            }
        }
        double igv = subtotal * 0.18;
        double total = subtotal + igv;

        // 3) Flags de venta
        boolean delivery = view.chkDelivery.isSelected();
        String tipoVenta = view.rbMayor.isSelected() ? "MAYOR" : "MENOR";

        // 4) Tipo doc - Permitir seleccionar entre BOLETA y FACTURA
        String[] opciones = {"BOLETA", "FACTURA"};
        String tipoDocSeleccionado = (String) JOptionPane.showInputDialog(
            view,
            "Seleccione el tipo de documento:",
            "Tipo de Comprobante",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0] // Por defecto BOLETA
        );
        
        if (tipoDocSeleccionado == null) {
            // Usuario canceló
            return;
        }
        
        String tipoDoc = tipoDocSeleccionado;

        // 5) Construir VentaDTO
        model.dto.VentaDTO venta = new model.dto.VentaDTO(
                clienteActual.getId(),
                tipoDoc,
                tipoVenta,
                delivery,
                subtotal,
                igv,
                total
        );

        // 6) Construir detalles desde JTable
        List<model.dto.VentaDetalleDTO> detalles = new ArrayList<>();
        model.builder.FacturaBuilder builder = new model.builder.FacturaBuilder().setVenta(venta);

        for (int i = 0; i < m.getRowCount(); i++) {
            try {
                // Obtener valores de la tabla con validación de null
                Object objProductoId = m.getValueAt(i, 5);
                Object objCant = m.getValueAt(i, 3);
                Object objPrecio = m.getValueAt(i, 2);
                Object objImporte = m.getValueAt(i, 4);
                
                // Validar que ningún valor sea null
                if (objProductoId == null || objCant == null || objPrecio == null || objImporte == null) {
                    JOptionPane.showMessageDialog(view, 
                        "Error: Hay datos incompletos en la fila " + (i+1) + "\n" +
                        "Por favor, elimina la fila y vuelve a agregar el producto.",
                        "Datos Incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int productoId = Integer.parseInt(objProductoId.toString());
                int cant = Integer.parseInt(objCant.toString());
                double precio = Double.parseDouble(objPrecio.toString());
                double importe = Double.parseDouble(objImporte.toString());
                
                // Validar que los valores sean válidos
                if (productoId <= 0 || cant <= 0 || precio < 0 || importe < 0) {
                    JOptionPane.showMessageDialog(view, 
                        "Error: Valores inválidos en la fila " + (i+1),
                        "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Crear detalle sin ventaId (se asignará después en el DAO)
                model.dto.VentaDetalleDTO det
                        = new model.dto.VentaDetalleDTO(0, productoId, cant, precio, importe);

                detalles.add(det);
                builder.addDetalle(det);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, 
                    "Error: Formato de número inválido en la fila " + (i+1) + "\n" +
                    "Mensaje: " + ex.getMessage(),
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
                return;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, 
                    "Error al procesar item en fila " + (i+1) + ":\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
                return;
            }
        }

        // 7) Factura final (Builder)
        model.builder.Factura factura = builder.build();

        // 8) Guardar en BD (transacción)
        try {
            System.out.println("Intentando registrar venta en BD...");
            int ventaId = ventaDAO.registrarVenta(factura);

            if (ventaId > 0) {
                JOptionPane.showMessageDialog(view, 
                    "✅ Venta registrada exitosamente\nID de Venta: " + ventaId,
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

                // 9) Generar PDF/HTML de la factura
                try {
                    String rutaArchivo = util.PdfFacturaGeneratorMejorado.generarFacturaPDF(
                        factura, ventaId, clienteActual);
                    
                    if (rutaArchivo != null) {
                        String tipoArchivo = rutaArchivo.endsWith(".pdf") ? "PDF" : "HTML";
                        int opcion = JOptionPane.showConfirmDialog(view,
                            "✅ Venta registrada exitosamente\n" +
                            "ID de Venta: " + ventaId + "\n\n" +
                            "Factura generada en formato " + tipoArchivo + "\n" +
                            "¿Deseas abrir el archivo?",
                            "Factura Generada", 
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                        
                        if (opcion == JOptionPane.YES_OPTION) {
                            // Abrir el archivo con el programa predeterminado
                            java.awt.Desktop.getDesktop().open(new java.io.File(rutaArchivo));
                        }
                    } else {
                        JOptionPane.showMessageDialog(view,
                            "⚠️ Venta registrada pero no se pudo generar el documento.\n\n" +
                            "ID de Venta: " + ventaId,
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception pdfEx) {
                    System.err.println("Error al generar documento: " + pdfEx.getMessage());
                    pdfEx.printStackTrace();
                    JOptionPane.showMessageDialog(view,
                        "✅ Venta registrada (ID: " + ventaId + ")\n" +
                        "⚠️ Error al generar documento: " + pdfEx.getMessage(),
                        "Venta Registrada", JOptionPane.INFORMATION_MESSAGE);
                }

                // 10) Limpiar pantalla para nueva venta
                m.setRowCount(0);
                recalcularTotales();
                view.txtDniCliente.setText("");
                view.txtNomCliente.setText("");
                view.txtDirCliente.setText("");
                clienteActual = null;
            } else {
                JOptionPane.showMessageDialog(view, 
                    "❌ No se pudo registrar la venta\n" +
                    "El ID de venta retornado es 0.\n\n" +
                    "Posibles causas:\n" +
                    "- Error al obtener el ID generado\n" +
                    "- Error en la transacción\n\n" +
                    "Revisa la consola para más detalles.",
                    "Error al Registrar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (RuntimeException ex) {
            // Capturar el error específico de la BD
            String mensajeError = ex.getMessage();
            String mensajeUsuario = "❌ Error al registrar la venta:\n\n";
            
            if (mensajeError != null) {
                if (mensajeError.contains("Stock insuficiente")) {
                    mensajeUsuario += "⚠️ Stock insuficiente en uno de los productos.\n";
                    mensajeUsuario += "Verifica el stock disponible.";
                } else if (mensajeError.contains("does not exist") || mensajeError.contains("relation")) {
                    mensajeUsuario += "⚠️ Error: Tabla o columna no encontrada.\n";
                    mensajeUsuario += "Verifica que el script SQL de PostgreSQL se haya ejecutado correctamente.\n\n";
                    mensajeUsuario += "Error: " + mensajeError;
                } else if (mensajeError.contains("connection") || mensajeError.contains("Connection")) {
                    mensajeUsuario += "⚠️ Error de conexión a la base de datos.\n";
                    mensajeUsuario += "Verifica que PostgreSQL esté corriendo.\n\n";
                    mensajeUsuario += "Error: " + mensajeError;
                } else {
                    mensajeUsuario += mensajeError;
                }
            } else {
                mensajeUsuario += "Error desconocido. Revisa la consola para más detalles.";
            }
            
            JOptionPane.showMessageDialog(view, 
                mensajeUsuario + "\n\n" +
                "Verifica:\n" +
                "1. Que PostgreSQL esté corriendo\n" +
                "2. Que la base de datos 'facturacion_tienda' exista\n" +
                "3. Que las tablas estén creadas correctamente\n" +
                "4. Revisa la consola de NetBeans para más detalles",
                "Error Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, 
                "❌ Error inesperado al registrar la venta:\n" + ex.getMessage() + "\n\n" +
                "Verifica:\n" +
                "1. Que PostgreSQL esté corriendo\n" +
                "2. Que la base de datos exista\n" +
                "3. Que las tablas estén creadas correctamente\n" +
                "4. Revisa la consola para más detalles",
                "Error Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

}
