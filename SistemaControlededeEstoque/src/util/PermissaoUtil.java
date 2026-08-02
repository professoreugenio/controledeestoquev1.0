package util;

import model.Usuario;

public class PermissaoUtil {

    public static boolean isAdministrador(Usuario usuario) {

        if (usuario == null || usuario.getPerfil() == null) {
            return false;
        }

        String perfil = usuario.getPerfil().trim().toUpperCase();

        return perfil.equals("ADMINISTRADOR") || perfil.equals("ADMIN");
    }

    public static boolean isOperador(Usuario usuario) {

        if (usuario == null || usuario.getPerfil() == null) {
            return false;
        }

        String perfil = usuario.getPerfil().trim().toUpperCase();

        return perfil.equals("OPERADOR");
    }
}
