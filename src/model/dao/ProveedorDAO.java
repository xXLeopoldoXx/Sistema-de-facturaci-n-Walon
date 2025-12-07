package model.dao;

import model.dto.ProveedorDTO;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO implements CrudDAO<ProveedorDTO> {

    @Override
    public boolean insertar(ProveedorDTO p) {
        String sql = "INSERT INTO proveedores(ruc,nombre,telefono,direccion,razon_social) VALUES(?,?,?,?,?)";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getRuc());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getRazonSocial());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(ProveedorDTO p) {
        String sql = "UPDATE proveedores SET ruc=?, nombre=?, telefono=?, direccion=?, razon_social=? WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getRuc());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getTelefono());
            ps.setString(4, p.getDireccion());
            ps.setString(5, p.getRazonSocial());
            ps.setInt(6, p.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id=?";
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
    public List<ProveedorDTO> listar() {
        List<ProveedorDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores ORDER BY id DESC";

        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProveedorDTO p = new ProveedorDTO(
                        rs.getInt("id"),
                        rs.getString("ruc"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getString("razon_social")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public ProveedorDTO buscarPorId(int id) {
        String sql = "SELECT * FROM proveedores WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProveedorDTO(
                            rs.getInt("id"),
                            rs.getString("ruc"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getString("razon_social")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // EXTRA ÚTIL: Buscar por RUC (para autocompletar si quieres)
    public ProveedorDTO buscarPorRuc(String ruc) {
        String sql = "SELECT * FROM proveedores WHERE ruc=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, ruc);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ProveedorDTO(
                            rs.getInt("id"),
                            rs.getString("ruc"),
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            rs.getString("direccion"),
                            rs.getString("razon_social")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
