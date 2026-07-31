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

public class PainelListarProdutos extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtPesquisa;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private TelaPrincipal telaPrincipal;

    public PainelListarProdutos() {
        this(null);
    }

    public PainelListarProdutos(TelaPrincipal telaPrincipal) {

        this.telaPrincipal = telaPrincipal;

        setLayout(null);

        JLabel lblTitulo = new JLabel("Listagem de Produtos");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(240, 20, 350, 30);
        add(lblTitulo);

        JLabel lblPesquisar = new JLabel("Pesquisar Produto");
        lblPesquisar.setBounds(50, 80, 150, 20);
        add(lblPesquisar);

        txtPesquisa = new JTextField();
        txtPesquisa.setBounds(50, 105, 300, 30);
        add(txtPesquisa);
        txtPesquisa.setColumns(10);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarProdutos();
            }
        });
        btnPesquisar.setBounds(370, 105, 110, 30);
        add(btnPesquisar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarProdutos();
            }
        });
        btnAtualizar.setBounds(490, 105, 110, 30);
        add(btnAtualizar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarProduto();
            }
        });
        btnEditar.setBounds(610, 105, 100, 30);
        add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirProduto();
            }
        });
        btnExcluir.setBounds(720, 105, 100, 30);
        add(btnExcluir);

        JScrollPane scrollTabelaProdutos = new JScrollPane();
        scrollTabelaProdutos.setBounds(50, 160, 780, 330);
        add(scrollTabelaProdutos);

        tabelaProdutos = new JTable();
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);
        scrollTabelaProdutos.setViewportView(tabelaProdutos);

        configurarTabela();
        carregarProdutos();
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
        modeloTabela.addColumn("Valor Custo");
        modeloTabela.addColumn("Valor Venda");
        modeloTabela.addColumn("Estoque");
        modeloTabela.addColumn("Est. Mínimo");

        tabelaProdutos.setModel(modeloTabela);

        tabelaProdutos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaProdutos.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabelaProdutos.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelaProdutos.getColumnModel().getColumn(3).setPreferredWidth(140);
        tabelaProdutos.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabelaProdutos.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabelaProdutos.getColumnModel().getColumn(6).setPreferredWidth(70);
        tabelaProdutos.getColumnModel().getColumn(7).setPreferredWidth(80);
    }

    private void carregarProdutos() {

        modeloTabela.setRowCount(0);

        ProdutoDAO produtoDAO = new ProdutoDAO();
        List<Produto> produtos = produtoDAO.listar();

        preencherTabela(produtos);

        txtPesquisa.setText("");
        txtPesquisa.requestFocus();
    }

    private void pesquisarProdutos() {

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
        List<Produto> produtos = produtoDAO.pesquisarPorNome(pesquisa);

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

            modeloTabela.addRow(new Object[] {
                    produto.getIdProduto(),
                    produto.getNome(),
                    produto.getNomeCategoria(),
                    produto.getNomeFornecedor(),
                    Formatador.moeda(produto.getValorCusto()),
                    Formatador.moeda(produto.getValorVenda()),
                    produto.getQuantidadeEstoque(),
                    produto.getEstoqueMinimo()
            });
        }
    }

    private int obterIdProdutoSelecionado() {

        int linhaSelecionada = tabelaProdutos.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um produto na tabela.",
                    "Nenhum produto selecionado",
                    JOptionPane.WARNING_MESSAGE
            );
            return -1;
        }

        int linhaModelo = tabelaProdutos.convertRowIndexToModel(linhaSelecionada);

        return Integer.parseInt(modeloTabela.getValueAt(linhaModelo, 0).toString());
    }

    private void editarProduto() {

        int idProduto = obterIdProdutoSelecionado();

        if (idProduto == -1) {
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

        telaPrincipal.carregarPainel(new PainelEditarProduto(telaPrincipal, idProduto));
    }

    private void excluirProduto() {

        int idProduto = obterIdProdutoSelecionado();

        if (idProduto == -1) {
            return;
        }

        String nomeProduto = modeloTabela.getValueAt(tabelaProdutos.getSelectedRow(), 1).toString();

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir o produto " + nomeProduto + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {

            ProdutoDAO produtoDAO = new ProdutoDAO();
            boolean excluido = produtoDAO.excluirLogico(idProduto);

            if (excluido) {
                JOptionPane.showMessageDialog(
                        this,
                        "Produto excluído com sucesso.",
                        "Exclusão realizada",
                        JOptionPane.INFORMATION_MESSAGE
                );

                carregarProdutos();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível excluir o produto.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
