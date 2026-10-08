package org.example.Util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Properties;

public class Conexion  {

    static Connection conexion;
    public static Connection conectar() throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();

        properties.load(new FileInputStream(new File("src/main/resources/configuration/database.properties")));
        //configuration.load(R.getProperties("database.properties"));
        String host = properties.getProperty("host");
        String port = properties.getProperty("port");
        String name = properties.getProperty("name");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC",
                username, password);
        //System.out.println("Conectado a la BD");
        return conexion;
    }

    public void desconectar() throws SQLException {
        conexion.close();
    }
}
