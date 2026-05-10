package sena.adso.captcha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sena.adso.captcha.dto.Horario;
import sena.adso.captcha.model.Conexion;

public class HorarioDAO {

    public List<Horario> obtenerTodos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Horario> horarios = new ArrayList<>();

        try {
            conn = Conexion.getConnection();

            String sql = "SELECT h.id, h.id_usuario, h.dia_semana, h.hora_inicio, h.hora_fin, "
                    + "u.nombres || ' ' || u.apellidos AS medico, "
                    + "COALESCE(u.especialidad, 'Sin especialidad') AS especialidad, "
                    + "h.estado "
                    + "FROM horarios h "
                    + "INNER JOIN usuarios u ON h.id_usuario = u.id "
                    + "ORDER BY h.id DESC";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Horario horario = mapearHorarioListado(rs);
                horarios.add(horario);
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener horarios: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }

        return horarios;
    }

    public List<Horario> obtenerDisponibles() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Horario> horarios = new ArrayList<>();

        try {
            conn = Conexion.getConnection();

            String sql = "SELECT h.id, h.id_usuario, h.dia_semana, h.hora_inicio, h.hora_fin, "
                    + "u.nombres || ' ' || u.apellidos AS medico, "
                    + "COALESCE(u.especialidad, 'Sin especialidad') AS especialidad, "
                    + "h.estado "
                    + "FROM horarios h "
                    + "INNER JOIN usuarios u ON h.id_usuario = u.id "
                    + "WHERE h.estado = 'DISPONIBLE' "
                    + "ORDER BY h.dia_semana, h.hora_inicio";

            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Horario horario = mapearHorarioListado(rs);
                horarios.add(horario);
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener horarios disponibles: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }

        return horarios;
    }

    public boolean insertar(Horario horario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = Conexion.getConnection();

            // CAMBIO: Se agrega ::time para convertir el String de Java al tipo TIME de PostgreSQL
            String sql = "INSERT INTO horarios "
                    + "(id_usuario, dia_semana, hora_inicio, hora_fin, estado) "
                    + "VALUES (?, ?, ?::time, ?::time, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, horario.getIdUsuario());
            stmt.setString(2, horario.getDiaSemana());
            stmt.setString(3, horario.getHoraInicio());
            stmt.setString(4, horario.getHoraFin());
            stmt.setString(5, horario.getEstado());

            exito = stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error al insertar horario: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }

        return exito;
    }

    public boolean actualizar(Horario horario) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = Conexion.getConnection();

            // CAMBIO: Se agrega ::time para convertir el String de Java al tipo TIME de PostgreSQL
            String sql = "UPDATE horarios SET "
                    + "id_usuario=?, dia_semana=?, hora_inicio=?::time, hora_fin=?::time, estado=? "
                    + "WHERE id=?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, horario.getIdUsuario());
            stmt.setString(2, horario.getDiaSemana());
            stmt.setString(3, horario.getHoraInicio());
            stmt.setString(4, horario.getHoraFin());
            stmt.setString(5, horario.getEstado());
            stmt.setInt(6, horario.getId());

            exito = stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error al actualizar horario: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }

        return exito;
    }

    public boolean eliminar(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean exito = false;

        try {
            conn = Conexion.getConnection();

            String sql = "DELETE FROM horarios WHERE id=?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            exito = stmt.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.err.println("Error al eliminar horario: " + ex.getMessage());
        } finally {
            cerrar(null, stmt, conn);
        }

        return exito;
    }

    public Horario obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Horario horario = null;

        try {
            conn = Conexion.getConnection();

            String sql = "SELECT id, id_usuario, dia_semana, hora_inicio, hora_fin, estado "
                    + "FROM horarios WHERE id=?";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                horario = new Horario();
                horario.setId(rs.getInt("id"));
                horario.setIdUsuario(rs.getInt("id_usuario"));
                horario.setDiaSemana(rs.getString("dia_semana"));
                horario.setHoraInicio(rs.getString("hora_inicio"));
                horario.setHoraFin(rs.getString("hora_fin"));
                horario.setEstado(rs.getString("estado"));
            }

        } catch (SQLException ex) {
            System.err.println("Error al obtener horario: " + ex.getMessage());
        } finally {
            cerrar(rs, stmt, conn);
        }

        return horario;
    }

    private Horario mapearHorarioListado(ResultSet rs) throws SQLException {
        Horario horario = new Horario();

        horario.setId(rs.getInt("id"));
        horario.setIdUsuario(rs.getInt("id_usuario"));
        horario.setDiaSemana(rs.getString("dia_semana"));
        horario.setHoraInicio(rs.getString("hora_inicio"));
        horario.setHoraFin(rs.getString("hora_fin"));
        horario.setMedico(rs.getString("medico"));
        horario.setEspecialidad(rs.getString("especialidad"));
        horario.setEstado(rs.getString("estado"));

        return horario;
    }

    private void cerrar(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }

            Conexion.closeConnection(conn);

        } catch (SQLException ex) {
            System.err.println("Error al cerrar conexión: " + ex.getMessage());
        }
    }
}
