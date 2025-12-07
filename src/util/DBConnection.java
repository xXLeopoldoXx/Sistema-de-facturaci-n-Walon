package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de conexión a PostgreSQL usando patrón Singleton
 * Configuración: PostgreSQL en localhost, puerto 5432
 */
public class DBConnection {
    private static DBConnection instance;
    // URL de conexión PostgreSQL: jdbc:postgresql://host:puerto/base_datos
    private final String url = "jdbc:postgresql://localhost:5432/facturacion_tienda";
    private final String user = "postgres";
    private final String pass = "30334006";

    private DBConnection() {
        // Cargar el driver de PostgreSQL
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de PostgreSQL");
            System.err.println("Descarga: https://jdbc.postgresql.org/download/");
            e.printStackTrace();
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}

