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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.FornecedorDAO;
import model.Fornecedor;

public class PainelListarFornecedores extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtPesquisa;
    private JTable tabelaFornecedores;
    private DefaultTableModel modeloTabela;

    private TelaPrincipal telaPrincipal;

    public PainelListarFornecedores() {
        this(null);
    }

    public PainelListarFornecedores(TelaPrincipal telaPrincipal) {

        this.telaPrincipal = telaPrincipal;

        setLayout(null);

        JLabel lblTitulo = new JLabel("Listagem de Fornecedores");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(220, 20, 420, 30);
        add(lblTitulo);

        JLabel lblPesquisar = new JLabel("Pesquisar Fornecedor");
        lblPesquisar.setBounds(50, 80, 180, 20);
        add(lblPesquisar);

        txtPesquisa = new JTextField();
        txtPesquisa.setBounds(50, 105, 430, 30);
        add(txtPesquisa);
        txtPesquisa.setColumns(10);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarFornecedor();
            }
        });
        btnPesquisar.setBounds(495, 105, 110, 30);
        add(btnPesquisar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarFornecedores();
            }
        });
        btnAtualizar.setBounds(615, 105, 100, 30);
        add(btnAtualizar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarFornecedor();
            }
        });
        btnEditar.setBounds(725, 105, 90, 30);
        add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirFornecedor();
            }
        });
        btnExcluir.setBounds(825, 105, 90, 30);
        add(btnExcluir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 160, 865, 320);
        add(scrollPane);

        tabelaFornecedores = new JTable();
        tabelaFornecedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaFornecedores.getTableHeader().setReorderingAllowed(false);
        scrollPane.setViewportView(tabelaFornecedores);

        configurarTabela();
        carregarFornecedores();
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
        modeloTabela.addColumn("CNPJ");
        modeloTabela.addColumn("Telefone");
        modeloTabela.addColumn("E-mail");
        modeloTabela.addColumn("Cidade");
        modeloTabela.addColumn("Status");

        tabelaFornecedores.setModel(modeloTabela);

        tabelaFornecedores.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaFornecedores.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabelaFornecedores.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaFornecedores.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaFornecedores.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabelaFornecedores.getColumnModel().getColumn(5).setPreferredWidth(140);
        tabelaFornecedores.getColumnModel().getColumn(6).setPreferredWidth(80);
    }

    private void carregarFornecedores() {

        FornecedorDAO fornecedorDAO = new FornecedorDAO();
        List<Fornecedor> fornecedores = fornecedorDAO.listar();

        preencherTabela(fornecedores);
    }

    private void pesquisarFornecedor() {

        String pesquisa = txtPesquisa.getText().trim();

        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        List<Fornecedor> fornecedores;

        if (pesquisa.isEmpty()) {
            fornecedores = fornecedorDAO.listar();
        } else {
            fornecedores = fornecedorDAO.pesquisarPorNome(pesquisa);
        }

        preencherTabela(fornecedores);
    }

    private void preencherTabela(List<Fornecedor> fornecedores) {

        modeloTabela.setRowCount(0);

        for (Fornecedor fornecedor : fornecedores) {

            String status = fornecedor.isAtivo() ? "Ativo" : "Inativo";

            modeloTabela.addRow(new Object[] {
                    fornecedor.getIdFornecedor(),
                    fornecedor.getNome(),
                    fornecedor.getCnpj(),
                    fornecedor.getTelefone(),
                    fornecedor.getEmail(),
                    fornecedor.getCidade(),
                    status
            });
        }
    }

    private int obterIdFornecedorSelecionado() {

        int linhaSelecionada = tabelaFornecedores.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um fornecedor.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );
            return -1;
        }

        int linhaModelo = tabelaFornecedores.convertRowIndexToModel(linhaSelecionada);

        Object valorId = modeloTabela.getValueAt(linhaModelo, 0);

        return Integer.parseInt(valorId.toString());
    }

    private void editarFornecedor() {

        int idFornecedor = obterIdFornecedorSelecionado();

        if (idFornecedor == -1) {
            return;
        }

        if (telaPrincipal != null) {
            telaPrincipal.carregarPainel(new PainelEditarFornecedor(telaPrincipal, idFornecedor));
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Tela principal não encontrada.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirFornecedor() {

        int idFornecedor = obterIdFornecedorSelecionado();

        if (idFornecedor == -1) {
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este fornecedor?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {

            FornecedorDAO fornecedorDAO = new FornecedorDAO();

            boolean excluido = fornecedorDAO.excluirLogico(idFornecedor);

            if (excluido) {
                JOptionPane.showMessageDialog(
                        this,
                        "Fornecedor excluído com sucesso.",
                        "Exclusão realizada",
                        JOptionPane.INFORMATION_MESSAGE
                );

                carregarFornecedores();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o fornecedor.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}