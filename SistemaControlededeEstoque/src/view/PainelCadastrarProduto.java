package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.CategoriaDAO;
import dao.FornecedorDAO;
import dao.ProdutoDAO;
import model.Categoria;
import model.Fornecedor;
import model.Produto;

public class PainelCadastrarProduto extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtValorCusto;
    private JTextField txtValorVenda;
    private JTextField txtQuantidade;
    private JTextField txtEstoqueMinimo;
    private JTextArea txtDescricao;

    private JComboBox<Categoria> comboCategoria;
    private JComboBox<Fornecedor> comboFornecedor;

    public PainelCadastrarProduto() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Produtos");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(230, 20, 350, 30);
        add(lblTitulo);

        JLabel lblNome = new JLabel("Nome do Produto");
        lblNome.setBounds(80, 80, 150, 20);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(80, 105, 300, 30);
        add(txtNome);
        txtNome.setColumns(10);

        JLabel lblCategoria = new JLabel("Categoria");
        lblCategoria.setBounds(420, 80, 150, 20);
        add(lblCategoria);

        comboCategoria = new JComboBox<Categoria>();
        comboCategoria.setBounds(420, 105, 250, 30);
        add(comboCategoria);

        JLabel lblFornecedor = new JLabel("Fornecedor");
        lblFornecedor.setBounds(80, 150, 150, 20);
        add(lblFornecedor);

        comboFornecedor = new JComboBox<Fornecedor>();
        comboFornecedor.setBounds(80, 175, 300, 30);
        add(comboFornecedor);

        JLabel lblValorCusto = new JLabel("Valor de Custo");
        lblValorCusto.setBounds(420, 150, 150, 20);
        add(lblValorCusto);

        txtValorCusto = new JTextField();
        txtValorCusto.setBounds(420, 175, 120, 30);
        add(txtValorCusto);
        txtValorCusto.setColumns(10);

        JLabel lblValorVenda = new JLabel("Valor de Venda");
        lblValorVenda.setBounds(550, 150, 150, 20);
        add(lblValorVenda);

        txtValorVenda = new JTextField();
        txtValorVenda.setBounds(550, 175, 120, 30);
        add(txtValorVenda);
        txtValorVenda.setColumns(10);

        JLabel lblQuantidade = new JLabel("Quantidade");
        lblQuantidade.setBounds(80, 220, 150, 20);
        add(lblQuantidade);

        txtQuantidade = new JTextField();
        txtQuantidade.setBounds(80, 245, 120, 30);
        add(txtQuantidade);
        txtQuantidade.setColumns(10);

        JLabel lblEstoqueMinimo = new JLabel("Estoque Mínimo");
        lblEstoqueMinimo.setBounds(220, 220, 150, 20);
        add(lblEstoqueMinimo);

        txtEstoqueMinimo = new JTextField();
        txtEstoqueMinimo.setBounds(220, 245, 120, 30);
        add(txtEstoqueMinimo);
        txtEstoqueMinimo.setColumns(10);

        JLabel lblDescricao = new JLabel("Descrição");
        lblDescricao.setBounds(80, 295, 150, 20);
        add(lblDescricao);

        JScrollPane scrollDescricao = new JScrollPane();
        scrollDescricao.setBounds(80, 320, 590, 90);
        add(scrollDescricao);

        txtDescricao = new JTextArea();
        scrollDescricao.setViewportView(txtDescricao);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarProduto();
            }
        });
        btnSalvar.setBounds(80, 440, 120, 35);
        add(btnSalvar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        btnLimpar.setBounds(220, 440, 120, 35);
        add(btnLimpar);

        carregarCategorias();
        carregarFornecedores();
    }

    private void carregarCategorias() {

        CategoriaDAO categoriaDAO = new CategoriaDAO();

        comboCategoria.removeAllItems();

        for (Categoria categoria : categoriaDAO.listar()) {
            comboCategoria.addItem(categoria);
        }
    }

    private void carregarFornecedores() {

        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        comboFornecedor.removeAllItems();

        for (Fornecedor fornecedor : fornecedorDAO.listar()) {
            comboFornecedor.addItem(fornecedor);
        }
    }

    private void salvarProduto() {

        String nome = txtNome.getText().trim();
        String descricao = txtDescricao.getText().trim();
        String valorCustoTexto = txtValorCusto.getText().trim().replace(",", ".");
        String valorVendaTexto = txtValorVenda.getText().trim().replace(",", ".");
        String quantidadeTexto = txtQuantidade.getText().trim();
        String estoqueMinimoTexto = txtEstoqueMinimo.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do produto.");
            txtNome.requestFocus();
            return;
        }

        if (valorCustoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o valor de custo.");
            txtValorCusto.requestFocus();
            return;
        }

        if (valorVendaTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o valor de venda.");
            txtValorVenda.requestFocus();
            return;
        }

        if (quantidadeTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade em estoque.");
            txtQuantidade.requestFocus();
            return;
        }

        if (estoqueMinimoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o estoque mínimo.");
            txtEstoqueMinimo.requestFocus();
            return;
        }

        if (comboCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Cadastre ou selecione uma categoria.");
            return;
        }

        if (comboFornecedor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Cadastre ou selecione um fornecedor.");
            return;
        }

        try {

            BigDecimal valorCusto = new BigDecimal(valorCustoTexto);
            BigDecimal valorVenda = new BigDecimal(valorVendaTexto);
            int quantidade = Integer.parseInt(quantidadeTexto);
            int estoqueMinimo = Integer.parseInt(estoqueMinimoTexto);

            if (valorCusto.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "O valor de custo não pode ser negativo.");
                txtValorCusto.requestFocus();
                return;
            }

            if (valorVenda.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "O valor de venda não pode ser negativo.");
                txtValorVenda.requestFocus();
                return;
            }

            if (quantidade < 0) {
                JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa.");
                txtQuantidade.requestFocus();
                return;
            }

            if (estoqueMinimo < 0) {
                JOptionPane.showMessageDialog(this, "O estoque mínimo não pode ser negativo.");
                txtEstoqueMinimo.requestFocus();
                return;
            }

            Categoria categoriaSelecionada = (Categoria) comboCategoria.getSelectedItem();
            Fornecedor fornecedorSelecionado = (Fornecedor) comboFornecedor.getSelectedItem();

            Produto produto = new Produto(
                    nome,
                    descricao,
                    valorCusto,
                    valorVenda,
                    quantidade,
                    estoqueMinimo,
                    categoriaSelecionada.getIdCategoria(),
                    fornecedorSelecionado.getIdFornecedor()
            );

            ProdutoDAO produtoDAO = new ProdutoDAO();
            boolean cadastrado = produtoDAO.cadastrar(produto);

            if (cadastrado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Produto cadastrado com sucesso.",
                        "Cadastro realizado",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limparCampos();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível cadastrar o produto.",
                        "Erro",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite valores numéricos válidos.",
                    "Erro de conversão",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtValorCusto.setText("");
        txtValorVenda.setText("");
        txtQuantidade.setText("");
        txtEstoqueMinimo.setText("");

        if (comboCategoria.getItemCount() > 0) {
            comboCategoria.setSelectedIndex(0);
        }

        if (comboFornecedor.getItemCount() > 0) {
            comboFornecedor.setSelectedIndex(0);
        }

        txtNome.requestFocus();
    }
}
