package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import model.Usuario;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class TelaPrincipal extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel painelConteudo;
	private JLabel lblUsuarioLogado;
	private Usuario usuarioLogado;
	private TelaPrincipal telaPrincipal;
	
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
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
		setTitle("Sistema Controle de Estoque - Tela Principal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 650);
		setLocationRelativeTo(null);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu menuInicio = new JMenu("Início");
		menuBar.add(menuInicio);
		JMenuItem itemDashboard = new JMenuItem("Dashboard");
		itemDashboard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.ALT_DOWN_MASK));
		itemDashboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelDashboard());
			}
		});
		menuInicio.add(itemDashboard);
		JMenu menuProdutos = new JMenu("Produtos");
		menuBar.add(menuProdutos);
		JMenuItem itemNovoProduto = new JMenuItem("Novo");
		itemNovoProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelCadastrarProduto());
			}
		});
		menuProdutos.add(itemNovoProduto);
		JMenuItem itemListarProdutos = new JMenuItem("Listar Produtos");
		itemListarProdutos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				carregarPainel(new PainelListarProdutos(TelaPrincipal.this));
			}
		});
		menuProdutos.add(itemListarProdutos);
		JMenu menuClientes = new JMenu("Clientes");
		menuBar.add(menuClientes);
		JMenuItem itemNovoCliente = new JMenuItem("Novo");
		itemNovoCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelCadastrarCliente());
			}
		});
		menuClientes.add(itemNovoCliente);
		JMenuItem itemListarClientes = new JMenuItem("Listar Clientes");
		itemListarClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 carregarPainel(new PainelListarClientes(TelaPrincipal.this));
			}
		});
		menuClientes.add(itemListarClientes);
		JMenu menuEstoque = new JMenu("Estoque");
		menuBar.add(menuEstoque);
		JMenuItem itemListarEstoque = new JMenuItem("Estoque");
		itemListarEstoque.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelListarEstoque());
			}
		});
		menuEstoque.add(itemListarEstoque);
		JMenuItem itemEntradaEstoque = new JMenuItem("Entrada");
		itemEntradaEstoque.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelEntradaEstoque());
			}
		});
		menuEstoque.add(itemEntradaEstoque);
		JMenuItem itemSaidaEstoque = new JMenuItem("Saída");
		itemSaidaEstoque.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelSaidaEstoque());
			}
		});
		menuEstoque.add(itemSaidaEstoque);
		JMenu mnNewMenu = new JMenu("Usuário");
		menuBar.add(mnNewMenu);
		JMenuItem mntmNewMenuItem = new JMenuItem("Editar Perfil");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelEditarPerfil(usuarioLogado));
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Novo Usuário");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelNovoUsuario());
			}
		});
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Listar Usuários");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarPainel(new PainelListarUsuarios());
			}
		});
		mnNewMenu.add(mntmNewMenuItem_2);
		JMenu menuSistema = new JMenu("Sistema");
		menuBar.add(menuSistema);
		JMenuItem itemSair = new JMenuItem("Sair");
		itemSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sair();
			}
		});
		menuSistema.add(itemSair);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		JPanel painelTopo = new JPanel();
		contentPane.add(painelTopo, BorderLayout.NORTH);
		painelTopo.setLayout(new BorderLayout(0, 0));
		JLabel lblTitulo = new JLabel("Sistema de Controle de Estoque");
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
		painelTopo.add(lblTitulo, BorderLayout.WEST);
		lblUsuarioLogado = new JLabel();
		lblUsuarioLogado.setHorizontalAlignment(SwingConstants.RIGHT);
		painelTopo.add(lblUsuarioLogado, BorderLayout.EAST);
		painelConteudo = new JPanel();
		contentPane.add(painelConteudo, BorderLayout.CENTER);
		painelConteudo.setLayout(new BorderLayout(0, 0));
		exibirUsuarioLogado();
		carregarPainel(new PainelDashboard());
	}

	public void carregarPainel(JPanel painel) {
		painelConteudo.removeAll();
		painelConteudo.add(painel, BorderLayout.CENTER);
		painelConteudo.revalidate();
		painelConteudo.repaint();
	}

	private void exibirUsuarioLogado() {
		if (usuarioLogado != null) {
			lblUsuarioLogado.setText("Usuário: " + usuarioLogado.getNome() + " | Perfil: " + usuarioLogado.getPerfil());
		} else {
			lblUsuarioLogado.setText("Usuário não identificado");
		}
	}

	private void sair() {
		int resposta = JOptionPane.showConfirmDialog(this, "Deseja realmente sair do sistema?", "Confirmar saída",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (resposta == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}
}