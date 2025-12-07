package model.dao;

import model.builder.Factura;
import model.dto.VentaDTO;
import model.dto.VentaDetalleDTO;
import util.DBConnection;

import java.sql.*;
import java.util.List;

public class VentaDAO implements IVentaDAO {

    // Registra venta + detalle + descuenta stock EN UNA TRANSACCIÓN
    public int registrarVenta(Factura factura) {
        VentaDTO v = factura.getVenta();
        List<VentaDetalleDTO> dets = factura.getDetalles();

        // PostgreSQL requiere cast explícito para tipos ENUM
        String sqlVenta = "INSERT INTO ventas(cliente_id,tipo_doc,tipo_venta,delivery,subtotal,igv,total) " +
                          "VALUES(?,?::tipo_documento,?::tipo_venta,?,?,?,?)";
        String sqlDet = "INSERT INTO venta_detalle(venta_id,producto_id,cantidad,precio,importe) " +
                        "VALUES(?,?,?,?,?)";
        String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id=? AND stock >= ?";

        Connection cn = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDet = null;
        PreparedStatement psStock = null;
        ResultSet rsKeys = null;

        try {
            cn = DBConnection.getInstance().getConnection();
            cn.setAutoCommit(false); // ---- INICIO TRANSACCIÓN ----

            // 1) Cabecera venta
            psVenta = cn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setInt(1, v.getClienteId());
            psVenta.setString(2, v.getTipoDoc());
            psVenta.setString(3, v.getTipoVenta());
            psVenta.setBoolean(4, v.isDelivery());
            psVenta.setDouble(5, v.getSubtotal());
            psVenta.setDouble(6, v.getIgv());
            psVenta.setDouble(7, v.getTotal());
            psVenta.executeUpdate();

            rsKeys = psVenta.getGeneratedKeys();
            int ventaId = 0;
            if (rsKeys.next()) {
                ventaId = rsKeys.getInt(1);
                System.out.println("Venta ID generado: " + ventaId);
            } else {
                throw new SQLException("No se pudo obtener el ID de la venta generada");
            }

            // 2) Detalle + descuento stock
            psDet = cn.prepareStatement(sqlDet);
            psStock = cn.prepareStatement(sqlStock);

            for (VentaDetalleDTO d : dets) {
                // detalle
                psDet.setInt(1, ventaId);
                psDet.setInt(2, d.getProductoId());
                psDet.setInt(3, d.getCantidad());
                psDet.setDouble(4, d.getPrecio());
                psDet.setDouble(5, d.getImporte());
                psDet.addBatch();

                // stock
                psStock.setInt(1, d.getCantidad());
                psStock.setInt(2, d.getProductoId());
                psStock.setInt(3, d.getCantidad());
                psStock.addBatch();
            }

            psDet.executeBatch();
            int[] stockResults = psStock.executeBatch();

            // validar stockResults (si algún update no afectó filas => stock insuficiente)
            for (int r : stockResults) {
                if (r == 0) {
                    cn.rollback();
                    throw new SQLException("Stock insuficiente en uno de los productos.");
                }
            }

            cn.commit(); // ---- FIN TRANSACCIÓN OK ----
            return ventaId;

        } catch (SQLException e) {
            try { 
                if (cn != null) {
                    cn.rollback();
                    System.err.println("Transacción revertida debido a error");
                }
            } catch (SQLException ex) { 
                System.err.println("Error al hacer rollback: " + ex.getMessage());
                ex.printStackTrace(); 
            }
            System.err.println("Error SQL al registrar venta:");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Estado SQL: " + e.getSQLState());
            System.err.println("Código de error: " + e.getErrorCode());
            
            // Lanzar excepción con mensaje detallado para que el controlador lo capture
            SQLException sqlEx = new SQLException("Error al registrar venta en BD: " + e.getMessage() + 
                (e.getSQLState() != null ? " (SQL State: " + e.getSQLState() + ")" : ""), e.getSQLState(), e);
            throw new RuntimeException(sqlEx.getMessage(), sqlEx);
        } catch (RuntimeException e) {
            // Re-lanzar RuntimeException para que el controlador la capture
            throw e;
        } catch (Exception e) {
            try { 
                if (cn != null) cn.rollback(); 
            } catch (SQLException ex) { 
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error inesperado al registrar venta:");
            e.printStackTrace();
            throw new RuntimeException("Error inesperado al registrar venta: " + e.getMessage(), e);
        } finally {
            try { if (rsKeys != null) rsKeys.close(); } catch (Exception ignored) {}
            try { if (psVenta != null) psVenta.close(); } catch (Exception ignored) {}
            try { if (psDet != null) psDet.close(); } catch (Exception ignored) {}
            try { if (psStock != null) psStock.close(); } catch (Exception ignored) {}
            try { if (cn != null) cn.close(); } catch (Exception ignored) {}
        }
    }
}

