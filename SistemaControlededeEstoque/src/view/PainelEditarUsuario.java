package view;

import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import dao.UsuarioDAO;
import model.Usuario;

public class PainelEditarUsuario extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;
    private JComboBox<String> comboPerfil;
    private JCheckBox chkAtivo;

    private int idUsuario;
    private Usuario usuario;

    public PainelEditarUsuario(int idUsuario) {
        this.idUsuario = idUsuario;

        setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Usuário");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(240, 20, 350, 30);
        add(lblTitulo);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setBounds(90, 90, 120, 20);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(90, 115, 350, 30);
        add(txtNome);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(470, 90, 120, 20);
        add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(470, 115, 220, 30);
        add(txtLogin);

        JLabel lblNovaSenha = new JLabel("Nova Senha");
        lblNovaSenha.setBounds(90, 165, 120, 20);
        add(lblNovaSenha);

        txtNovaSenha = new JPasswordField();
        txtNovaSenha.setBounds(90, 190, 250, 30);
        add(txtNovaSenha);

        JLabel lblConfirmarSenha = new JLabel("Confirmar Senha");
        lblConfirmarSenha.setBounds(370, 165, 150, 20);
        add(lblConfirmarSenha);

        txtConfirmarSenha = new JPasswordField();
        txtConfirmarSenha.setBounds(370, 190, 250, 30);
        add(txtConfirmarSenha);

        JLabel lblPerfil = new JLabel("Perfil");
        lblPerfil.setBounds(90, 240, 120, 20);
        add(lblPerfil);

        comboPerfil = new JComboBox<String>();
        comboPerfil.addItem("ADMINISTRADOR");
        comboPerfil.addItem("OPERADOR");
        comboPerfil.setBounds(90, 265, 180, 30);
        add(comboPerfil);

        chkAtivo = new JCheckBox("Usuário ativo");
        chkAtivo.setBounds(300, 265, 150, 30);
        add(chkAtivo);

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            }
        });
        btnSalvar.setBounds(90, 330, 160, 35);
        add(btnSalvar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltarListagem();
            }
        });
        btnVoltar.setBounds(270, 330, 120, 35);
        add(btnVoltar);

        carregarUsuario();
    }

    private void carregarUsuario() {

        UsuarioDAO dao = new UsuarioDAO();
        usuario = dao.buscarPorId(idUsuario);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "Usuário não encontrado.");
            voltarListagem();
            return;
        }

        txtNome.setText(usuario.getNome());
        txtLogin.setText(usuario.getLogin());
        comboPerfil.setSelectedItem(usuario.getPerfil());
        chkAtivo.setSelected(usuario.isAtivo());
    }

    private void salvarAlteracoes() {

        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String novaSenha = new String(txtNovaSenha.getPassword()).trim();
        String confirmarSenha = new String(txtConfirmarSenha.getPassword()).trim();
        String perfil = comboPerfil.getSelectedItem().toString();
        boolean ativo = chkAtivo.isSelected();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome.");
            txtNome.requestFocus();
            return;
        }

        if (login.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o login.");
            txtLogin.requestFocus();
            return;
        }

        if (!novaSenha.isEmpty() || !confirmarSenha.isEmpty()) {

            if (novaSenha.length() < 6) {
                JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 6 caracteres.");
                txtNovaSenha.requestFocus();
                return;
            }

            if (!novaSenha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas não conferem.");
                txtConfirmarSenha.requestFocus();
                return;
            }
        }

        usuario.setNome(nome);
        usuario.setLogin(login);
        usuario.setPerfil(perfil);
        usuario.setAtivo(ativo);

        UsuarioDAO dao = new UsuarioDAO();
        boolean atualizado = dao.atualizar(usuario, novaSenha);

        if (atualizado) {
            JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso.");
            voltarListagem();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível atualizar o usuário.");
        }
    }

    private void voltarListagem() {

        Window janela = SwingUtilities.getWindowAncestor(this);

        if (janela instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) janela;
            telaPrincipal.carregarPainel(new PainelListarUsuarios());
        }
    }
}
