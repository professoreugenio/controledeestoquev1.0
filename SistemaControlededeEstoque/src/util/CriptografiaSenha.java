package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CriptografiaSenha {

    public static String gerarHash(String senha) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashBytes = digest.digest(senha.getBytes());

            StringBuilder senhaCriptografada = new StringBuilder();

            for (byte b : hashBytes) {
                senhaCriptografada.append(String.format("%02x", b));
            }

            return senhaCriptografada.toString();

        } catch (NoSuchAlgorithmException erro) {
            throw new RuntimeException("Erro ao criptografar senha.", erro);
        }
    }
}
