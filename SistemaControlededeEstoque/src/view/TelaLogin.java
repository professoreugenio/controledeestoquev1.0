package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import dao.UsuarioDAO;
import model.Usuario;

public class TelaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    /*
     * Cores utilizadas na tela.
     */
    private static final Color COR_PRIMARIA =
            new Color(31, 78, 121);

    private static final Color COR_PRIMARIA_ESCURA =
            new Color(23, 57, 88);

    private static final Color COR_FUNDO =
            new Color(242, 245, 249);

    private static final Color COR_BRANCA =
            Color.WHITE;

    private static final Color COR_TEXTO =
            new Color(55, 55, 55);

    private static final Color COR_TEXTO_SECUNDARIO =
            new Color(110, 110, 110);

    private static final Color COR_BORDA =
            new Color(210, 216, 224);

    private static final Color COR_SAIR =
            new Color(108, 117, 125);

    /*
     * Fontes utilizadas na tela.
     */
    private static final Font FONTE_TITULO =
            new Font("Segoe UI", Font.BOLD, 24);

    private static final Font FONTE_SUBTITULO =
            new Font("Segoe UI", Font.PLAIN, 14);

    private static final Font FONTE_LABEL =
            new Font("Segoe UI", Font.BOLD, 13);

    private static final Font FONTE_CAMPO =
            new Font("Segoe UI", Font.PLAIN, 14);

    private static final Font FONTE_BOTAO =
            new Font("Segoe UI", Font.BOLD, 14);

    private JPanel contentPane;

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JCheckBox chkMostrarSenha;

    private JButton btnEntrar;
    private JButton btnSair;

    /*
     * Guarda o caractere original utilizado pelo campo de senha.
     */
    private char caractereSenhaPadrao;

    public static void main(String[] args) {

        aplicarAparencia();

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {

                    TelaLogin frame = new TelaLogin();
                    frame.setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();

                    JOptionPane.showMessageDialog(
                            null,
                            "Não foi possível iniciar a tela de login.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    /*
     * Aplica o tema Nimbus, que já acompanha o Java.
     */
    private static void aplicarAparencia() {

        try {

            for (UIManager.LookAndFeelInfo tema
                    : UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(tema.getName())) {

                    UIManager.setLookAndFeel(
                            tema.getClassName()
                    );

                    break;
                }
            }

            UIManager.put(
                    "OptionPane.messageFont",
                    new Font("Segoe UI", Font.PLAIN, 14)
            );

            UIManager.put(
                    "OptionPane.buttonFont",
                    new Font("Segoe UI", Font.PLAIN, 13)
            );

        } catch (Exception e) {

            System.err.println(
                    "Não foi possível aplicar o tema Nimbus."
            );
        }
    }

    public TelaLogin() {

        configurarJanela();
        montarInterface();
        configurarEventos();

        /*
         * Centraliza a janela depois que os componentes
         * foram montados.
         */
        setLocationRelativeTo(null);

        /*
         * Coloca o cursor diretamente no campo login.
         */
        txtLogin.requestFocusInWindow();
    }

    /*
     * Configurações gerais da janela.
     */
    private void configurarJanela() {

        setTitle("Sistema de Controle de Estoque - Login");

        setDefaultCloseOperation(
                JFrame.DO_NOTHING_ON_CLOSE
        );

        setSize(520, 610);
        setResizable(false);
    }

    /*
     * Monta toda a interface da tela.
     */
    private void montarInterface() {

        contentPane = new JPanel(
                new BorderLayout()
        );

        contentPane.setBackground(COR_FUNDO);

        setContentPane(contentPane);

        criarCabecalho();
        criarAreaLogin();
        criarRodape();
    }

    /*
     * Cria o cabeçalho azul da tela.
     */
    private void criarCabecalho() {

        JPanel painelCabecalho = new JPanel();

        painelCabecalho.setLayout(
                new BoxLayout(
                        painelCabecalho,
                        BoxLayout.Y_AXIS
                )
        );

        painelCabecalho.setBackground(COR_PRIMARIA);

        painelCabecalho.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        JLabel lblTitulo = new JLabel(
                "Controle de Estoque"
        );

        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_BRANCA);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(
                "Acesse o sistema utilizando suas credenciais"
        );

        lblSubtitulo.setFont(FONTE_SUBTITULO);
        lblSubtitulo.setForeground(
                new Color(225, 235, 245)
        );

        lblSubtitulo.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        painelCabecalho.add(lblTitulo);
        painelCabecalho.add(Box.createVerticalStrut(6));
        painelCabecalho.add(lblSubtitulo);

        contentPane.add(
                painelCabecalho,
                BorderLayout.NORTH
        );
    }

    /*
     * Cria a área central contendo o formulário.
     */
    private void criarAreaLogin() {

        JPanel painelCentral = new JPanel(
                new GridBagLayout()
        );

        painelCentral.setBackground(COR_FUNDO);

        painelCentral.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        20,
                        30
                )
        );

        JPanel painelFormulario = new JPanel();

        painelFormulario.setLayout(
                new BoxLayout(
                        painelFormulario,
                        BoxLayout.Y_AXIS
                )
        );

        painelFormulario.setBackground(COR_BRANCA);

        painelFormulario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                25,
                                30,
                                25,
                                30
                        )
                )
        );

        painelFormulario.setPreferredSize(
                new Dimension(410, 350)
        );

        /*
         * Título do formulário.
         */
        JLabel lblAcesso = new JLabel(
                "Acesso ao sistema"
        );

        lblAcesso.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        lblAcesso.setForeground(
                COR_PRIMARIA_ESCURA
        );

        lblAcesso.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel lblOrientacao = new JLabel(
                "Informe seu login e sua senha."
        );

        lblOrientacao.setFont(FONTE_SUBTITULO);
        lblOrientacao.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        lblOrientacao.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painelFormulario.add(lblAcesso);
        painelFormulario.add(Box.createVerticalStrut(4));
        painelFormulario.add(lblOrientacao);
        painelFormulario.add(Box.createVerticalStrut(22));

        /*
         * Campo login.
         */
        JLabel lblLogin = criarLabel("Login");

        painelFormulario.add(lblLogin);
        painelFormulario.add(Box.createVerticalStrut(6));

        txtLogin = new JTextField();

        estilizarCampo(txtLogin);

        txtLogin.setToolTipText(
                "Digite seu login de acesso"
        );

        painelFormulario.add(txtLogin);
        painelFormulario.add(Box.createVerticalStrut(16));

        /*
         * Campo senha.
         */
        JLabel lblSenha = criarLabel("Senha");

        painelFormulario.add(lblSenha);
        painelFormulario.add(Box.createVerticalStrut(6));

        txtSenha = new JPasswordField();

        estilizarCampo(txtSenha);

        txtSenha.setToolTipText(
                "Digite sua senha de acesso"
        );

        caractereSenhaPadrao =
                txtSenha.getEchoChar();

        painelFormulario.add(txtSenha);
        painelFormulario.add(Box.createVerticalStrut(8));

        /*
         * Opção mostrar senha.
         */
        chkMostrarSenha = new JCheckBox(
                "Mostrar senha"
        );

        chkMostrarSenha.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        chkMostrarSenha.setForeground(COR_TEXTO);
        chkMostrarSenha.setBackground(COR_BRANCA);
        chkMostrarSenha.setFocusPainted(false);

        chkMostrarSenha.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painelFormulario.add(chkMostrarSenha);
        painelFormulario.add(Box.createVerticalStrut(20));

        /*
         * Área dos botões.
         */
        JPanel painelBotoes = new JPanel(
                new GridLayout(
                        1,
                        2,
                        12,
                        0
                )
        );

        painelBotoes.setOpaque(false);

        painelBotoes.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        42
                )
        );

        painelBotoes.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        btnEntrar = criarBotao(
                "Entrar",
                COR_PRIMARIA
        );

        btnSair = criarBotao(
                "Sair",
                COR_SAIR
        );

        painelBotoes.add(btnEntrar);
        painelBotoes.add(btnSair);

        painelFormulario.add(painelBotoes);

        painelCentral.add(painelFormulario);

        contentPane.add(
                painelCentral,
                BorderLayout.CENTER
        );
    }

    /*
     * Cria o rodapé da tela.
     */
    private void criarRodape() {

        JLabel lblRodape = new JLabel(
                "Sistema de Controle de Estoque • Acesso restrito"
        );

        lblRodape.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        lblRodape.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblRodape.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        lblRodape.setBorder(
                new EmptyBorder(
                        5,
                        10,
                        15,
                        10
                )
        );

        contentPane.add(
                lblRodape,
                BorderLayout.SOUTH
        );
    }

    /*
     * Cria os textos que ficam acima dos campos.
     */
    private JLabel criarLabel(String texto) {

        JLabel label = new JLabel(texto);

        label.setFont(FONTE_LABEL);
        label.setForeground(COR_TEXTO);

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    /*
     * Aplica a mesma aparência nos campos.
     */
    private void estilizarCampo(JTextField campo) {

        campo.setFont(FONTE_CAMPO);

        campo.setPreferredSize(
                new Dimension(350, 40)
        );

        campo.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        40
                )
        );

        campo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                7,
                                10,
                                7,
                                10
                        )
                )
        );

        campo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );
    }

    /*
     * Cria um botão com aparência padronizada.
     */
    private JButton criarBotao(
            String texto,
            Color corFundo
    ) {

        JButton botao = new JButton(texto);

        botao.setFont(FONTE_BOTAO);
        botao.setForeground(COR_BRANCA);
        botao.setBackground(corFundo);

        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);

        botao.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        botao.setBorder(
                new EmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        return botao;
    }

    /*
     * Configura as ações dos componentes.
     */
    private void configurarEventos() {

        btnEntrar.addActionListener(
                e -> entrar()
        );

        btnSair.addActionListener(
                e -> sair()
        );

        chkMostrarSenha.addActionListener(
                e -> mostrarSenha()
        );

        /*
         * Pressionar Enter executa o login.
         */
        getRootPane().setDefaultButton(
                btnEntrar
        );

        /*
         * Pressionar ESC solicita o fechamento.
         */
        getRootPane().registerKeyboardAction(
                e -> sair(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        /*
         * Clicar no X também solicita confirmação.
         */
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                sair();
            }
        });
    }

    /*
     * Valida os campos e consulta o usuário no banco.
     */
    private void entrar() {

        String login =
                txtLogin.getText().trim();

        String senha =
                new String(
                        txtSenha.getPassword()
                ).trim();

        if (login.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe o login.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );

            txtLogin.requestFocusInWindow();

            return;
        }

        if (senha.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Informe a senha.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );

            txtSenha.requestFocusInWindow();

            return;
        }

        /*
         * Evita vários cliques enquanto o login
         * está sendo processado.
         */
        btnEntrar.setEnabled(false);
        btnEntrar.setText("Aguarde...");

        try {

            UsuarioDAO usuarioDAO =
                    new UsuarioDAO();

            Usuario usuario =
                    usuarioDAO.autenticar(
                            login,
                            senha
                    );

            if (usuario != null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Bem-vindo, "
                                + usuario.getNome()
                                + "!",
                        "Login realizado",
                        JOptionPane.INFORMATION_MESSAGE
                );

                TelaPrincipal telaPrincipal =
                        new TelaPrincipal(usuario);

                telaPrincipal.setVisible(true);

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Login ou senha inválidos.",
                        "Acesso negado",
                        JOptionPane.ERROR_MESSAGE
                );

                txtSenha.setText("");
                txtSenha.requestFocusInWindow();
            }

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível realizar o login.\n"
                            + "Verifique a conexão com o banco de dados.",
                    "Erro de conexão",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            btnEntrar.setEnabled(true);
            btnEntrar.setText("Entrar");
        }
    }

    /*
     * Mostra ou oculta os caracteres da senha.
     */
    private void mostrarSenha() {

        if (chkMostrarSenha.isSelected()) {

            txtSenha.setEchoChar((char) 0);

        } else {

            txtSenha.setEchoChar(
                    caractereSenhaPadrao
            );
        }
    }

    /*
     * Solicita confirmação antes de fechar.
     */
    private void sair() {

        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente sair do sistema?",
                        "Confirmar saída",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (resposta
                == JOptionPane.YES_OPTION) {

            dispose();
        }
    }
}