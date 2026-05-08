package sena.adso.captcha.util;

import sena.adso.captcha.dto.Usuario;

/**
 * Utilidad central para validar permisos por rol.
 * Roles segun especificacion: MEDICO, RECEPCIONISTA, ENFERMERO.
 */
public final class SeguridadUtil {
    private SeguridadUtil() {}

    public static String rol(Usuario usuario) {
        return usuario == null || usuario.getRol() == null ? "" : usuario.getRol().trim().toUpperCase();
    }

    public static boolean esMedico(Usuario usuario) {
        String rol = rol(usuario);
        return "MEDICO".equals(rol) || "ADMIN".equals(rol) || "ADMINISTRADOR".equals(rol);
    }

    public static boolean esRecepcionista(Usuario usuario) {
        return "RECEPCIONISTA".equals(rol(usuario));
    }

    public static boolean esEnfermero(Usuario usuario) {
        return "ENFERMERO".equals(rol(usuario));
    }

    public static boolean puedeIngresar(Usuario usuario) {
        return esMedico(usuario) || esRecepcionista(usuario) || esEnfermero(usuario);
    }

    public static boolean puedeGestionarPacientes(Usuario usuario) {
        return esMedico(usuario) || esRecepcionista(usuario);
    }

    public static boolean puedeGestionarCitas(Usuario usuario) {
        return esMedico(usuario) || esRecepcionista(usuario);
    }

    public static boolean puedeGestionarHorarios(Usuario usuario) {
        return esMedico(usuario);
    }

    public static boolean puedeGestionarUsuarios(Usuario usuario) {
        return esMedico(usuario);
    }
}
