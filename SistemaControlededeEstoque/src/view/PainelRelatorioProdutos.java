package view;

import java.awt.Font;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.RelatorioDAO;
import model.Produto;
import util.Formatador;

public class PainelRelatorioProdutos extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotalProdutos;
    private JLabel lblValorTotalEstoque;

    public PainelRelatorioProdutos() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Relatório de Produtos Cadastrados");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(230, 20, 500, 30);
        add(lblTitulo);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(70, 80, 120, 30);
        btnAtualizar.addActionListener(e -> carregarRelatorio());
        add(btnAtualizar);

        JButton btnImprimir = new JButton("Imprimir");
        btnImprimir.setBounds(210, 80, 120, 30);
        btnImprimir.addActionListener(e -> imprimirRelatorio());
        add(btnImprimir);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(70, 130, 850, 320);
        add(scrollPane);

        tabelaProdutos = new JTable();
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);
        scrollPane.setViewportView(tabelaProdutos);

        lblTotalProdutos = new JLabel("Total de produtos: 0");
        lblTotalProdutos.setBounds(70, 470, 250, 25);
        add(lblTotalProdutos);

        lblValorTotalEstoque = new JLabel("Valor total do estoque: R$ 0,00");
        lblValorTotalEstoque.setBounds(330, 470, 400, 25);
        add(lblValorTotalEstoque);

        configurarTabela();
        carregarRelatorio();
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
        modeloTabela.addColumn("Estoque");
        modeloTabela.addColumn("Est. Mínimo");

        tabelaProdutos.setModel(modeloTabela);

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(170);
        tabelaProdutos.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabelaProdutos.getColumnModel().getColumn(5).setPreferredWidth(80);
        tabelaProdutos.getColumnModel().getColumn(6).setPreferredWidth(90);
    }

    private void carregarRelatorio() {

        RelatorioDAO relatorioDAO = new RelatorioDAO();
        List<Produto> produtos = relatorioDAO.listarProdutosCadastrados();

        modeloTabela.setRowCount(0);

        BigDecimal valorTotalEstoque = BigDecimal.ZERO;

        for (Produto produto : produtos) {

            BigDecimal valorItemEstoque = produto.getValorVenda().multiply(
                    BigDecimal.valueOf(produto.getQuantidadeEstoque())
            );

            valorTotalEstoque = valorTotalEstoque.add(valorItemEstoque);

            modeloTabela.addRow(new Object[] {
                    produto.getIdProduto(),
                    produto.getNome(),
                    produto.getNomeCategoria(),
                    produto.getNomeFornecedor(),
                    Formatador.moeda(produto.getValorVenda()),
                    produto.getQuantidadeEstoque(),
                    produto.getEstoqueMinimo()
            });
        }

        lblTotalProdutos.setText("Total de produtos: " + produtos.size());
        lblValorTotalEstoque.setText(
                "Valor total do estoque: " + Formatador.moeda(valorTotalEstoque)
        );
    }

    private void imprimirRelatorio() {

        try {
            boolean impressaoConcluida = tabelaProdutos.print();

            if (impressaoConcluida) {
                JOptionPane.showMessageDialog(this, "Relatório enviado para impressão.");
            }

        } catch (PrinterException erro) {
            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao imprimir relatório: " + erro.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
