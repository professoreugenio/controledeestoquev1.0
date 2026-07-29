package view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.awt.Rectangle;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    /*
     * Cores principais da aplicação
     */
    private static final Color COR_PRIMARIA = new Color(31, 78, 121);
    private static final Color COR_PRIMARIA_ESCURA = new Color(24, 59, 91);

    private static final Color COR_FUNDO = new Color(244, 246, 249);
    private static final Color COR_BRANCA = Color.WHITE;
    private static final Color COR_TEXTO = new Color(50, 50, 50);
    private static final Color COR_TEXTO_SECUNDARIO = new Color(110, 110, 110);
    private static final Color COR_BORDA = new Color(220, 224, 230);
    private static final Color COR_USUARIO = new Color(230, 239, 248);

    /*
     * Fontes principais
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
            new Font("Segoe UI", Font.PLAIN, 13);

    private JPanel contentPane;
    private JPanel painelConteudo;

    private JLabel lblUsuarioLogado;
    private JLabel lblStatus;

    private Usuario usuarioLogado;

    /*
     * Inicia a aplicação
     */
    public static void main(String[] args) {

        aplicarAparencia();

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {

                    TelaPrincipal frame = new TelaPrincipal();
                    frame.setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();

                    JOptionPane.showMessageDialog(
                            null,
                            "Não foi possível iniciar o sistema.",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
    }

    /*
     * Aplica o tema Nimbus ao sistema
     */
    private static void aplicarAparencia() {

        try {

            for (UIManager.LookAndFeelInfo tema
                    : UIManager.getInstalledLookAndFeels()) {

                if ("Nimbus".equals(tema.getName())) {

                    UIManager.setLookAndFeel(tema.getClassName());
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
     * Construtor utilizado para testes
     */
    public TelaPrincipal() {

        this(null);
    }

    /*
     * Construtor que recebe o usuário logado
     */
    public TelaPrincipal(Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        configurarJanela();
        setJMenuBar(criarBarraMenu());
        montarInterface();

        exibirUsuarioLogado();
        PainelDashboard painelDashboard = new PainelDashboard();
        carregarPainel(painelDashboard);
    }

    /*
     * Configurações gerais da janela
     */
    private void configurarJanela() {

        setTitle("Sistema de Controle de Estoque");

        /*
         * Impede o fechamento direto para que seja exibida
         * a confirmação de saída.
         */
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setSize(1100, 700);

        /*
         * Evita que a janela fique pequena demais.
         */
        setMinimumSize(new Dimension(900, 600));

        /*
         * Centraliza a janela na tela.
         */
        setLocationRelativeTo(null);

        /*
         * Exibe uma confirmação quando o usuário clica no X.
         */
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {

                sair();
            }
        });
    }

    /*
     * Cria a barra de menus
     */
    private JMenuBar criarBarraMenu() {

        JMenuBar menuBar = new JMenuBar();

        menuBar.setBackground(COR_PRIMARIA);
        menuBar.setOpaque(true);

        menuBar.setBorder(
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        );

        /*
         * Menu Início
         */
        JMenu menuInicio = criarMenu("Início");

        menuInicio.add(
                criarItemMenu(
                        "Dashboard",
                        e -> carregarPainel(new PainelDashboard())
                )
        );

        menuBar.add(menuInicio);

        /*
         * Menu Produtos
         */
        JMenu menuProdutos = criarMenu("Produtos");

        menuProdutos.add(
                criarItemMenu(
                        "Cadastrar produto",
                        e -> carregarPainel(
                                new PainelCadastrarProduto()
                        )
                )
        );

        menuProdutos.add(
                criarItemMenu(
                        "Listar produtos",
                        e -> carregarPainel(
                                new PainelListarProdutos()
                        )
                )
        );

        menuBar.add(menuProdutos);

        /*
         * Menu Clientes
         */
        JMenu menuClientes = criarMenu("Clientes");

        menuClientes.add(
                criarItemMenu(
                        "Cadastrar cliente",
                        e -> carregarPainel(
                                new PainelCadastrarCliente()
                        )
                )
        );

        menuClientes.add(
                criarItemMenu(
                        "Listar clientes",
                        e -> carregarPainel(
                                new PainelListarClientes()
                        )
                )
        );

        menuBar.add(menuClientes);

        /*
         * Menu Estoque
         */
        JMenu menuEstoque = criarMenu("Estoque");

        menuEstoque.add(
                criarItemMenu(
                        "Consultar estoque",
                        e -> carregarPainel(
                                new PainelListarEstoque()
                        )
                )
        );

        menuEstoque.addSeparator();

        menuEstoque.add(
                criarItemMenu(
                        "Entrada de estoque",
                        e -> carregarPainel(
                                new PainelEntradaEstoque()
                        )
                )
        );

        menuEstoque.add(
                criarItemMenu(
                        "Saída de estoque",
                        e -> carregarPainel(
                                new PainelSaidaEstoque()
                        )
                )
        );

        menuBar.add(menuEstoque);

        /*
         * Empurra o menu Sistema para o lado direito.
         */
        menuBar.add(Box.createHorizontalGlue());

        /*
         * Menu Sistema
         */
        JMenu menuSistema = criarMenu("Sistema");

        JMenuItem itemSair = criarItemMenu(
                "Encerrar sistema",
                e -> sair()
        );

        itemSair.setForeground(new Color(180, 40, 40));

        menuSistema.add(itemSair);

        menuBar.add(menuSistema);

        return menuBar;
    }

    /*
     * Configuração visual dos menus principais
     */
    private JMenu criarMenu(String texto) {

        JMenu menu = new JMenu(texto);

        menu.setFont(FONTE_MENU);
        menu.setForeground(COR_BRANCA);
        menu.setBackground(COR_PRIMARIA);
        menu.setOpaque(true);

        menu.setMargin(
                new Insets(7, 14, 7, 14)
        );

        return menu;
    }

    /*
     * Cria os itens dos menus evitando código repetido
     */
    private JMenuItem criarItemMenu(
            String texto,
            ActionListener acao
    ) {

        JMenuItem item = new JMenuItem(texto);

        item.setFont(FONTE_ITEM_MENU);
        item.setForeground(COR_TEXTO);
        item.setBackground(COR_BRANCA);
        item.setOpaque(true);

        item.setBorder(
                new EmptyBorder(8, 15, 8, 15)
        );

        item.addActionListener(acao);

        return item;
    }

    /*
     * Monta a estrutura visual principal
     */
    private void montarInterface() {

        contentPane = new JPanel();

        contentPane.setLayout(
                new BorderLayout(0, 0)
        );

        contentPane.setBackground(COR_FUNDO);

        setContentPane(contentPane);

        criarCabecalho();
        criarAreaConteudo();
        criarRodape();
    }

    /*
     * Cria o cabeçalho da aplicação
     */
    private void criarCabecalho() {

        JPanel painelTopo = new JPanel();

        painelTopo.setLayout(
                new BorderLayout(20, 0)
        );

        painelTopo.setBackground(COR_BRANCA);

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
                                18,
                                24,
                                18,
                                24
                        )
                )
        );

        contentPane.add(
                painelTopo,
                BorderLayout.NORTH
        );

        /*
         * Área do título e subtítulo
         */
        JPanel painelTitulos = new JPanel();

        painelTitulos.setOpaque(false);

        painelTitulos.setLayout(
                new BoxLayout(
                        painelTitulos,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel lblTitulo = new JLabel(
                "Sistema de Controle de Estoque"
        );

        lblTitulo.setFont(FONTE_TITULO);
        lblTitulo.setForeground(COR_PRIMARIA_ESCURA);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel(
                "Gerenciamento de produtos, clientes e movimentações"
        );

        lblSubtitulo.setFont(FONTE_SUBTITULO);
        lblSubtitulo.setForeground(COR_TEXTO_SECUNDARIO);
        lblSubtitulo.setAlignmentX(LEFT_ALIGNMENT);

        painelTitulos.add(lblTitulo);
        painelTitulos.add(Box.createVerticalStrut(4));
        painelTitulos.add(lblSubtitulo);

        painelTopo.add(
                painelTitulos,
                BorderLayout.WEST
        );

        /*
         * Área de identificação do usuário
         */
        JPanel painelUsuario = new JPanel(
                new BorderLayout()
        );

        painelUsuario.setBackground(COR_USUARIO);

        painelUsuario.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(190, 210, 230),
                                1,
                                true
                        ),
                        new EmptyBorder(
                                10,
                                15,
                                10,
                                15
                        )
                )
        );

        lblUsuarioLogado = new JLabel();

        lblUsuarioLogado.setFont(FONTE_USUARIO);
        lblUsuarioLogado.setForeground(COR_PRIMARIA_ESCURA);
        lblUsuarioLogado.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        painelUsuario.add(
                lblUsuarioLogado,
                BorderLayout.CENTER
        );

        painelTopo.add(
                painelUsuario,
                BorderLayout.EAST
        );
    }

    /*
     * Cria a área que receberá os outros painéis
     */
    private void criarAreaConteudo() {

        painelConteudo = new JPanel();

        painelConteudo.setLayout(
                new BorderLayout()
        );

        painelConteudo.setBackground(COR_FUNDO);

        painelConteudo.setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        contentPane.add(
                painelConteudo,
                BorderLayout.CENTER
        );
    }

    /*
     * Cria o rodapé da aplicação
     */
    private void criarRodape() {

        JPanel painelRodape = new JPanel(
                new BorderLayout()
        );

        painelRodape.setBackground(COR_BRANCA);

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

        lblStatus = new JLabel(
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

        JLabel lblVersao = new JLabel(
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
     * Remove o painel atual e adiciona o novo painel
     */
    private void carregarPainel(JPanel painel) {

        if (painel == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "O painel informado não é válido.",
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

        atualizarStatusPainel(painel);
    }

    /*
     * Atualiza o nome do painel exibido no rodapé
     */
    private void atualizarStatusPainel(JPanel painel) {

        String nomePainel =
                painel.getClass().getSimpleName();

        nomePainel = nomePainel.replace(
                "Painel",
                ""
        );

        /*
         * Separa palavras escritas em CamelCase.
         *
         * Exemplo:
         * ListarProdutos passa a ser Listar Produtos.
         */
        nomePainel = nomePainel.replaceAll(
                "([a-z])([A-Z])",
                "$1 $2"
        );

        lblStatus.setText(
                "Tela atual: " + nomePainel
        );
    }

    /*
     * Exibe os dados do usuário logado
     */
    private void exibirUsuarioLogado() {

        if (usuarioLogado != null) {

            lblUsuarioLogado.setText(
                    "Usuário: "
                            + usuarioLogado.getNome()
                            + "  |  Perfil: "
                            + usuarioLogado.getPerfil()
            );

        } else {

            lblUsuarioLogado.setText(
                    "Usuário não identificado"
            );
        }
    }

    /*
     * Confirma a saída do sistema
     */
    private void sair() {

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente encerrar o sistema?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {

            dispose();
        }
    }
}