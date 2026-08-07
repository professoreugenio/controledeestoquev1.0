package util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImagemProdutoUtil {

    private static final String PASTA_IMAGENS_PRODUTOS = "imagens/produtos";

    public static String copiarImagem(File arquivoSelecionado) throws IOException {

        if (arquivoSelecionado == null) {
            return null;
        }

        File pastaDestino = new File(PASTA_IMAGENS_PRODUTOS);

        if (!pastaDestino.exists()) {
            pastaDestino.mkdirs();
        }

        String nomeOriginal = arquivoSelecionado.getName();
        String extensao = "";

        int posicaoPonto = nomeOriginal.lastIndexOf(".");

        if (posicaoPonto >= 0) {
            extensao = nomeOriginal.substring(posicaoPonto).toLowerCase();
        }

        String novoNome = "produto_" + UUID.randomUUID().toString() + extensao;

        File arquivoDestino = new File(pastaDestino, novoNome);

        Files.copy(
                arquivoSelecionado.toPath(),
                arquivoDestino.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        return PASTA_IMAGENS_PRODUTOS + "/" + novoNome;
    }

    public static void exibirImagem(JLabel label, String caminhoImagem) {

        if (caminhoImagem == null || caminhoImagem.trim().isEmpty()) {
            limparImagem(label);
            return;
        }

        File arquivoImagem = new File(caminhoImagem);

        if (!arquivoImagem.exists()) {
            limparImagem(label);
            return;
        }

        ImageIcon imagemOriginal = new ImageIcon(arquivoImagem.getAbsolutePath());

        Image imagemRedimensionada = imagemOriginal.getImage().getScaledInstance(
                label.getWidth(),
                label.getHeight(),
                Image.SCALE_SMOOTH
        );

        label.setText("");
        label.setIcon(new ImageIcon(imagemRedimensionada));
    }

    public static void limparImagem(JLabel label) {
        label.setIcon(null);
        label.setText("Sem imagem");
    }

    public static boolean extensaoPermitida(File arquivo) {

        if (arquivo == null) {
            return false;
        }

        String nome = arquivo.getName().toLowerCase();

        return nome.endsWith(".jpg")
                || nome.endsWith(".jpeg")
                || nome.endsWith(".png");
    }
}
