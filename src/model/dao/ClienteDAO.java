package model.dao;

import model.dto.ClienteDTO;
import util.DBConnection;
import java.sql.*;
import java.util.*;

public class ClienteDAO implements IClienteDAO {

    @Override
    public boolean insertar(ClienteDTO c) {
        String sql = "INSERT INTO clientes(dni,nombre,direccion,telefono) VALUES(?,?,?,?)";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDireccion());
            ps.setString(4, c.getTelefono());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean actualizar(ClienteDTO c) {
        String sql = "UPDATE clientes SET dni=?,nombre=?,direccion=?,telefono=? WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getDireccion());
            ps.setString(4, c.getTelefono());
            ps.setInt(5, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public List<ClienteDTO> listar() {
        List<ClienteDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while(rs.next()){
                lista.add(new ClienteDTO(
                    rs.getInt("id"),
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("direccion"),
                    rs.getString("telefono")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public ClienteDTO buscarPorId(int id) { /* similar */ return null; }

    public ClienteDTO buscarPorDni(String dni){
        String sql="SELECT * FROM clientes WHERE dni=?";
        try (Connection cn=DBConnection.getInstance().getConnection();
             PreparedStatement ps=cn.prepareStatement(sql)){
            ps.setString(1, dni);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return new ClienteDTO(rs.getInt("id"), dni,
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"));
            }
        }catch(SQLException e){e.printStackTrace();}
        return null;
    }
}
