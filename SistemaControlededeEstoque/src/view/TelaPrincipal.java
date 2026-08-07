package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import model.Usuario;
import util.PermissaoUtil;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    /*
     * Cores utilizadas na interface.
     */
    private static final Color COR_PRIMARIA = new Color(31, 78, 121);
    private static final Color COR_PRIMARIA_ESCURA = new Color(22, 55, 87);
    private static final Color COR_FUNDO = new Color(242, 245, 249);
    private static final Color COR_BRANCA = Color.WHITE;
    private static final Color COR_TEXTO = new Color(45, 55, 65);
    private static final Color COR_BORDA = new Color(210, 218, 226);

    private JPanel contentPane;
    private JPanel painelConteudo;
    private JLabel lblUsuarioLogado;

    private Usuario usuarioLogado;

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {
                    TelaPrincipal frame = new TelaPrincipal();
                    frame.setVisible(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaPrincipal() {
        this(null);
    }

    public TelaPrincipal(Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;

        configurarAparenciaDosComponentes();
        configurarJanela();
        criarBarraDeMenu();
        criarConteudoPrincipal();
        configurarFechamentoDaJanela();

        exibirUsuarioLogado();
        carregarPainel(new PainelDashboard());
    }

    /*
     * Configura propriedades gerais da janela.
     */
    private void configurarJanela() {

        setTitle("Sistema de Controle de Estoque");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));

        setLocationRelativeTo(null);
    }

    /*
     * Configura cores utilizadas na seleção dos menus.
     */
    private void configurarAparenciaDosComponentes() {

        UIManager.put("Menu.selectionBackground", COR_PRIMARIA);
        UIManager.put("Menu.selectionForeground", COR_BRANCA);

        UIManager.put("MenuItem.selectionBackground", COR_PRIMARIA);
        UIManager.put("MenuItem.selectionForeground", COR_BRANCA);

        UIManager.put("PopupMenu.border",
                new MatteBorder(1, 1, 1, 1, COR_BORDA));
    }

    /*
     * Criação da barra de menus.
     *
     * A estrutura original dos menus foi mantida.
     */
    private void criarBarraDeMenu() {

        JMenuBar menuBar = new JMenuBar();

        menuBar.setBackground(COR_BRANCA);
        menuBar.setPreferredSize(new Dimension(0, 42));
        menuBar.setBorder(
                new MatteBorder(0, 0, 1, 0, COR_BORDA));

        setJMenuBar(menuBar);

        /*
         * Menu Início
         */
        JMenu menuInicio = criarMenu("Início");
        menuBar.add(menuInicio);

        JMenuItem itemDashboard = criarItemMenu("Dashboard");

        itemDashboard.setAccelerator(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_D,
                        InputEvent.ALT_DOWN_MASK
                )
        );

        itemDashboard.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                carregarPainel(new PainelDashboard());
            }
        });

        menuInicio.add(itemDashboard);

        /*
         * Menu Produtos
         */
        JMenu menuProdutos = criarMenu("Produtos");
        menuBar.add(menuProdutos);

        JMenuItem itemNovoProduto = criarItemMenu("Novo");

        itemNovoProduto.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                abrirPainelSomenteAdministrador(
                        new PainelCadastrarProduto()
                );
            }
        });

        menuProdutos.add(itemNovoProduto);

        JMenuItem itemListarProdutos =
                criarItemMenu("Listar Produtos");

        itemListarProdutos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                carregarPainel(
                        new PainelListarProdutos(TelaPrincipal.this)
                );
            }
        });

        menuProdutos.add(itemListarProdutos);

        /*
         * Menu Clientes
         *
         * Exibido somente para administradores.
         */
        if (usuarioAdministrador()) {

            JMenu menuClientes = criarMenu("Clientes");
            menuBar.add(menuClientes);

            JMenuItem itemNovoCliente =
                    criarItemMenu("Novo");

            itemNovoCliente.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    abrirPainelSomenteAdministrador(
                            new PainelCadastrarCliente()
                    );
                }
            });

            menuClientes.add(itemNovoCliente);

            JMenuItem itemListarClientes =
                    criarItemMenu("Listar Clientes");

            itemListarClientes.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    abrirPainelSomenteAdministrador(
                            new PainelListarClientes(
                                    TelaPrincipal.this
                            )
                    );
                }
            });

            menuClientes.add(itemListarClientes);
        }
        
        
        /*
         * Menu Relatórios
         */
        JMenu menuRelatorios = criarMenu("Relatórios");
        menuBar.add(menuRelatorios);

        JMenuItem itemRelatorioProdutos =
                criarItemMenu("Produtos Cadastrados");

        itemRelatorioProdutos.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                carregarPainel(
                        new PainelRelatorioProdutos()
                );
            }
        });

        menuRelatorios.add(itemRelatorioProdutos);

        JMenuItem itemRelatorioEstoqueBaixo =
                criarItemMenu("Estoque Baixo");

        itemRelatorioEstoqueBaixo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                carregarPainel(
                        new PainelRelatorioEstoqueBaixo()
                );
            }
        });

        menuRelatorios.add(itemRelatorioEstoqueBaixo);

        
        /*
         * Menu Fornecedores
         *
         * Exibido somente para administradores.
         */
        if (usuarioAdministrador()) {

            JMenu menuFornecedores = criarMenu("Fornecedores");
            menuBar.add(menuFornecedores);

            JMenuItem itemNovoFornecedor =
                    criarItemMenu("Novo Fornecedor");

            itemNovoFornecedor.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    abrirPainelSomenteAdministrador(
                            new PainelNovoFornecedor()
                    );
                }
            });

            menuFornecedores.add(itemNovoFornecedor);

            JMenuItem itemListarFornecedores =
                    criarItemMenu("Listar Fornecedores");

            itemListarFornecedores.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    abrirPainelSomenteAdministrador(
                            new PainelListarFornecedores(
                                    TelaPrincipal.this
                            )
                    );
                }
            });

            menuFornecedores.add(itemListarFornecedores);
        }


        /*
         * Menu Estoque
         */
        JMenu menuEstoque = criarMenu("Estoque");
        menuBar.add(menuEstoque);

        JMenuItem itemListarEstoque =
                criarItemMenu("Exibir Estoque");

        itemListarEstoque.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                carregarPainel(new PainelListarEstoque());
            }
        });

        menuEstoque.add(itemListarEstoque);

        JMenuItem itemEntradaEstoque =
                criarItemMenu("Entrada no Estoque");

        itemEntradaEstoque.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                carregarPainel(new PainelEntradaEstoque());
            }
        });

        menuEstoque.add(itemEntradaEstoque);

        JMenuItem itemSaidaEstoque =
                criarItemMenu("Saída do Estoque");

        itemSaidaEstoque.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                carregarPainel(new PainelSaidaEstoque());
            }
        });

        menuEstoque.add(itemSaidaEstoque);

        /*
         * Menu Usuário
         *
         * Exibido somente para administradores.
         */
        if (usuarioAdministrador()) {

            JMenu menuUsuario = criarMenu("Usuário");
            menuBar.add(menuUsuario);

            JMenuItem itemEditarPerfil =
                    criarItemMenu("Editar Perfil");

            itemEditarPerfil.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    carregarPainel(
                            new PainelEditarPerfil(usuarioLogado)
                    );
                }
            });

            menuUsuario.add(itemEditarPerfil);

            JMenuItem itemNovoUsuario =
                    criarItemMenu("Novo Usuário");

            itemNovoUsuario.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    abrirPainelSomenteAdministrador(
                            new PainelNovoUsuario()
                    );
                }
            });

            menuUsuario.add(itemNovoUsuario);

            JMenuItem itemListarUsuarios =
                    criarItemMenu("Listar Usuários");

            itemListarUsuarios.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    abrirPainelSomenteAdministrador(
                            new PainelListarUsuarios()
                    );
                }
            });

            menuUsuario.add(itemListarUsuarios);
        }

        /*
         * Menu Sistema
         */
        JMenu menuSistema = criarMenu("Sistema");
        menuBar.add(menuSistema);

        JMenuItem itemSair = criarItemMenu("Sair");

        itemSair.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                sair();
            }
        });

        menuSistema.add(itemSair);
        
        
        /*
         * Menu Sobre
         */
        JMenu menuSobre = criarMenu("Sobre");
        menuBar.add(menuSobre);

        JMenuItem itemSobreSistema = criarItemMenu("Sobre o Sistema");

        itemSobreSistema.setAccelerator(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_F1,
                        0
                )
        );

        itemSobreSistema.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exibirSobreSistema();
            }
        });

        menuSobre.add(itemSobreSistema);

        
        
        
    }
    
    
    private void exibirSobreSistema() {

        String mensagem =
                "Sistema de Controle de Estoque\n\n" +
                "Versão: 1.0\n" +
                "Curso: Programador de Sistemas\n" +
                "Professor: Eugênio\n" +
                "Tecnologias utilizadas:\n" +
                "- Java\n" +
                "- Java Swing\n" +
                "- WindowBuilder\n" +
                "- JDBC\n" +
                "- MySQL\n\n" +
                "Sistema desenvolvido para fins didáticos.";

        JOptionPane.showMessageDialog(
                this,
                mensagem,
                "Sobre o Sistema",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    /*
     * Cria o painel principal da janela.
     */
    private void criarConteudoPrincipal() {

        contentPane = new JPanel();
        contentPane.setBackground(COR_FUNDO);
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout());

        setContentPane(contentPane);

        criarCabecalho();
        criarAreaDeConteudo();
    }

    /*
     * Cria o cabeçalho superior.
     */
    private void criarCabecalho() {

        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(COR_PRIMARIA);
        painelTopo.setBorder(
                new EmptyBorder(15, 22, 15, 22)
        );
        painelTopo.setLayout(new BorderLayout(20, 0));

        contentPane.add(painelTopo, BorderLayout.NORTH);

        /*
         * Painel do título e subtítulo.
         */
        JPanel painelTitulos = new JPanel();
        painelTitulos.setOpaque(false);
        painelTitulos.setLayout(new GridLayout(2, 1, 0, 3));

        JLabel lblTitulo =
                new JLabel("Sistema de Controle de Estoque");

        lblTitulo.setForeground(COR_BRANCA);
        lblTitulo.setFont(
                new Font("Segoe UI", Font.BOLD, 24)
        );

        JLabel lblSubtitulo =
                new JLabel("Gerenciamento de produtos, clientes e estoque");

        lblSubtitulo.setForeground(
                new Color(220, 230, 240)
        );

        lblSubtitulo.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        painelTitulos.add(lblTitulo);
        painelTitulos.add(lblSubtitulo);

        painelTopo.add(painelTitulos, BorderLayout.WEST);

        /*
         * Identificação do usuário.
         */
        lblUsuarioLogado = new JLabel();
        lblUsuarioLogado.setOpaque(true);
        lblUsuarioLogado.setBackground(COR_PRIMARIA_ESCURA);
        lblUsuarioLogado.setForeground(COR_BRANCA);

        lblUsuarioLogado.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        lblUsuarioLogado.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        lblUsuarioLogado.setBorder(
                new EmptyBorder(8, 15, 8, 15)
        );

        painelTopo.add(lblUsuarioLogado, BorderLayout.EAST);
    }

    /*
     * Cria a área que recebe os painéis internos.
     */
    private void criarAreaDeConteudo() {

        JPanel painelCentral = new JPanel();
        painelCentral.setBackground(COR_FUNDO);
        painelCentral.setBorder(
                new EmptyBorder(18, 18, 18, 18)
        );
        painelCentral.setLayout(new BorderLayout());

        contentPane.add(painelCentral, BorderLayout.CENTER);

        painelConteudo = new JPanel();
        painelConteudo.setBackground(COR_BRANCA);
        painelConteudo.setLayout(new BorderLayout());

        painelConteudo.setBorder(
                new MatteBorder(
                        1,
                        1,
                        1,
                        1,
                        COR_BORDA
                )
        );

        painelCentral.add(
                painelConteudo,
                BorderLayout.CENTER
        );
    }

    /*
     * Método auxiliar para padronizar os menus.
     */
    private JMenu criarMenu(String texto) {

        JMenu menuFornecedores = new JMenu(texto);

        menuFornecedores.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        menuFornecedores.setForeground(COR_TEXTO);
        menuFornecedores.setOpaque(false);

        menuFornecedores.setBorder(
                new EmptyBorder(0, 13, 0, 13)
        );

        return menuFornecedores;
    }

    /*
     * Método auxiliar para padronizar os itens dos menus.
     */
    private JMenuItem criarItemMenu(String texto) {

        JMenuItem item = new JMenuItem(texto);

        item.setFont(
                new Font("Segoe UI", Font.PLAIN, 14)
        );

        item.setForeground(COR_TEXTO);
        item.setBackground(COR_BRANCA);

        item.setBorder(
                new EmptyBorder(9, 18, 9, 18)
        );

        item.setPreferredSize(
                new Dimension(220, 38)
        );

        return item;
    }

    /*
     * Configura o fechamento da janela.
     */
    private void configurarFechamentoDaJanela() {

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSaidaParaLogin();
            }
        });
    }

    /*
     * Confirma a saída do sistema.
     */
    private void confirmarSaidaParaLogin() {

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja sair da tela principal e voltar para o login?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            voltarParaLogin();
        }
    }

    /*
     * Fecha a tela principal e retorna ao login.
     */
    private void voltarParaLogin() {

        fecharPaineis();

        TelaLogin telaLogin = new TelaLogin();
        telaLogin.setLocationRelativeTo(null);
        telaLogin.setVisible(true);

        dispose();
    }

    /*
     * Remove os painéis carregados.
     */
    private void fecharPaineis() {

        if (painelConteudo != null) {

            painelConteudo.removeAll();
            painelConteudo.revalidate();
            painelConteudo.repaint();
        }
    }

    /*
     * Carrega um JPanel dentro da área central.
     */
    public void carregarPainel(JPanel painel) {

        if (painel == null) {
            return;
        }

        painelConteudo.removeAll();
        painelConteudo.add(painel, BorderLayout.CENTER);
        painelConteudo.revalidate();
        painelConteudo.repaint();
    }

    /*
     * Verifica se o usuário possui perfil de administrador.
     *
     * A verificação de null evita NullPointerException.
     */
    public boolean usuarioAdministrador() {

        return usuarioLogado != null
                && PermissaoUtil.isAdministrador(usuarioLogado);
    }

    /*
     * Abre um painel restrito ao administrador.
     */
    private void abrirPainelSomenteAdministrador(JPanel painel) {

        if (!usuarioAdministrador()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Acesso permitido somente para ADMINISTRADOR.",
                    "Acesso negado",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        carregarPainel(painel);
    }

    /*
     * Exibe o usuário autenticado no cabeçalho.
     */
    private void exibirUsuarioLogado() {

        if (usuarioLogado != null) {

            String nome = usuarioLogado.getNome();
            String perfil = usuarioLogado.getPerfil();

            lblUsuarioLogado.setText(
                    "<html>"
                            + "<div style='text-align: right;'>"
                            + "<b>" + nome + "</b><br>"
                            + "Perfil: " + perfil
                            + "</div>"
                            + "</html>"
            );

        } else {

            lblUsuarioLogado.setText(
                    "<html>"
                            + "<div style='text-align: right;'>"
                            + "<b>Usuário não identificado</b><br>"
                            + "Acesso de demonstração"
                            + "</div>"
                            + "</html>"
            );
        }
    }

    private void sair() {
        confirmarSaidaParaLogin();
    }
}