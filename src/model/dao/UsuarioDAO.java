package model.dao;

import model.dto.UsuarioDTO;
import util.DBConnection;

import java.sql.*;

public class UsuarioDAO {

    // LOGIN SIMPLE (usuario y clave en tabla usuarios)
    public UsuarioDTO login(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username=? AND password=?";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UsuarioDTO(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("rol")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // (Opcional) Insertar usuario
    public boolean insertar(UsuarioDTO u) {
        String sql = "INSERT INTO usuarios(username,password,rol) VALUES(?,?,?)";
        try (Connection cn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword()); // luego puedes hashear
            ps.setString(3, u.getRol());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

