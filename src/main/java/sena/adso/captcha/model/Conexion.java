package sena.adso.captcha.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Se obtienen de las variables de entorno de Render (PostgreSQL)
    private static final String HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String PORT = System.getenv().getOrDefault("DB_PORT", "5432"); // Postgres usa 5432
    private static final String DBNAME = System.getenv().getOrDefault("DB_NAME", "vacunacion_db");
    
    // URL dinámica ajustada para PostgreSQL
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DBNAME;

    private static final String USER = System.getenv().getOrDefault("DB_USER", "postgres"); 
    private static final String PASS = System.getenv().getOrDefault("DB_PASS", "123");

    public static Connection getConnection() throws SQLException {
        try {
            // CAMBIO: Ahora usamos el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException ex) {
            // CAMBIO: Mensaje de error actualizado
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
