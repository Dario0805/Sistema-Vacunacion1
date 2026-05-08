package sena.adso.captcha.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import sena.adso.captcha.dao.HorarioDAO;
import sena.adso.captcha.dto.Horario;

@WebServlet(name = "HorariosDisponiblesServlet", urlPatterns = {"/horarios-disponibles"})
public class HorariosDisponiblesServlet extends HttpServlet {

    private final HorarioDAO horarioDAO = new HorarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<Horario> horarios = horarioDAO.obtenerDisponibles();

        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < horarios.size(); i++) {
            Horario h = horarios.get(i);

            if (i > 0) {
                json.append(",");
            }

            json.append("{");
            json.append("\"id\":").append(h.getId()).append(",");
            json.append("\"diaSemana\":\"").append(escape(h.getDiaSemana())).append("\",");
            json.append("\"horaInicio\":\"").append(escape(h.getHoraInicio())).append("\",");
            json.append("\"horaFin\":\"").append(escape(h.getHoraFin())).append("\",");
            json.append("\"medico\":\"").append(escape(h.getMedico())).append("\",");
            json.append("\"especialidad\":\"").append(escape(h.getEspecialidad())).append("\"");
            json.append("}");
        }

        json.append("]");

        response.getWriter().write(json.toString());
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}