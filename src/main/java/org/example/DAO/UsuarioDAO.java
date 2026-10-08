package org.example.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.Util.Conexion;

public class UsuarioDAO {
    public boolean registrar(
            String email,
            String passwordHash) {

        String sql = "INSERT INTO usuarios (email, password_hash) VALUES (?, ?)";

        try {
                Connection conn=Conexion.conectar();
                PreparedStatement sentencia = conn.prepareStatement(sql);



            sentencia.setString(1, email);
            sentencia.setString(2, passwordHash);

            sentencia.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String obtenerPasswordHash(
            String email) {

        String sql =
                "SELECT password_hash FROM usuarios WHERE email = ?";

        try {


            Connection conn=Conexion.conectar();
            PreparedStatement sentencia = conn.prepareStatement(sql);
            sentencia.setString(1, email);

            ResultSet rs = sentencia.executeQuery();

            if (rs.next()) {
                return rs.getString("password_hash");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
