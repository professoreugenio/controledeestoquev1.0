package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SenhaUtil {

    public static String gerarHash(String senha) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashTexto = new StringBuilder();

            for (byte b : hashBytes) {
                hashTexto.append(String.format("%02x", b));
            }

            return hashTexto.toString();

        } catch (NoSuchAlgorithmException erro) {
            throw new RuntimeException("Erro ao gerar hash da senha.", erro);
        }
    }

    public static boolean verificarSenha(String senhaDigitada, String senhaHashBanco) {
        String senhaHashDigitada = gerarHash(senhaDigitada);
        return senhaHashDigitada.equals(senhaHashBanco);
    }
}
