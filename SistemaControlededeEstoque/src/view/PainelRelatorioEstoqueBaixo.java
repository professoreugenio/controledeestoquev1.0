package view;

import java.awt.Font;
import java.awt.print.PrinterException;
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

public class PainelRelatorioEstoqueBaixo extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotal;

    public PainelRelatorioEstoqueBaixo() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Relatório de Estoque Baixo");
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

        lblTotal = new JLabel("Produtos com estoque baixo: 0");
        lblTotal.setBounds(70, 470, 350, 25);
        add(lblTotal);

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
        modeloTabela.addColumn("Estoque Atual");
        modeloTabela.addColumn("Estoque Mínimo");

        tabelaProdutos.setModel(modeloTabela);

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(220);
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(170);
        tabelaProdutos.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabelaProdutos.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabelaProdutos.getColumnModel().getColumn(6).setPreferredWidth(100);
    }

    private void carregarRelatorio() {

        RelatorioDAO relatorioDAO = new RelatorioDAO();
        List<Produto> produtos = relatorioDAO.listarProdutosComEstoqueBaixo();

        modeloTabela.setRowCount(0);

        for (Produto produto : produtos) {

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

        lblTotal.setText("Produtos com estoque baixo: " + produtos.size());
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
