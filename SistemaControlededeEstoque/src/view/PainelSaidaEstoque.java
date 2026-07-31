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

import dao.MovimentacaoEstoqueDAO;
import dao.ProdutoDAO;
import model.MovimentacaoEstoque;
import model.Produto;

public class PainelSaidaEstoque extends JPanel {

    private static final long serialVersionUID = 1L;

    private JComboBox<Produto> comboProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextArea txtObservacao;
    private JLabel lblEstoqueAtual;

    public PainelSaidaEstoque() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Saída de Estoque");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(230, 20, 350, 30);
        add(lblTitulo);

        JLabel lblProduto = new JLabel("Produto");
        lblProduto.setBounds(80, 90, 100, 20);
        add(lblProduto);

        comboProduto = new JComboBox<Produto>();
        comboProduto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarEstoqueAtual();
            }
        });
        comboProduto.setBounds(80, 115, 420, 30);
        add(comboProduto);

        lblEstoqueAtual = new JLabel("Estoque atual: 0");
        lblEstoqueAtual.setBounds(520, 115, 180, 30);
        add(lblEstoqueAtual);

        JLabel lblQuantidade = new JLabel("Quantidade");
        lblQuantidade.setBounds(80, 165, 120, 20);
        add(lblQuantidade);

        txtQuantidade = new JTextField();
        txtQuantidade.setBounds(80, 190, 150, 30);
        add(txtQuantidade);

        JLabel lblValorUnitario = new JLabel("Valor Unitário");
        lblValorUnitario.setBounds(260, 165, 120, 20);
        add(lblValorUnitario);

        txtValorUnitario = new JTextField();
        txtValorUnitario.setBounds(260, 190, 150, 30);
        add(txtValorUnitario);

        JLabel lblObservacao = new JLabel("Observação");
        lblObservacao.setBounds(80, 240, 120, 20);
        add(lblObservacao);

        JScrollPane scrollObservacao = new JScrollPane();
        scrollObservacao.setBounds(80, 265, 500, 90);
        add(scrollObservacao);

        txtObservacao = new JTextArea();
        scrollObservacao.setViewportView(txtObservacao);

        JButton btnRegistrar = new JButton("Registrar Saída");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarSaida();
            }
        });
        btnRegistrar.setBounds(80, 390, 160, 35);
        add(btnRegistrar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        btnLimpar.setBounds(260, 390, 120, 35);
        add(btnLimpar);

        carregarProdutos();
    }

    private void carregarProdutos() {

        ProdutoDAO produtoDAO = new ProdutoDAO();

        comboProduto.removeAllItems();

        for (Produto produto : produtoDAO.listarParaCombo()) {
            comboProduto.addItem(produto);
        }

        mostrarEstoqueAtual();
    }

    private void mostrarEstoqueAtual() {

        Produto produtoSelecionado = (Produto) comboProduto.getSelectedItem();

        if (produtoSelecionado == null) {
            lblEstoqueAtual.setText("Estoque atual: 0");
            return;
        }

        ProdutoDAO produtoDAO = new ProdutoDAO();
        int estoqueAtual = produtoDAO.buscarEstoqueAtual(produtoSelecionado.getIdProduto());

        lblEstoqueAtual.setText("Estoque atual: " + estoqueAtual);
    }

    private void registrarSaida() {

        Produto produtoSelecionado = (Produto) comboProduto.getSelectedItem();

        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }

        String quantidadeTexto = txtQuantidade.getText().trim();
        String valorTexto = txtValorUnitario.getText().trim().replace(",", ".");
        String observacao = txtObservacao.getText().trim();

        if (quantidadeTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a quantidade.");
            txtQuantidade.requestFocus();
            return;
        }

        if (valorTexto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o valor unitário.");
            txtValorUnitario.requestFocus();
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeTexto);
            BigDecimal valorUnitario = new BigDecimal(valorTexto);

            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
                txtQuantidade.requestFocus();
                return;
            }

            int estoqueAtual = new ProdutoDAO().buscarEstoqueAtual(produtoSelecionado.getIdProduto());

            if (quantidade > estoqueAtual) {
                JOptionPane.showMessageDialog(
                        this,
                        "Quantidade insuficiente em estoque.\nEstoque atual: " + estoqueAtual,
                        "Estoque insuficiente",
                        JOptionPane.WARNING_MESSAGE
                );
                txtQuantidade.requestFocus();
                return;
            }

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                    produtoSelecionado.getIdProduto(),
                    "SAIDA",
                    quantidade,
                    valorUnitario,
                    observacao
            );

            MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
            boolean registrado = dao.registrarSaida(movimentacao);

            if (registrado) {
                JOptionPane.showMessageDialog(this, "Saída registrada com sucesso.");
                limparCampos();
                mostrarEstoqueAtual();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível registrar a saída.");
            }

        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Digite quantidade e valor válidos.");
        }
    }

    private void limparCampos() {
        txtQuantidade.setText("");
        txtValorUnitario.setText("");
        txtObservacao.setText("");

        if (comboProduto.getItemCount() > 0) {
            comboProduto.setSelectedIndex(0);
        }

        mostrarEstoqueAtual();
        txtQuantidade.requestFocus();
    }
}
