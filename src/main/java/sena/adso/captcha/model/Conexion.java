package sena.adso.captcha.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static Connection getConnection() throws SQLException {
        try {
            // 1. Intentar obtener la URL completa de Render (la más confiable)
            String dbUrl = System.getenv("DB_URL"); 
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASS");

            // 2. Si no hay variables (entorno local), usar valores por defecto
            if (dbUrl == null) {
                dbUrl = "jdbc:postgresql://localhost:5432/vacunacion_db";
                user = "postgres";
                pass = "123";
            } else {
                // IMPORTANTE: Render requiere SSL para conexiones externas
                if (!dbUrl.contains("sslmode")) {
                    dbUrl += (dbUrl.contains("?") ? "&" : "?") + "sslmode=require";
                }
            }

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl, user, pass);
            
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Error al cargar el driver de PostgreSQL", ex);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar la conexión: " + ex.getMessage());
        }
    }
}
