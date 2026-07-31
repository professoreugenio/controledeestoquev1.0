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

import dao.ProdutoDAO;
import model.Produto;
import util.Formatador;

public class PainelListarEstoque extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtPesquisa;
    private JTable tabelaEstoque;
    private DefaultTableModel modeloTabela;

    public PainelListarEstoque() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Listagem de Estoque");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(240, 20, 350, 30);
        add(lblTitulo);

        JLabel lblPesquisar = new JLabel("Pesquisar Produto");
        lblPesquisar.setBounds(50, 80, 150, 20);
        add(lblPesquisar);

        txtPesquisa = new JTextField();
        txtPesquisa.setBounds(50, 105, 330, 30);
        add(txtPesquisa);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarEstoque();
            }
        });
        btnPesquisar.setBounds(400, 105, 120, 30);
        add(btnPesquisar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarEstoque();
            }
        });
        btnAtualizar.setBounds(540, 105, 120, 30);
        add(btnAtualizar);

        JScrollPane scrollTabelaEstoque = new JScrollPane();
        scrollTabelaEstoque.setBounds(50, 160, 820, 330);
        add(scrollTabelaEstoque);

        tabelaEstoque = new JTable();
        tabelaEstoque.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaEstoque.getTableHeader().setReorderingAllowed(false);
        scrollTabelaEstoque.setViewportView(tabelaEstoque);

        configurarTabela();
        carregarEstoque();
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
        modeloTabela.addColumn("Produto");
        modeloTabela.addColumn("Categoria");
        modeloTabela.addColumn("Fornecedor");
        modeloTabela.addColumn("Valor Venda");
        modeloTabela.addColumn("Estoque Atual");
        modeloTabela.addColumn("Estoque Mínimo");
        modeloTabela.addColumn("Situação");

        tabelaEstoque.setModel(modeloTabela);

        tabelaEstoque.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaEstoque.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabelaEstoque.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelaEstoque.getColumnModel().getColumn(3).setPreferredWidth(140);
        tabelaEstoque.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabelaEstoque.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabelaEstoque.getColumnModel().getColumn(6).setPreferredWidth(90);
        tabelaEstoque.getColumnModel().getColumn(7).setPreferredWidth(110);
    }

    private void carregarEstoque() {

        modeloTabela.setRowCount(0);

        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listarEstoque();

        preencherTabela(produtos);

        txtPesquisa.setText("");
        txtPesquisa.requestFocus();
    }

    private void pesquisarEstoque() {

        String pesquisa = txtPesquisa.getText().trim();

        if (pesquisa.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite o nome do produto para pesquisar.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            txtPesquisa.requestFocus();
            return;
        }

        modeloTabela.setRowCount(0);

        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.pesquisarEstoquePorNome(pesquisa);

        preencherTabela(produtos);

        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nenhum produto encontrado.",
                    "Resultado da pesquisa",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void preencherTabela(List<Produto> produtos) {

        for (Produto produto : produtos) {

            String situacao = "Normal";

            if (produto.getQuantidadeEstoque() <= produto.getEstoqueMinimo()) {
                situacao = "Estoque Baixo";
            }

            modeloTabela.addRow(new Object[] {
                    produto.getIdProduto(),
                    produto.getNome(),
                    produto.getNomeCategoria(),
                    produto.getNomeFornecedor(),
                    Formatador.moeda(produto.getValorVenda()),
                    produto.getQuantidadeEstoque(),
                    produto.getEstoqueMinimo(),
                    situacao
            });
        }
    }
}
