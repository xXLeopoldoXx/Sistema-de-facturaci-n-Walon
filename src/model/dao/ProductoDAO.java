package model.dao;

import model.dto.ProductoDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO implements IProductoDAO {

    @Override
    public boolean insertar(ProductoDTO p) {
        String sql = "INSERT INTO productos(codigo,descripcion,stock,precio_unit,precio_mayor,proveedor_id) " +
                     "VALUES(?,?,?,?,?,?)";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStock());
            ps.setDouble(4, p.getPrecioUnit());
            ps.setDouble(5, p.getPrecioMayor());
            ps.setInt(6, p.getProveedorId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(ProductoDTO p) {
        String sql = "UPDATE productos SET codigo=?, descripcion=?, stock=?, precio_unit=?, precio_mayor=?, proveedor_id=? " +
                     "WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getDescripcion());
            ps.setInt(3, p.getStock());
            ps.setDouble(4, p.getPrecioUnit());
            ps.setDouble(5, p.getPrecioMayor());
            ps.setInt(6, p.getProveedorId());
            ps.setInt(7, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ProductoDTO> listar() {
        List<ProductoDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY id DESC";

        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductoDTO p = new ProductoDTO(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getInt("stock"),
                        rs.getDouble("precio_unit"),
                        rs.getDouble("precio_mayor"),
                        rs.getInt("proveedor_id")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public ProductoDTO buscarPorId(int id) {
        String sql = "SELECT * FROM productos WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductoDTO(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getInt("stock"),
                            rs.getDouble("precio_unit"),
                            rs.getDouble("precio_mayor"),
                            rs.getInt("proveedor_id")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // MÉTODO CLAVE PARA VENTAS: buscar por código
    public ProductoDTO buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProductoDTO(
                            rs.getInt("id"),
                            rs.getString("codigo"),
                            rs.getString("descripcion"),
                            rs.getInt("stock"),
                            rs.getDouble("precio_unit"),
                            rs.getDouble("precio_mayor"),
                            rs.getInt("proveedor_id")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // EXTRA ÚTIL: actualizar stock (para el VentaDAO)
    public boolean actualizarStock(int productoId, int nuevoStock) {
        String sql = "UPDATE productos SET stock=? WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, nuevoStock);
            ps.setInt(2, productoId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

