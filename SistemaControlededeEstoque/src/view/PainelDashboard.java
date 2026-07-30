package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.DashboardDAO;
import model.Produto;
import util.Formatador;

public class PainelDashboard extends JPanel {

    private static final long serialVersionUID = 1L;

    /*
     * Cores principais do painel
     */
    private static final Color COR_PRIMARIA =
            new Color(31, 78, 121);

    private static final Color COR_PRIMARIA_ESCURA =
            new Color(23, 57, 88);

    private static final Color COR_FUNDO =
            new Color(244, 246, 249);

    private static final Color COR_BRANCA =
            Color.WHITE;

    private static final Color COR_TEXTO =
            new Color(55, 55, 55);

    private static final Color COR_TEXTO_SECUNDARIO =
            new Color(110, 110, 110);

    private static final Color COR_BORDA =
            new Color(220, 224, 230);

    private static final Color COR_LINHA_ALTERNADA =
            new Color(247, 249, 252);

    private static final Color COR_PRODUTOS =
            new Color(0, 123, 255);

    private static final Color COR_CLIENTES =
            new Color(40, 167, 69);

    private static final Color COR_ESTOQUE_BAIXO =
            new Color(220, 53, 69);

    private static final Color COR_VALOR_ESTOQUE =
            new Color(111, 66, 193);

    /*
     * Componentes que exibirão os indicadores
     */
    private JLabel lblTotalProdutos;
    private JLabel lblTotalClientes;
    private JLabel lblEstoqueBaixo;
    private JLabel lblValorEstoque;

    private JLabel lblUltimaAtualizacao;

    private JTable tabelaEstoqueBaixo;
    private DefaultTableModel modeloTabela;

    private JButton btnAtualizar;

    public PainelDashboard() {

        configurarPainel();
        criarInterface();
        configurarTabela();
        carregarDashboard();
    }

    /*
     * Configurações gerais do painel
     */
    private void configurarPainel() {

        setLayout(
                new BorderLayout(0, 20)
        );

        setBackground(COR_FUNDO);

        setBorder(
                new EmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );
    }

    /*
     * Monta a interface principal
     */
    private void criarInterface() {

        add(
                criarCabecalho(),
                BorderLayout.NORTH
        );

        JPanel painelCentral = new JPanel(
                new BorderLayout(0, 18)
        );

        painelCentral.setOpaque(false);

        painelCentral.add(
                criarPainelCards(),
                BorderLayout.NORTH
        );

        painelCentral.add(
                criarPainelTabela(),
                BorderLayout.CENTER
        );

        add(
                painelCentral,
                BorderLayout.CENTER
        );
    }

    /*
     * Cria o cabeçalho do Dashboard
     */
    private JPanel criarCabecalho() {

        JPanel painelCabecalho = new JPanel(
                new BorderLayout(20, 0)
        );

        painelCabecalho.setOpaque(false);

        /*
         * Área do título
         */
        JPanel painelTitulos = new JPanel();

        painelTitulos.setLayout(
                new BoxLayout(
                        painelTitulos,
                        BoxLayout.Y_AXIS
                )
        );

        painelTitulos.setOpaque(false);

        JLabel lblTitulo = new JLabel(
                "Dashboard"
        );

        lblTitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        lblTitulo.setForeground(
                COR_PRIMARIA_ESCURA
        );

        lblTitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel lblSubtitulo = new JLabel(
                "Visão geral do sistema de controle de estoque"
        );

        lblSubtitulo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        lblSubtitulo.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        lblSubtitulo.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        painelTitulos.add(lblTitulo);
        painelTitulos.add(
                Box.createVerticalStrut(4)
        );
        painelTitulos.add(lblSubtitulo);

        painelCabecalho.add(
                painelTitulos,
                BorderLayout.WEST
        );

        /*
         * Botão de atualização
         */
        JPanel painelBotao = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT,
                        0,
                        0
                )
        );

        painelBotao.setOpaque(false);

        btnAtualizar = new JButton(
                "Atualizar Dashboard"
        );

        estilizarBotaoAtualizar();

        btnAtualizar.addActionListener(
                e -> carregarDashboard()
        );

        painelBotao.add(btnAtualizar);

        painelCabecalho.add(
                painelBotao,
                BorderLayout.EAST
        );

        return painelCabecalho;
    }

    /*
     * Cria a área com os quatro cards
     */
    private JPanel criarPainelCards() {

        JPanel painelCards = new JPanel(
                new GridLayout(
                        1,
                        4,
                        15,
                        0
                )
        );

        painelCards.setOpaque(false);

        painelCards.setPreferredSize(
                new Dimension(0, 125)
        );

        lblTotalProdutos = criarLabelValor();

        painelCards.add(
                criarCard(
                        "Produtos ativos",
                        lblTotalProdutos,
                        "Total cadastrado no sistema",
                        COR_PRODUTOS
                )
        );

        lblTotalClientes = criarLabelValor();

        painelCards.add(
                criarCard(
                        "Clientes ativos",
                        lblTotalClientes,
                        "Clientes disponíveis",
                        COR_CLIENTES
                )
        );

        lblEstoqueBaixo = criarLabelValor();

        painelCards.add(
                criarCard(
                        "Estoque baixo",
                        lblEstoqueBaixo,
                        "Produtos que precisam de atenção",
                        COR_ESTOQUE_BAIXO
                )
        );

        lblValorEstoque = criarLabelValor();

        painelCards.add(
                criarCard(
                        "Valor do estoque",
                        lblValorEstoque,
                        "Valor total dos produtos",
                        COR_VALOR_ESTOQUE
                )
        );

        return painelCards;
    }

    /*
     * Cria um card de indicador
     */
    private JPanel criarCard(
            String titulo,
            JLabel lblValor,
            String descricao,
            Color corDestaque
    ) {

        JPanel card = new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBackground(COR_BRANCA);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(
                                0,
                                5,
                                0,
                                0,
                                corDestaque
                        ),
                        BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(
                                        COR_BORDA,
                                        1,
                                        true
                                ),
                                new EmptyBorder(
                                        15,
                                        18,
                                        15,
                                        18
                                )
                        )
                )
        );

        JLabel lblTituloCard = new JLabel(
                titulo
        );

        lblTituloCard.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        lblTituloCard.setForeground(
                COR_TEXTO
        );

        lblTituloCard.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        lblValor.setForeground(
                corDestaque
        );

        JLabel lblDescricao = new JLabel(
                descricao
        );

        lblDescricao.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        lblDescricao.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        lblDescricao.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(lblTituloCard);
        card.add(Box.createVerticalStrut(8));
        card.add(lblValor);
        card.add(Box.createVerticalStrut(4));
        card.add(lblDescricao);

        return card;
    }

    /*
     * Cria o JLabel que receberá o valor do card
     */
    private JLabel criarLabelValor() {

        JLabel label = new JLabel("0");

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        return label;
    }

    /*
     * Cria o painel que contém a tabela
     */
    private JPanel criarPainelTabela() {

        JPanel painelTabela = new JPanel(
                new BorderLayout(0, 12)
        );

        painelTabela.setBackground(
                COR_BRANCA
        );

        painelTabela.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                COR_BORDA,
                                1,
                                true
                        ),
                        new EmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );

        /*
         * Cabeçalho da tabela
         */
        JPanel painelTituloTabela = new JPanel(
                new BorderLayout()
        );

        painelTituloTabela.setOpaque(false);

        JLabel lblTabela = new JLabel(
                "Produtos com estoque baixo"
        );

        lblTabela.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        lblTabela.setForeground(
                COR_PRIMARIA_ESCURA
        );

        painelTituloTabela.add(
                lblTabela,
                BorderLayout.WEST
        );

        lblUltimaAtualizacao = new JLabel(
                "Aguardando atualização"
        );

        lblUltimaAtualizacao.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblUltimaAtualizacao.setForeground(
                COR_TEXTO_SECUNDARIO
        );

        lblUltimaAtualizacao.setHorizontalAlignment(
                SwingConstants.RIGHT
        );

        painelTituloTabela.add(
                lblUltimaAtualizacao,
                BorderLayout.EAST
        );

        painelTabela.add(
                painelTituloTabela,
                BorderLayout.NORTH
        );

        /*
         * JTable
         */
        tabelaEstoqueBaixo = new JTable();

        tabelaEstoqueBaixo.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabelaEstoqueBaixo.setRowHeight(32);

        tabelaEstoqueBaixo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        tabelaEstoqueBaixo.setForeground(
                COR_TEXTO
        );

        tabelaEstoqueBaixo.setSelectionBackground(
                new Color(218, 232, 247)
        );

        tabelaEstoqueBaixo.setSelectionForeground(
                COR_PRIMARIA_ESCURA
        );

        tabelaEstoqueBaixo.setShowVerticalLines(false);
        tabelaEstoqueBaixo.setShowHorizontalLines(false);

        tabelaEstoqueBaixo.setIntercellSpacing(
                new Dimension(0, 0)
        );

        tabelaEstoqueBaixo.setFillsViewportHeight(true);

        tabelaEstoqueBaixo.setAutoCreateRowSorter(true);

        tabelaEstoqueBaixo
                .getTableHeader()
                .setReorderingAllowed(false);

        tabelaEstoqueBaixo
                .getTableHeader()
                .setPreferredSize(
                        new Dimension(0, 38)
                );

        JScrollPane scrollTabela = new JScrollPane(
                tabelaEstoqueBaixo
        );

        scrollTabela.setBorder(
                BorderFactory.createLineBorder(
                        COR_BORDA
                )
        );

        scrollTabela.getViewport().setBackground(
                COR_BRANCA
        );

        painelTabela.add(
                scrollTabela,
                BorderLayout.CENTER
        );

        return painelTabela;
    }

    /*
     * Configuração visual do botão atualizar
     */
    private void estilizarBotaoAtualizar() {

        btnAtualizar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        btnAtualizar.setForeground(
                COR_BRANCA
        );

        btnAtualizar.setBackground(
                COR_PRIMARIA
        );

        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setOpaque(true);

        btnAtualizar.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        btnAtualizar.setBorder(
                new EmptyBorder(
                        10,
                        18,
                        10,
                        18
                )
        );

        /*
         * Efeito visual quando o mouse passa no botão
         */
        btnAtualizar.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        if (btnAtualizar.isEnabled()) {

                            btnAtualizar.setBackground(
                                    COR_PRIMARIA_ESCURA
                            );
                        }
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        if (btnAtualizar.isEnabled()) {

                            btnAtualizar.setBackground(
                                    COR_PRIMARIA
                            );
                        }
                    }
                }
        );
    }

    /*
     * Configura o modelo e as colunas da tabela
     */
    private void configurarTabela() {

        modeloTabela = new DefaultTableModel(
                new Object[] {
                        "ID",
                        "Produto",
                        "Estoque atual",
                        "Estoque mínimo",
                        "Valor de venda",
                        "Situação"
                },
                0
        ) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {

                return false;
            }
        };

        tabelaEstoqueBaixo.setModel(
                modeloTabela
        );

        /*
         * Aparência do cabeçalho
         */
        tabelaEstoqueBaixo
                .getTableHeader()
                .setDefaultRenderer(
                        new CabecalhoTabelaRenderer()
                );

        /*
         * Aparência das células
         */
        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(0)
                .setCellRenderer(
                        new CelulaTabelaRenderer(
                                SwingConstants.CENTER
                        )
                );

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(1)
                .setCellRenderer(
                        new CelulaTabelaRenderer(
                                SwingConstants.LEFT
                        )
                );

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(2)
                .setCellRenderer(
                        new CelulaTabelaRenderer(
                                SwingConstants.CENTER
                        )
                );

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        new CelulaTabelaRenderer(
                                SwingConstants.CENTER
                        )
                );

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new CelulaTabelaRenderer(
                                SwingConstants.RIGHT
                        )
                );

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(5)
                .setCellRenderer(
                        new SituacaoTabelaRenderer()
                );

        /*
         * Largura das colunas
         */
        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(0)
                .setPreferredWidth(50);

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(1)
                .setPreferredWidth(260);

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(2)
                .setPreferredWidth(110);

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(3)
                .setPreferredWidth(110);

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(4)
                .setPreferredWidth(130);

        tabelaEstoqueBaixo
                .getColumnModel()
                .getColumn(5)
                .setPreferredWidth(130);
    }

    /*
     * Consulta os dados no banco e atualiza o Dashboard
     */
    private void carregarDashboard() {

        btnAtualizar.setEnabled(false);
        btnAtualizar.setText("Atualizando...");
        btnAtualizar.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.WAIT_CURSOR
                )
        );

        try {

            DashboardDAO dashboardDAO =
                    new DashboardDAO();

            int totalProdutos =
                    dashboardDAO.contarProdutosAtivos();

            int totalClientes =
                    dashboardDAO.contarClientesAtivos();

            int totalEstoqueBaixo =
                    dashboardDAO.contarProdutosEstoqueBaixo();

            lblTotalProdutos.setText(
                    String.valueOf(totalProdutos)
            );

            lblTotalClientes.setText(
                    String.valueOf(totalClientes)
            );

            lblEstoqueBaixo.setText(
                    String.valueOf(totalEstoqueBaixo)
            );

            lblValorEstoque.setText(
                    Formatador.moeda(
                            dashboardDAO
                                    .calcularValorTotalEstoque()
                    )
            );

            List<Produto> produtos =
                    dashboardDAO
                            .listarProdutosEstoqueBaixo();

            carregarTabelaEstoqueBaixo(
                    produtos
            );

            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy HH:mm:ss"
                    );

            lblUltimaAtualizacao.setText(
                    "Atualizado em "
                            + LocalDateTime
                                    .now()
                                    .format(formato)
            );

        } catch (Exception e) {

            e.printStackTrace();

            lblUltimaAtualizacao.setText(
                    "Erro ao atualizar os dados"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível carregar os dados do Dashboard.\n"
                            + "Verifique a conexão com o banco de dados.",
                    "Erro ao carregar Dashboard",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {

            btnAtualizar.setEnabled(true);
            btnAtualizar.setText(
                    "Atualizar Dashboard"
            );

            btnAtualizar.setBackground(
                    COR_PRIMARIA
            );

            btnAtualizar.setCursor(
                    Cursor.getPredefinedCursor(
                            Cursor.HAND_CURSOR
                    )
            );
        }
    }

    /*
     * Preenche a tabela com os produtos encontrados
     */
    private void carregarTabelaEstoqueBaixo(
            List<Produto> produtos
    ) {

        modeloTabela.setRowCount(0);

        if (produtos == null) {
            return;
        }

        for (Produto produto : produtos) {

            modeloTabela.addRow(
                    new Object[] {
                            produto.getIdProduto(),
                            produto.getNome(),
                            produto.getQuantidadeEstoque(),
                            produto.getEstoqueMinimo(),
                            Formatador.moeda(
                                    produto.getValorVenda()
                            ),
                            "Estoque baixo"
                    }
            );
        }
    }

    /*
     * Personaliza o cabeçalho da tabela
     */
    private static class CabecalhoTabelaRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        public CabecalhoTabelaRenderer() {

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            13
                    )
            );

            setForeground(
                    COR_BRANCA
            );

            setBackground(
                    COR_PRIMARIA
            );

            setOpaque(true);

            setBorder(
                    new EmptyBorder(
                            0,
                            8,
                            0,
                            8
                    )
            );
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            setBackground(COR_PRIMARIA);
            setForeground(COR_BRANCA);

            return this;
        }
    }

    /*
     * Personaliza as células comuns da tabela
     */
    private static class CelulaTabelaRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        private final int alinhamento;

        public CelulaTabelaRenderer(
                int alinhamento
        ) {

            this.alinhamento =
                    alinhamento;

            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            setHorizontalAlignment(
                    alinhamento
            );

            setBorder(
                    new EmptyBorder(
                            0,
                            10,
                            0,
                            10
                    )
            );

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground()
                );

                setForeground(
                        table.getSelectionForeground()
                );

            } else {

                if (row % 2 == 0) {

                    setBackground(COR_BRANCA);

                } else {

                    setBackground(
                            COR_LINHA_ALTERNADA
                    );
                }

                setForeground(COR_TEXTO);
            }

            return this;
        }
    }

    /*
     * Personaliza a coluna Situação
     */
    private static class SituacaoTabelaRenderer
            extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        public SituacaoTabelaRenderer() {

            setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            super.getTableCellRendererComponent(
                    table,
                    value,
                    isSelected,
                    hasFocus,
                    row,
                    column
            );

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );

            setBorder(
                    new EmptyBorder(
                            0,
                            8,
                            0,
                            8
                    )
            );

            if (isSelected) {

                setBackground(
                        table.getSelectionBackground()
                );

                setForeground(
                        table.getSelectionForeground()
                );

            } else {

                if (row % 2 == 0) {

                    setBackground(COR_BRANCA);

                } else {

                    setBackground(
                            COR_LINHA_ALTERNADA
                    );
                }

                setForeground(
                        COR_ESTOQUE_BAIXO
                );
            }

            return this;
        }
    }
}