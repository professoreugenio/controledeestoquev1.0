package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.UsuarioDAO;
import model.Usuario;

public class PainelEditarPerfil extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;

    private Usuario usuarioLogado;

    public PainelEditarPerfil(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;

        setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Perfil Atual");
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
        lblLogin.setBounds(90, 160, 120, 20);
        add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(90, 185, 250, 30);
        add(txtLogin);

        JLabel lblNovaSenha = new JLabel("Nova Senha");
        lblNovaSenha.setBounds(90, 230, 120, 20);
        add(lblNovaSenha);

        txtNovaSenha = new JPasswordField();
        txtNovaSenha.setBounds(90, 255, 250, 30);
        add(txtNovaSenha);

        JLabel lblConfirmarSenha = new JLabel("Confirmar Senha");
        lblConfirmarSenha.setBounds(370, 230, 150, 20);
        add(lblConfirmarSenha);

        txtConfirmarSenha = new JPasswordField();
        txtConfirmarSenha.setBounds(370, 255, 250, 30);
        add(txtConfirmarSenha);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarPerfil();
            }
        });
        btnSalvar.setBounds(90, 320, 120, 35);
        add(btnSalvar);

        JButton btnLimparSenha = new JButton("Limpar Senha");
        btnLimparSenha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparSenha();
            }
        });
        btnLimparSenha.setBounds(230, 320, 140, 35);
        add(btnLimparSenha);

        carregarDados();
    }

    private void carregarDados() {

        if (usuarioLogado != null) {
            txtNome.setText(usuarioLogado.getNome());
            txtLogin.setText(usuarioLogado.getLogin());
        }
    }

    private void salvarPerfil() {

        if (usuarioLogado == null) {
            JOptionPane.showMessageDialog(this, "Usuário logado não encontrado.");
            return;
        }

        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String novaSenha = new String(txtNovaSenha.getPassword()).trim();
        String confirmarSenha = new String(txtConfirmarSenha.getPassword()).trim();

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

        usuarioLogado.setNome(nome);
        usuarioLogado.setLogin(login);

        UsuarioDAO dao = new UsuarioDAO();
        boolean atualizado = dao.atualizarPerfil(usuarioLogado, novaSenha);

        if (atualizado) {
            JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso.");
            limparSenha();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível atualizar o perfil.");
        }
    }

    private void limparSenha() {
        txtNovaSenha.setText("");
        txtConfirmarSenha.setText("");
    }
}
