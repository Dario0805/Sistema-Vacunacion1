package sena.adso.captcha.controller;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import sena.adso.captcha.dao.UsuarioDAO;
import sena.adso.captcha.dto.Usuario;

public class LoginServlet extends HttpServlet {

    private static final SecureRandom OTP_RANDOM = new SecureRandom();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (request.getParameter("reset") != null) {
            session.removeAttribute("otpPending");
            session.removeAttribute("otpCode");
            session.removeAttribute("tempUser");
        }
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String otpIngresado = request.getParameter("otp");

        // 1. VALIDAR OTP (Si ya se envió previamente)
        if (session.getAttribute("otpPending") != null && otpIngresado != null) {
            String otpReal = (String) session.getAttribute("otpCode");
            if (otpIngresado.equals(otpReal)) {
                Usuario usuario = (Usuario) session.getAttribute("tempUser");
                if (usuario == null) {
                    session.invalidate();
                    request.setAttribute("error", "Sesión expirada.");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                iniciarSesion(session, usuario);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } else {
                request.setAttribute("error", "Código OTP incorrecto");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
            return;
        }

        // 2. LOGIN NORMAL (Usuario y Contraseña)
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.validarLogin(username, password);

        if (usuario != null) {
            // Verificar si el rol requiere segundo factor de autenticación
            if (requiereOtpAdministrador(usuario)) {
                String generatedOTP = String.format("%06d", OTP_RANDOM.nextInt(999999));
                session.setAttribute("otpCode", generatedOTP);
                session.setAttribute("tempUser", usuario);
                session.setAttribute("otpPending", true);

                try {
                    enviarOtpPorCorreo(usuario.getEmail(), generatedOTP);
                } catch (Exception e) {
                    System.err.println("Fallo crítico en envío de correo: " + e.getMessage());
                }
                
                // Redirige de nuevo a /login para que el doGet cargue el JSP con el campo OTP
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                // Usuarios sin OTP (ej. Pacientes si así lo definiste)
                iniciarSesion(session, usuario);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            request.setAttribute("error", "Usuario o contraseña incorrectos");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void enviarOtpPorCorreo(String destinatario, String otp) {
        final String correoRemitente = "clinipetadso@gmail.com";
        final String claveEnv = System.getenv("MAIL_PASSWORD");
        final String claveAplicacion = (claveEnv != null) ? claveEnv : "qqzopsuxfmdcswmy";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        // Configuración de tiempos de espera para evitar bloqueos en Render
        props.put("mail.smtp.connectiontimeout", "10000"); 
        props.put("mail.smtp.timeout", "10000");

        Session mailSession = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoRemitente, claveAplicacion);
            }
        });

        try {
            Message mensaje = new MimeMessage(mailSession);
            mensaje.setFrom(new InternetAddress(correoRemitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Código OTP de seguridad");
            mensaje.setText("Su código de acceso es: " + otp);

            Transport.send(mensaje);
            System.out.println("✅ OTP enviado exitosamente a: " + destinatario);

        } catch (MessagingException e) {
            System.err.println("❌ Error SMTP (Puerto 465): " + e.getMessage());
            // Log de emergencia para poder entrar si el servidor de correo falla
            System.err.println("OTP DE EMERGENCIA [" + destinatario + "]: " + otp);
        }
    }

    private boolean requiereOtpAdministrador(Usuario usuario) {
        String rol = (usuario.getRol() != null) ? usuario.getRol().toUpperCase() : "";
        return rol.contains("ADMIN") || rol.contains("MEDICO");
    }

    private void iniciarSesion(HttpSession session, Usuario usuario) {
        session.setAttribute("usuario", usuario);
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("usuarioNombre", usuario.getNombres() + " " + usuario.getApellidos());
        session.setAttribute("usuarioRol", usuario.getRol());
        
        // Limpiar datos temporales del OTP
        session.removeAttribute("otpPending");
        session.removeAttribute("otpCode");
        session.removeAttribute("tempUser");
    }
}
