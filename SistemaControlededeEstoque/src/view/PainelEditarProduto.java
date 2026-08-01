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

public class PainelEditarProduto extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtValorCusto;
    private JTextField txtValorVenda;
    private JTextField txtQuantidade;
    private JTextField txtEstoqueMinimo;
    private JTextArea txtDescricao;

    private JComboBox<Categoria> comboCategoria;
    private JComboBox<Fornecedor> comboFornecedor;

    private int idProduto;
    private TelaPrincipal telaPrincipal;

    public PainelEditarProduto() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Produto");
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

        JLabel lblValorVenda = new JLabel("Valor de Venda");
        lblValorVenda.setBounds(550, 150, 150, 20);
        add(lblValorVenda);

        txtValorVenda = new JTextField();
        txtValorVenda.setBounds(550, 175, 120, 30);
        add(txtValorVenda);

        JLabel lblQuantidade = new JLabel("Quantidade");
        lblQuantidade.setBounds(80, 220, 150, 20);
        add(lblQuantidade);

        txtQuantidade = new JTextField();
        txtQuantidade.setBounds(80, 245, 120, 30);
        add(txtQuantidade);

        JLabel lblEstoqueMinimo = new JLabel("Estoque Mínimo");
        lblEstoqueMinimo.setBounds(220, 220, 150, 20);
        add(lblEstoqueMinimo);

        txtEstoqueMinimo = new JTextField();
        txtEstoqueMinimo.setBounds(220, 245, 120, 30);
        add(txtEstoqueMinimo);

        JLabel lblDescricao = new JLabel("Descrição");
        lblDescricao.setBounds(80, 295, 150, 20);
        add(lblDescricao);

        JScrollPane scrollDescricao = new JScrollPane();
        scrollDescricao.setBounds(80, 320, 590, 90);
        add(scrollDescricao);

        txtDescricao = new JTextArea();
        scrollDescricao.setViewportView(txtDescricao);

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            }
        });
        btnSalvar.setBounds(80, 440, 160, 35);
        add(btnSalvar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltar();
            }
        });
        btnVoltar.setBounds(260, 440, 120, 35);
        add(btnVoltar);

        carregarCategorias();
        carregarFornecedores();
    }

    public PainelEditarProduto(TelaPrincipal telaPrincipal, int idProduto) {
        this();
        this.telaPrincipal = telaPrincipal;
        this.idProduto = idProduto;
        carregarProduto();
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

    private void carregarProduto() {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = produtoDAO.buscarPorId(idProduto);

        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto não encontrado.");
            return;
        }

        txtNome.setText(produto.getNome());
        txtDescricao.setText(produto.getDescricao());
        txtValorCusto.setText(produto.getValorCusto().toString());
        txtValorVenda.setText(produto.getValorVenda().toString());
        txtQuantidade.setText(String.valueOf(produto.getQuantidadeEstoque()));
        txtEstoqueMinimo.setText(String.valueOf(produto.getEstoqueMinimo()));

        selecionarCategoria(produto.getIdCategoria());
        selecionarFornecedor(produto.getIdFornecedor());
    }

    private void selecionarCategoria(int idCategoria) {

        for (int i = 0; i < comboCategoria.getItemCount(); i++) {
            Categoria categoria = comboCategoria.getItemAt(i);

            if (categoria.getIdCategoria() == idCategoria) {
                comboCategoria.setSelectedIndex(i);
                return;
            }
        }
    }

    private void selecionarFornecedor(int idFornecedor) {

        for (int i = 0; i < comboFornecedor.getItemCount(); i++) {
            Fornecedor fornecedor = comboFornecedor.getItemAt(i);

            if (fornecedor.getIdFornecedor() == idFornecedor) {
                comboFornecedor.setSelectedIndex(i);
                return;
            }
        }
    }

    private void salvarAlteracoes() {

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

        if (comboCategoria.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria.");
            return;
        }

        if (comboFornecedor.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor.");
            return;
        }

        try {
            BigDecimal valorCusto = new BigDecimal(valorCustoTexto);
            BigDecimal valorVenda = new BigDecimal(valorVendaTexto);
            int quantidade = Integer.parseInt(quantidadeTexto);
            int estoqueMinimo = Integer.parseInt(estoqueMinimoTexto);

            if (valorCusto.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "O valor de custo não pode ser negativo.");
                return;
            }

            if (valorVenda.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "O valor de venda não pode ser negativo.");
                return;
            }

            if (quantidade < 0) {
                JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa.");
                return;
            }

            if (estoqueMinimo < 0) {
                JOptionPane.showMessageDialog(this, "O estoque mínimo não pode ser negativo.");
                return;
            }

            Categoria categoria = (Categoria) comboCategoria.getSelectedItem();
            Fornecedor fornecedor = (Fornecedor) comboFornecedor.getSelectedItem();

            Produto produto = new Produto();

            produto.setIdProduto(idProduto);
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setValorCusto(valorCusto);
            produto.setValorVenda(valorVenda);
            produto.setQuantidadeEstoque(quantidade);
            produto.setEstoqueMinimo(estoqueMinimo);
            produto.setIdCategoria(categoria.getIdCategoria());
            produto.setIdFornecedor(fornecedor.getIdFornecedor());

            ProdutoDAO produtoDAO = new ProdutoDAO();
            boolean atualizado = produtoDAO.atualizar(produto);

            if (atualizado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Produto atualizado com sucesso.",
                        "Atualização realizada",
                        JOptionPane.INFORMATION_MESSAGE
                );

                voltar();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Não foi possível atualizar o produto.",
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

    private void voltar() {

        if (telaPrincipal != null) {
            telaPrincipal.carregarPainel(new PainelListarProdutos(telaPrincipal));
        }
    }
}
