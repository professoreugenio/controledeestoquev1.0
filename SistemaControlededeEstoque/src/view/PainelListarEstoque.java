package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
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

import com.toedter.calendar.JDateChooser;

import dao.MovimentacaoEstoqueDAO;
import model.MovimentacaoEstoque;
import util.Formatador;

public class PainelListarEstoque extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNotaFiscal;
    private JDateChooser dateChooserData;
    private JTable tabelaMovimentacoes;
    private DefaultTableModel modeloTabela;

    public PainelListarEstoque() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Movimentações de Estoque");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(240, 20, 400, 30);
        add(lblTitulo);

        JLabel lblNotaFiscal = new JLabel("Nº Nota Fiscal");
        lblNotaFiscal.setBounds(50, 80, 120, 20);
        add(lblNotaFiscal);

        txtNotaFiscal = new JTextField();
        txtNotaFiscal.setBounds(50, 105, 200, 30);
        add(txtNotaFiscal);

        JLabel lblData = new JLabel("Data");
        lblData.setBounds(280, 80, 100, 20);
        add(lblData);

        dateChooserData = new JDateChooser();
        dateChooserData.setDateFormatString("dd/MM/yyyy");
        dateChooserData.setBounds(280, 105, 150, 30);
        add(dateChooserData);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarMovimentacoes();
            }
        });
        btnPesquisar.setBounds(460, 105, 120, 30);
        add(btnPesquisar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarMovimentacoes();
            }
        });
        btnAtualizar.setBounds(600, 105, 120, 30);
        add(btnAtualizar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparPesquisa();
            }
        });
        btnLimpar.setBounds(740, 105, 120, 30);
        add(btnLimpar);

        JScrollPane scrollTabela = new JScrollPane();
        scrollTabela.setBounds(50, 160, 860, 340);
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
        modeloTabela.addColumn("Observação");
        modeloTabela.addColumn("Data");

        tabelaMovimentacoes.setModel(modeloTabela);

        tabelaMovimentacoes.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaMovimentacoes.getColumnModel().getColumn(1).setPreferredWidth(160);
        tabelaMovimentacoes.getColumnModel().getColumn(2).setPreferredWidth(80);
        tabelaMovimentacoes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tabelaMovimentacoes.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabelaMovimentacoes.getColumnModel().getColumn(5).setPreferredWidth(100);
        tabelaMovimentacoes.getColumnModel().getColumn(6).setPreferredWidth(180);
        tabelaMovimentacoes.getColumnModel().getColumn(7).setPreferredWidth(120);
    }

    private void carregarMovimentacoes() {

        modeloTabela.setRowCount(0);

        MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
        List<MovimentacaoEstoque> movimentacoes = dao.listarMovimentacoes();

        preencherTabela(movimentacoes);

        txtNotaFiscal.setText("");
        dateChooserData.setDate(null);
        txtNotaFiscal.requestFocus();
    }

    private void pesquisarMovimentacoes() {

        String nrNotaFiscal = txtNotaFiscal.getText().trim();
        java.util.Date dataUtil = dateChooserData.getDate();

        if (nrNotaFiscal.isEmpty() && dataUtil == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o número da nota fiscal ou selecione uma data.",
                    "Pesquisa",
                    JOptionPane.WARNING_MESSAGE
            );
            txtNotaFiscal.requestFocus();
            return;
        }

        MovimentacaoEstoqueDAO dao = new MovimentacaoEstoqueDAO();
        List<MovimentacaoEstoque> movimentacoes;

        if (!nrNotaFiscal.isEmpty() && dataUtil != null) {

            Date dataSql = new Date(dataUtil.getTime());
            movimentacoes = dao.pesquisarPorNotaFiscalEData(nrNotaFiscal, dataSql);

        } else if (!nrNotaFiscal.isEmpty()) {

            movimentacoes = dao.pesquisarPorNotaFiscal(nrNotaFiscal);

        } else {

            Date dataSql = new Date(dataUtil.getTime());
            movimentacoes = dao.pesquisarPorData(dataSql);
        }

        modeloTabela.setRowCount(0);
        preencherTabela(movimentacoes);

        if (movimentacoes.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nenhuma movimentação encontrada.",
                    "Resultado da pesquisa",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void preencherTabela(List<MovimentacaoEstoque> movimentacoes) {

        for (MovimentacaoEstoque movimentacao : movimentacoes) {

            modeloTabela.addRow(new Object[] {
                    movimentacao.getIdMovimentacao(),
                    movimentacao.getNomeProduto(),
                    movimentacao.getTipo(),
                    movimentacao.getNrNotaFiscal(),
                    movimentacao.getQuantidade(),
                    Formatador.moeda(movimentacao.getValorUnitario()),
                    movimentacao.getObservacao(),
                    movimentacao.getCriadoEmFormatado()
            });
        }
    }

    private void limparPesquisa() {
        txtNotaFiscal.setText("");
        dateChooserData.setDate(null);
        carregarMovimentacoes();
    }
}
