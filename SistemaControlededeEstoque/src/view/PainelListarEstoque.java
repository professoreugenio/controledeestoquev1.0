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

import dao.MovimentacaoEstoqueDAO;
import model.MovimentacaoEstoque;
import util.Formatador;

public class PainelListarEstoque extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNotaFiscalPesquisa;
    private JTextField txtDataPesquisa;
    private JTable tabelaMovimentacoes;
    private DefaultTableModel modeloTabela;

    public PainelListarEstoque() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Histórico de Movimentações de Estoque");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(190, 20, 520, 30);
        add(lblTitulo);

        JLabel lblNotaFiscal = new JLabel("Pesquisar Nº Nota Fiscal");
        lblNotaFiscal.setBounds(50, 80, 180, 20);
        add(lblNotaFiscal);

        txtNotaFiscalPesquisa = new JTextField();
        txtNotaFiscalPesquisa.setBounds(50, 105, 200, 30);
        add(txtNotaFiscalPesquisa);

        JButton btnPesquisarNota = new JButton("Pesquisar NF");
        btnPesquisarNota.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarPorNotaFiscal();
            }
        });
        btnPesquisarNota.setBounds(260, 105, 130, 30);
        add(btnPesquisarNota);

        JLabel lblData = new JLabel("Pesquisar Data");
        lblData.setBounds(420, 80, 150, 20);
        add(lblData);

        txtDataPesquisa = new JTextField();
        txtDataPesquisa.setToolTipText("Digite no formato yyyy-MM-dd");
        txtDataPesquisa.setBounds(420, 105, 150, 30);
        add(txtDataPesquisa);

        JButton btnPesquisarData = new JButton("Pesquisar Data");
        btnPesquisarData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarPorData();
            }
        });
        btnPesquisarData.setBounds(580, 105, 140, 30);
        add(btnPesquisarData);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarMovimentacoes();
            }
        });
        btnAtualizar.setBounds(740, 105, 120, 30);
        add(btnAtualizar);

        JScrollPane scrollTabela = new JScrollPane();
        scrollTabela.setBounds(50, 160, 850, 350);
        add(scrollTabela);

        tabelaMovimentacoes = new JTable();
        tabelaMovimentacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaMovimentacoes.getTableHeader().setReorderingAllowed(false);
        scrollTabela.setViewportView(tabelaMovimentacoes);

        configurarTabela();
        carregarMovimentacoes();
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
        modeloTabela.addColumn("Tipo");
        modeloTabela.addColumn("Nº Nota Fiscal");
        modeloTabela.addColumn("Quantidade");
        modeloTabela.addColumn("Valor Unitário");
        modeloTabela.addColumn("Data");
        modeloTabela.addColumn("Observação");

        tabelaMovimentacoes.setModel(modeloTabela);

        tabelaMovimentacoes.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaMovimentacoes.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabelaMovimentacoes.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabelaMovimentacoes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaMovimentacoes.getColumnModel().getColumn(4).setPreferredWidth(80);
        tabelaMovimentacoes.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabelaMovimentacoes.getColumnModel().getColumn(6).setPreferredWidth(130);
        tabelaMovimentacoes.getColumnModel().getColumn(7).setPreferredWidth(200);
    }

    private void carregarMovimentacoes() {

        modeloTabela.setRowCount(0);

        MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
        List<MovimentacaoEstoque> movimentacoes = dao.listarMovimentacoes();

        preencherTabela(movimentacoes);

        txtNotaFiscalPesquisa.setText("");
        txtDataPesquisa.setText("");
        txtNotaFiscalPesquisa.requestFocus();
    }

    private void pesquisarPorNotaFiscal() {

        String notaFiscal = txtNotaFiscalPesquisa.getText().trim();

        if (notaFiscal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o número da nota fiscal.");
            txtNotaFiscalPesquisa.requestFocus();
            return;
        }

        modeloTabela.setRowCount(0);

        MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
        List<MovimentacaoEstoque> movimentacoes = dao.pesquisarPorNotaFiscal(notaFiscal);

        preencherTabela(movimentacoes);

        if (movimentacoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma movimentação encontrada para esta nota fiscal.");
        }
    }

    private void pesquisarPorData() {

        String data = txtDataPesquisa.getText().trim();

        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite a data no formato yyyy-MM-dd.");
            txtDataPesquisa.requestFocus();
            return;
        }

        if (!data.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Formato inválido. Use yyyy-MM-dd. Exemplo: 2026-08-02");
            txtDataPesquisa.requestFocus();
            return;
        }

        modeloTabela.setRowCount(0);

        MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
        List<MovimentacaoEstoque> movimentacoes = dao.pesquisarPorData(data);

        preencherTabela(movimentacoes);

        if (movimentacoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma movimentação encontrada para esta data.");
        }
    }

    private void preencherTabela(List<MovimentacaoEstoque> movimentacoes) {

        for (MovimentacaoEstoque mov : movimentacoes) {

            modeloTabela.addRow(new Object[] {
                    mov.getIdMovimentacao(),
                    mov.getNomeProduto(),
                    mov.getTipo(),
                    mov.getNrNotaFiscal(),
                    mov.getQuantidade(),
                    Formatador.moeda(mov.getValorUnitario()),
                    mov.getCriadoEm(),
                    mov.getObservacao()
            });
        }
    }
}
