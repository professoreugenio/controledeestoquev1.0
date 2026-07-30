package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import model.Usuario;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    /*
     * Cores principais do sistema
     */
    private static final Color COR_PRIMARIA =
            new Color(31, 78, 121);

    private static final Color COR_PRIMARIA_ESCURA =
            new Color(23, 57, 88);

    private static final Color COR_FUNDO =
            new Color(244, 246, 249);

    private static final Color COR_BRANCA =
            Color.WHITE;

    private static final Color COR_TEXTO =
            new Color(55, 55, 55);

    private static final Color COR_TEXTO_SECUNDARIO =
            new Color(110, 110, 110);

    private static final Color COR_BORDA =
            new Color(220, 224, 230);

    private static final Color COR_USUARIO =
            new Color(232, 240, 248);

    private static final Color COR_SAIR =
            new Color(190, 50, 50);

    /*
     * Fontes utilizadas na tela
     */
    private static final Font FONTE_TITULO =
            new Font("Segoe UI", Font.BOLD, 24);

    private static final Font FONTE_SUBTITULO =
            new Font("Segoe UI", Font.PLAIN, 13);

    private static final Font FONTE_MENU =
            new Font("Segoe UI", Font.BOLD, 14);

    private static final Font FONTE_ITEM_MENU =
            new Font("Segoe UI", Font.PLAIN, 14);

    private static final Font FONTE_USUARIO =
            new Font("Segoe UI", Font.BOLD, 13);

    private static final Font FONTE_PERFIL =
            new Font("Segoe UI", Font.PLAIN, 12);

    /*
     * Componentes principais
     */
    private JPanel contentPane;
    private JPanel painelConteudo;

    private JLabel lblUsuarioLogado;
    private JLabel lblPerfilUsuario;
    private JLabel lblStatus;

    private Usuario usuarioLogado;

    public static void main(String[] args) {

        aplicarAparencia();

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {

                    TelaPrincipal frame =
                            new TelaPrincipal();

                    frame.setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();

                    JOptionPane.showMessageDialog(
                            null,
                            "Não foi possível iniciar a tela principal.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    /*
     * Aplica o tema Nimbus.
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

    /*
     * Construtor usado para testes.
     */
    public TelaPrincipal() {

        this(null);
    }

    /*
     * Construtor utilizado após o login.
     */
    public TelaPrincipal(Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        configurarJanela();
        setJMenuBar(criarBarraMenu());
        montarInterface();

        exibirUsuarioLogado();

        /*
         * Abre o Dashboard automaticamente.
         */
        carregarPainel(
                new PainelDashboard(),
                "Dashboard"
        );
    }

    /*
     * Configurações gerais do JFrame.
     */
    private void configurarJanela() {

        setTitle(
                "Sistema de Controle de Estoque"
        );

        /*
         * Permite controlar o fechamento pelo método sair().
         */
        setDefaultCloseOperation(
                JFrame.DO_NOTHING_ON_CLOSE
        );

        setSize(1180, 760);

        /*
         * Impede que a janela fique pequena demais.
         */
        setMinimumSize(
                new Dimension(950, 620)
        );

        setLocationRelativeTo(null);

        /*
         * Quando o usuário clicar no X,
         * será exibida a confirmação de saída.
         */
        addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(
                            WindowEvent e
                    ) {

                        sair();
                    }
                }
        );
    }

    /*
     * Cria toda a barra superior de menus.
     */
    private JMenuBar criarBarraMenu() {

        JMenuBar menuBar =
                new JMenuBar();

        menuBar.setBackground(
                COR_PRIMARIA
        );

        menuBar.setOpaque(true);

        menuBar.setBorder(
                new EmptyBorder(
                        4,
                        12,
                        4,
                        12
                )
        );

        /*
         * Menu Início
         */
        JMenu menuInicio =
                criarMenu("Início");

        menuInicio.add(
                criarItemMenu(
                        "Dashboard",
                        e -> carregarPainel(
                                new PainelDashboard(),
                                "Dashboard"
                        )
                )
        );

        menuBar.add(menuInicio);

        /*
         * Menu Produtos
         */
        JMenu menuProdutos =
                criarMenu("Produtos");

        menuProdutos.add(
                criarItemMenu(
                        "Cadastrar produto",
                        e -> carregarPainel(
                                new PainelCadastrarProduto(),
                                "Cadastrar produto"
                        )
                )
        );

        menuProdutos.add(
                criarItemMenu(
                        "Listar produtos",
                        e -> carregarPainel(
                                new PainelListarProdutos(),
                                "Listar produtos"
                        )
                )
        );

        menuBar.add(menuProdutos);

        /*
         * Menu Clientes
         */
        JMenu menuClientes =
                criarMenu("Clientes");

        menuClientes.add(
                criarItemMenu(
                        "Cadastrar cliente",
                        e -> carregarPainel(
                                new PainelCadastrarCliente(),
                                "Cadastrar cliente"
                        )
                )
        );

        menuClientes.add(
                criarItemMenu(
                        "Listar clientes",
                        e -> carregarPainel(
                                new PainelListarClientes(),
                                "Listar clientes"
                        )
                )
        );

        menuBar.add(menuClientes);

        /*
         * Menu Estoque
         */
        JMenu menuEstoque =
                criarMenu("Estoque");

        menuEstoque.add(
                criarItemMenu(
                        "Consultar estoque",
                        e -> carregarPainel(
                                new PainelListarEstoque(),
                                "Consultar estoque"
                        )
                )
        );

        menuEstoque.addSeparator();

        menuEstoque.add(
                criarItemMenu(
                        "Entrada de estoque",
                        e -> carregarPainel(
                                new PainelEntradaEstoque(),
                                "Entrada de estoque"
                        )
                )
        );

        menuEstoque.add(
                criarItemMenu(
                        "Saída de estoque",
                        e -> carregarPainel(
                                new PainelSaidaEstoque(),
                                "Saída de estoque"
                        )
                )
        );

        menuBar.add(menuEstoque);

        /*
         * Menu Usuário
         */
        JMenu menuUsuario =
                criarMenu("Usuário");

        menuUsuario.add(
                criarItemMenu(
                        "Editar perfil",
                        e -> exibirRecursoEmDesenvolvimento(
                                "Editar perfil"
                        )
                )
        );

        /*
         * Exemplo de controle por perfil.
         *
         * Você poderá permitir esse recurso apenas
         * para administradores.
         */
        JMenuItem itemNovoUsuario =
                criarItemMenu(
                        "Novo usuário",
                        e -> exibirRecursoEmDesenvolvimento(
                                "Cadastro de usuário"
                        )
                );

        menuUsuario.add(itemNovoUsuario);

        menuBar.add(menuUsuario);

        /*
         * Cria um espaço flexível.
         * O menu Sistema será colocado à direita.
         */
        menuBar.add(
                Box.createHorizontalGlue()
        );

        /*
         * Menu Sistema
         */
        JMenu menuSistema =
                criarMenu("Sistema");

        JMenuItem itemSair =
                criarItemMenu(
                        "Encerrar sistema",
                        e -> sair()
                );

        itemSair.setForeground(
                COR_SAIR
        );

        menuSistema.add(itemSair);

        menuBar.add(menuSistema);

        return menuBar;
    }

    /*
     * Cria um menu principal padronizado.
     */
    private JMenu criarMenu(
            String texto
    ) {

        JMenu menu =
                new JMenu(texto);

        menu.setFont(FONTE_MENU);
        menu.setForeground(COR_BRANCA);
        menu.setBackground(COR_PRIMARIA);
        menu.setOpaque(true);

        menu.setMargin(
                new Insets(
                        8,
                        14,
                        8,
                        14
                )
        );

        menu.getPopupMenu().setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA
                )
        );

        return menu;
    }

    /*
     * Cria um item de menu padronizado.
     */
    private JMenuItem criarItemMenu(
            String texto,
            ActionListener acao
    ) {

        JMenuItem item =
                new JMenuItem(texto);

        item.setFont(FONTE_ITEM_MENU);
        item.setForeground(COR_TEXTO);
        item.setBackground(COR_BRANCA);
        item.setOpaque(true);

        item.setBorder(
                new EmptyBorder(
                        9,
                        16,
                        9,
                        16
                )
        );

        item.addActionListener(acao);

        return item;
    }

    /*
     * Monta a estrutura da tela.
     */
    private void montarInterface() {

        contentPane =
                new JPanel(
                        new BorderLayout()
                );

        contentPane.setBackground(
                COR_FUNDO
        );

        setContentPane(contentPane);

        criarCabecalho();
        criarAreaConteudo();
        criarRodape();
    }

    /*
     * Cria o cabeçalho da tela.
     */
    private void criarCabecalho() {

        JPanel painelTopo =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        painelTopo.setBackground(
                COR_BRANCA
        );

        painelTopo.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                COR_BORDA
                        ),
                        new EmptyBorder(
                                17,
                                24,
                                17,
                                24
                        )
                )
        );

        /*
         * Área do título e subtítulo.
         */
        JPanel painelTitulos =
                new JPanel();

        painelTitulos.setLayout(
                new BoxLayout(
                        painelTitulos,
                        BoxLayout.Y_AXIS
                )
        );

        painelTitulos.setOpaque(false);

        JLabel lblTitulo =
                new JLabel(
                        "Sistema de Controle de Estoque"
                );

        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(
                COR_PRIMARIA_ESCURA
        );

        lblTitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel lblSubtitulo =
                new JLabel(
                        "Gerenciamento de produtos, clientes e movimentações"
                );

        lblSubtitulo.setFont(
                FONTE_SUBTITULO
        );

        lblSubtitulo.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        lblSubtitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painelTitulos.add(lblTitulo);

        painelTitulos.add(
                Box.createVerticalStrut(4)
        );

        painelTitulos.add(lblSubtitulo);

        painelTopo.add(
                painelTitulos,
                BorderLayout.WEST
        );

        /*
         * Área do usuário logado.
         */
        JPanel painelUsuario =
                new JPanel();

        painelUsuario.setLayout(
                new BoxLayout(
                        painelUsuario,
                        BoxLayout.Y_AXIS
                )
        );

        painelUsuario.setBackground(
                COR_USUARIO
        );

        painelUsuario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        195,
                                        213,
                                        230
                                ),
                                1,
                                true
                        ),
                        new EmptyBorder(
                                9,
                                16,
                                9,
                                16
                        )
                )
        );

        lblUsuarioLogado =
                new JLabel();

        lblUsuarioLogado.setFont(
                FONTE_USUARIO
        );

        lblUsuarioLogado.setForeground(
                COR_PRIMARIA_ESCURA
        );

        lblUsuarioLogado.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        lblUsuarioLogado.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        lblPerfilUsuario =
                new JLabel();

        lblPerfilUsuario.setFont(
                FONTE_PERFIL
        );

        lblPerfilUsuario.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        lblPerfilUsuario.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        lblPerfilUsuario.setAlignmentX(
                Component.RIGHT_ALIGNMENT
        );

        painelUsuario.add(
                lblUsuarioLogado
        );

        painelUsuario.add(
                Box.createVerticalStrut(3)
        );

        painelUsuario.add(
                lblPerfilUsuario
        );

        painelTopo.add(
                painelUsuario,
                BorderLayout.EAST
        );

        contentPane.add(
                painelTopo,
                BorderLayout.NORTH
        );
    }

    /*
     * Cria a área central que receberá os painéis.
     */
    private void criarAreaConteudo() {

        painelConteudo =
                new JPanel(
                        new BorderLayout()
                );

        painelConteudo.setBackground(
                COR_FUNDO
        );

        painelConteudo.setBorder(
                new EmptyBorder(
                        14,
                        14,
                        14,
                        14
                )
        );

        contentPane.add(
                painelConteudo,
                BorderLayout.CENTER
        );
    }

    /*
     * Cria o rodapé.
     */
    private void criarRodape() {

        JPanel painelRodape =
                new JPanel(
                        new BorderLayout()
                );

        painelRodape.setBackground(
                COR_BRANCA
        );

        painelRodape.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                COR_BORDA
                        ),
                        new EmptyBorder(
                                8,
                                20,
                                8,
                                20
                        )
                )
        );

        lblStatus =
                new JLabel(
                        "Sistema pronto"
                );

        lblStatus.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblStatus.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        JLabel lblVersao =
                new JLabel(
                        "Versão 1.0"
                );

        lblVersao.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblVersao.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        painelRodape.add(
                lblStatus,
                BorderLayout.WEST
        );

        painelRodape.add(
                lblVersao,
                BorderLayout.EAST
        );

        contentPane.add(
                painelRodape,
                BorderLayout.SOUTH
        );
    }

    /*
     * Remove o painel atual e adiciona o novo.
     */
    private void carregarPainel(
            JPanel painel,
            String nomeTela
    ) {

        if (painel == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar o painel.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        painelConteudo.removeAll();

        painelConteudo.add(
                painel,
                BorderLayout.CENTER
        );

        painelConteudo.revalidate();
        painelConteudo.repaint();

        lblStatus.setText(
                "Tela atual: " + nomeTela
        );
    }

    /*
     * Exibe os dados do usuário autenticado.
     */
    private void exibirUsuarioLogado() {

        if (usuarioLogado != null) {

            lblUsuarioLogado.setText(
                    usuarioLogado.getNome()
            );

            lblPerfilUsuario.setText(
                    "Perfil: "
                            + usuarioLogado.getPerfil()
            );

        } else {

            lblUsuarioLogado.setText(
                    "Usuário não identificado"
            );

            lblPerfilUsuario.setText(
                    "Acesso de teste"
            );
        }
    }

    /*
     * Mensagem temporária para menus
     * que ainda não possuem painel.
     */
    private void exibirRecursoEmDesenvolvimento(
            String recurso
    ) {

        JOptionPane.showMessageDialog(
                this,
                recurso
                        + " ainda não foi implementado.",
                "Recurso em desenvolvimento",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /*
     * Confirma o fechamento do sistema.
     */
    private void sair() {

        int resposta =
                JOptionPane.showConfirmDialog(
                        this,
                        "Deseja realmente encerrar o sistema?",
                        "Confirmar saída",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (resposta
                == JOptionPane.YES_OPTION) {

            System.exit(0);
        }
    }
}