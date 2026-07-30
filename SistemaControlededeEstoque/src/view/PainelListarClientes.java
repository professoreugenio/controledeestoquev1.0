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

    public PainelListarClientes() {
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
        txtPesquisa.setBounds(50, 105, 330, 30);
        add(txtPesquisa);
        txtPesquisa.setColumns(10);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarClientes();
            }
        });
        btnPesquisar.setBounds(400, 105, 120, 30);
        add(btnPesquisar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarClientes();
            }
        });
        btnAtualizar.setBounds(540, 105, 120, 30);
        add(btnAtualizar);

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
}
