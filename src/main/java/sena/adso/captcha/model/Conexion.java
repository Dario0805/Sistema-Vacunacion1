package sena.adso.captcha.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static Connection getConnection() throws SQLException {
        // Render suele usar DATABASE_URL por defecto
        String dbUrl = System.getenv("DB_URL"); 
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASSWORD");

        // Si dbUrl es nulo, intentamos con DATABASE_URL (estándar de Render)
        if (dbUrl == null) {
            dbUrl = System.getenv("DATABASE_URL");
        }

        if (dbUrl == null) {
            // Valores para tu entorno local
            dbUrl = "jdbc:postgresql://localhost:5432/vacunacion_db";
            user = "postgres";
            pass = "123";
        } else {
            // Forzar SSL para Render si no viene en la cadena
            if (!dbUrl.contains("sslmode")) {
                dbUrl += (dbUrl.contains("?") ? "&" : "?") + "sslmode=require";
            }
        }

        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl, user, pass);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("Driver PostgreSQL no encontrado", ex);
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error al cerrar conexión: " + ex.getMessage());
        }
    }
}
