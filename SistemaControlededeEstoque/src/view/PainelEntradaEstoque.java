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

public class PainelEntradaEstoque extends JPanel {

    private static final long serialVersionUID = 1L;

    private JComboBox<Produto> comboProduto;
    private JTextField txtQuantidade;
    private JTextField txtValorUnitario;
    private JTextField txtNrNotaFiscal;
    private JTextArea txtObservacao;

    public PainelEntradaEstoque() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Entrada de Estoque");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(230, 20, 350, 30);
        add(lblTitulo);

        JLabel lblProduto = new JLabel("Produto");
        lblProduto.setBounds(80, 90, 100, 20);
        add(lblProduto);

        comboProduto = new JComboBox<Produto>();
        comboProduto.setBounds(80, 115, 420, 30);
        add(comboProduto);

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

        JLabel lblNrNotaFiscal = new JLabel("Nº Nota Fiscal");
        lblNrNotaFiscal.setBounds(440, 165, 120, 20);
        add(lblNrNotaFiscal);

        txtNrNotaFiscal = new JTextField();
        txtNrNotaFiscal.setBounds(440, 190, 160, 30);
        add(txtNrNotaFiscal);

        JLabel lblObservacao = new JLabel("Observação");
        lblObservacao.setBounds(80, 240, 120, 20);
        add(lblObservacao);

        JScrollPane scrollObservacao = new JScrollPane();
        scrollObservacao.setBounds(80, 265, 500, 90);
        add(scrollObservacao);

        txtObservacao = new JTextArea();
        scrollObservacao.setViewportView(txtObservacao);

        JButton btnRegistrar = new JButton("Registrar Entrada");
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registrarEntrada();
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
    }

    private void registrarEntrada() {

        Produto produtoSelecionado = (Produto) comboProduto.getSelectedItem();

        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto.");
            return;
        }

        String quantidadeTexto = txtQuantidade.getText().trim();
        String valorTexto = txtValorUnitario.getText().trim().replace(",", ".");
        String nrNotaFiscal = txtNrNotaFiscal.getText().trim();
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

        if (nrNotaFiscal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o número da nota fiscal.");
            txtNrNotaFiscal.requestFocus();
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

            if (valorUnitario.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "O valor unitário não pode ser negativo.");
                txtValorUnitario.requestFocus();
                return;
            }

            MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                    produtoSelecionado.getIdProduto(),
                    "ENTRADA",
                    nrNotaFiscal,
                    quantidade,
                    valorUnitario,
                    observacao
            );

            MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
            boolean registrado = dao.registrarEntrada(movimentacao);

            if (registrado) {
                JOptionPane.showMessageDialog(this, "Entrada registrada com sucesso.");
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível registrar a entrada.");
            }

        } catch (NumberFormatException erro) {
            JOptionPane.showMessageDialog(this, "Digite quantidade e valor válidos.");
        }
    }

    private void limparCampos() {
        txtQuantidade.setText("");
        txtValorUnitario.setText("");
        txtNrNotaFiscal.setText("");
        txtObservacao.setText("");

        if (comboProduto.getItemCount() > 0) {
            comboProduto.setSelectedIndex(0);
        }

        txtQuantidade.requestFocus();
    }
}
