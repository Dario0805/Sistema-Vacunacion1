package sena.adso.captcha.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import sena.adso.captcha.dao.HorarioDAO;
import sena.adso.captcha.dao.UsuarioDAO;
import sena.adso.captcha.dto.Horario;

public class HorarioServlet extends HttpServlet {

    private final HorarioDAO horarioDAO = new HorarioDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        switch (accion) {
            case "nuevo":
                request.setAttribute("usuarios", usuarioDAO.obtenerMedicos());
                request.setAttribute("accion", "nuevo");
                request.getRequestDispatcher("/views/horario_form.jsp").forward(request, response);
                break;

            case "editar":
                editar(request, response);
                break;

            case "eliminar":
                eliminar(request, response);
                break;

            case "listar":
            default:
                listar(request, response);
                break;
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtenemos la lista de la base de datos
        List<Horario> lista = horarioDAO.obtenerTodos();
        
        // Pasamos la lista al JSP
        request.setAttribute("horarios", lista);

        request.getRequestDispatcher("/views/horarios.jsp").forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        request.setAttribute("horario", horarioDAO.obtenerPorId(id));
        request.setAttribute("usuarios", usuarioDAO.obtenerMedicos());
        request.setAttribute("accion", "editar");

        request.getRequestDispatcher("/views/horario_form.jsp").forward(request, response);
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        horarioDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/horarios");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Horario horario = new Horario();
        String id = request.getParameter("id");

        if (id != null && !id.trim().isEmpty()) {
            horario.setId(Integer.parseInt(id));
        }

        horario.setIdUsuario(Integer.parseInt(request.getParameter("idUsuario")));
        horario.setDiaSemana(request.getParameter("diaSemana"));
        horario.setHoraInicio(request.getParameter("horaInicio"));
        horario.setHoraFin(request.getParameter("horaFin"));
        horario.setEstado(request.getParameter("estado"));

        if (horario.getId() > 0) {
            horarioDAO.actualizar(horario);
        } else {
            horarioDAO.insertar(horario);
        }

        // Importante: Redirigir siempre al listado para refrescar datos
        response.sendRedirect(request.getContextPath() + "/horarios");
    }
}
