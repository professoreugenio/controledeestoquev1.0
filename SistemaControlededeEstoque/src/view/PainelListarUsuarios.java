package view;

import java.awt.Font;
import java.awt.Window;
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
import javax.swing.SwingUtilities;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;

import dao.UsuarioDAO;
import model.Usuario;

public class PainelListarUsuarios extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtPesquisa;
    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;

    public PainelListarUsuarios() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Listar Usuários");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(240, 20, 350, 30);
        add(lblTitulo);

        JLabel lblPesquisar = new JLabel("Pesquisar Usuário");
        lblPesquisar.setBounds(50, 80, 150, 20);
        add(lblPesquisar);

        txtPesquisa = new JTextField();
        txtPesquisa.setBounds(50, 105, 330, 30);
        add(txtPesquisa);

        JButton btnPesquisar = new JButton("Pesquisar");
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pesquisarUsuarios();
            }
        });
        btnPesquisar.setBounds(400, 105, 120, 30);
        add(btnPesquisar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                carregarUsuarios();
            }
        });
        btnAtualizar.setBounds(540, 105, 120, 30);
        add(btnAtualizar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editarUsuarioSelecionado();
            }
        });
        btnEditar.setBounds(680, 105, 100, 30);
        add(btnEditar);

        JButton btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                excluirUsuarioSelecionado();
            }
        });
        btnExcluir.setBounds(790, 105, 100, 30);
        add(btnExcluir);

        JScrollPane scrollTabela = new JScrollPane();
        scrollTabela.setBounds(50, 160, 840, 330);
        add(scrollTabela);

        tabelaUsuarios = new JTable();
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaUsuarios.getTableHeader().setReorderingAllowed(false);
        scrollTabela.setViewportView(tabelaUsuarios);

        configurarTabela();
        carregarUsuarios();
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
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Login");
        modeloTabela.addColumn("Perfil");
        modeloTabela.addColumn("Status");

        tabelaUsuarios.setModel(modeloTabela);

        tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(240);
        tabelaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(160);
        tabelaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        aplicarDestaqueUsuariosInativos();
    }
    
    
    private void aplicarDestaqueUsuariosInativos() {

        DefaultTableCellRenderer renderizador = new DefaultTableCellRenderer() {

            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component componente = super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

                String status = table.getValueAt(row, 4).toString();

                if ("Inativo".equalsIgnoreCase(status)) {
                    componente.setBackground(new Color(220, 53, 69));
                    componente.setForeground(Color.WHITE);
                } else {
                    if (isSelected) {
                        componente.setBackground(table.getSelectionBackground());
                        componente.setForeground(table.getSelectionForeground());
                    } else {
                        componente.setBackground(Color.WHITE);
                        componente.setForeground(Color.BLACK);
                    }
                }

                return componente;
            }
        };

        for (int i = 0; i < tabelaUsuarios.getColumnCount(); i++) {
            tabelaUsuarios.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
    } 

    private void carregarUsuarios() {

        modeloTabela.setRowCount(0);

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> usuarios = dao.listar();

        preencherTabela(usuarios);

        txtPesquisa.setText("");
        txtPesquisa.requestFocus();
    }

    private void pesquisarUsuarios() {

        String pesquisa = txtPesquisa.getText().trim();

        if (pesquisa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite o nome do usuário para pesquisar.");
            txtPesquisa.requestFocus();
            return;
        }

        modeloTabela.setRowCount(0);

        UsuarioDAO dao = new UsuarioDAO();
        List<Usuario> usuarios = dao.pesquisarPorNome(pesquisa);

        preencherTabela(usuarios);

        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum usuário encontrado.");
        }
    }

    private void preencherTabela(List<Usuario> usuarios) {

        for (Usuario usuario : usuarios) {

            String status = usuario.isAtivo() ? "Ativo" : "Inativo";

            modeloTabela.addRow(new Object[] {
                    usuario.getIdUsuario(),
                    usuario.getNome(),
                    usuario.getLogin(),
                    usuario.getPerfil(),
                    status
            });
        }
    }

    private int obterIdSelecionado() {

        int linhaSelecionada = tabelaUsuarios.getSelectedRow();

        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário na tabela.");
            return -1;
        }

        return Integer.parseInt(modeloTabela.getValueAt(linhaSelecionada, 0).toString());
    }

    private void editarUsuarioSelecionado() {

        int idUsuario = obterIdSelecionado();

        if (idUsuario == -1) {
            return;
        }

        Window janela = SwingUtilities.getWindowAncestor(this);

        if (janela instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) janela;
            telaPrincipal.carregarPainel(new PainelEditarUsuario(idUsuario));
        }
    }

    private void excluirUsuarioSelecionado() {

        int idUsuario = obterIdSelecionado();

        if (idUsuario == -1) {
            return;
        }

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente excluir este usuário?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {

            UsuarioDAO dao = new UsuarioDAO();
            boolean excluido = dao.excluirLogico(idUsuario);

            if (excluido) {
                JOptionPane.showMessageDialog(this, "Usuário excluído com sucesso.");
                carregarUsuarios();
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível excluir o usuário.");
            }
        }
    }
}
