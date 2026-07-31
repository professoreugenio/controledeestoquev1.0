package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import dao.ClienteDAO;
import model.Cliente;

public class PainelListarClientes extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtPesquisa;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    private TelaPrincipal telaPrincipal;

    public PainelListarClientes() {
        this(null);
    }

    public PainelListarClientes(TelaPrincipal telaPrincipal) {

        this.telaPrincipal = telaPrincipal;

        setLayout(null);

        JLabel lblTitulo = new JLabel("Listagem de Clientes");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(240, 20, 350, 30);
        add(lblTitulo);

        JLabel lblPesquisar = new JLabel("Pesquisar Cliente");
        lblPesquisar.setBounds(50, 80, 150, 20);
        add(lblPesquisar);

        txtPesquisa = new JTextField();
        txtPesquisa.setBounds(50, 105, 300, 30);
        add(txtPesquisa);
        txtPesquisa.setColumns(10);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarClientes();
            }
        });
        btnPesquisar.setBounds(370, 105, 110, 30);
        add(btnPesquisar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarClientes();
            }
        });
        btnAtualizar.setBounds(490, 105, 110, 30);
        add(btnAtualizar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarCliente();
            }
        });
        btnEditar.setBounds(610, 105, 100, 30);
        add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirCliente();
            }
        });
        btnExcluir.setBounds(720, 105, 100, 30);
        add(btnExcluir);

        JScrollPane scrollTabelaClientes = new JScrollPane();
        scrollTabelaClientes.setBounds(50, 160, 780, 330);
        add(scrollTabelaClientes);

        tabelaClientes = new JTable();
        tabelaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaClientes.getTableHeader().setReorderingAllowed(false);
        scrollTabelaClientes.setViewportView(tabelaClientes);

        configurarTabela();
        carregarClientes();
    }

    private void configurarTabela() {

        modeloTabela = new DefaultTableModel() {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("CPF");
        modeloTabela.addColumn("Telefone");
        modeloTabela.addColumn("E-mail");
        modeloTabela.addColumn("Cidade");

        tabelaClientes.setModel(modeloTabela);

        tabelaClientes.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaClientes.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabelaClientes.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelaClientes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaClientes.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabelaClientes.getColumnModel().getColumn(5).setPreferredWidth(120);
    }

    private void carregarClientes() {

        modeloTabela.setRowCount(0);

        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.listar();

        preencherTabela(clientes);

        txtPesquisa.setText("");
        txtPesquisa.requestFocus();
    }

    private void pesquisarClientes() {

        String pesquisa = txtPesquisa.getText().trim();

        if (pesquisa.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite o nome do cliente para pesquisar.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            txtPesquisa.requestFocus();
            return;
        }

        modeloTabela.setRowCount(0);

        ClienteDAO clienteDAO = new ClienteDAO();
        List<Cliente> clientes = clienteDAO.pesquisarPorNome(pesquisa);

        preencherTabela(clientes);

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum cliente encontrado.",
                    "Resultado da pesquisa",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void preencherTabela(List<Cliente> clientes) {

        for (Cliente cliente : clientes) {

            modeloTabela.addRow(new Object[] {
                    cliente.getIdCliente(),
                    cliente.getNome(),
                    cliente.getCpf(),
                    cliente.getTelefone(),
                    cliente.getEmail(),
                    cliente.getCidade()
            });
        }
    }

    private int obterIdClienteSelecionado() {

        int linhaSelecionada = tabelaClientes.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um cliente na tabela.",
                    "Nenhum cliente selecionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return -1;
        }

        int linhaModelo = tabelaClientes.convertRowIndexToModel(linhaSelecionada);

        return Integer.parseInt(modeloTabela.getValueAt(linhaModelo, 0).toString());
    }

    private String obterNomeClienteSelecionado() {

        int linhaSelecionada = tabelaClientes.getSelectedRow();

        if (linhaSelecionada == -1) {
            return "";
        }

        int linhaModelo = tabelaClientes.convertRowIndexToModel(linhaSelecionada);

        return modeloTabela.getValueAt(linhaModelo, 1).toString();
    }

    private void editarCliente() {

        int idCliente = obterIdClienteSelecionado();

        if (idCliente == -1) {
            return;
        }

        if (telaPrincipal == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível abrir a tela de edição.\nPainel principal não identificado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        telaPrincipal.carregarPainel(new PainelEditarCliente(telaPrincipal, idCliente));
    }

    private void excluirCliente() {

        int idCliente = obterIdClienteSelecionado();

        if (idCliente == -1) {
            return;
        }

        String nomeCliente = obterNomeClienteSelecionado();

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir o cliente " + nomeCliente + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {

            ClienteDAO clienteDAO = new ClienteDAO();
            boolean excluido = clienteDAO.excluirLogico(idCliente);

            if (excluido) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cliente excluído com sucesso.",
                        "Exclusão realizada",
                        JOptionPane.INFORMATION_MESSAGE
                );

                carregarClientes();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o cliente.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
