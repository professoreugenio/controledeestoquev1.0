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
import util.SenhaUtil;

public class PainelNovoUsuario extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;
    private JComboBox<String> comboPerfil;
    private JCheckBox chkAtivo;

    public PainelNovoUsuario() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastrar Novo Usuário");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(220, 20, 400, 30);
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

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setBounds(90, 165, 120, 20);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(90, 190, 250, 30);
        add(txtSenha);

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
        comboPerfil.addItem("ADMIN");
        comboPerfil.addItem("OPERADOR");
        comboPerfil.setBounds(90, 265, 180, 30);
        add(comboPerfil);

        chkAtivo = new JCheckBox("Usuário ativo");
        chkAtivo.setSelected(true);
        chkAtivo.setBounds(300, 265, 150, 30);
        add(chkAtivo);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarUsuario();
            }
        });
        btnSalvar.setBounds(90, 330, 120, 35);
        add(btnSalvar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        btnLimpar.setBounds(230, 330, 120, 35);
        add(btnLimpar);
    }

    private void salvarUsuario() {

        String nome = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();
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

        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe a senha.");
            txtSenha.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            JOptionPane.showMessageDialog(this, "A senha deve ter pelo menos 6 caracteres.");
            txtSenha.requestFocus();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas não conferem.");
            txtConfirmarSenha.requestFocus();
            return;
        }

        String senhaHash = SenhaUtil.gerarHash(senha);

        Usuario usuario = new Usuario(nome, login, senhaHash, perfil, ativo);

        UsuarioDAO dao = new UsuarioDAO();
        boolean cadastrado = dao.cadastrar(usuario);

        if (cadastrado) {
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso.");
            limparCampos();
            voltarListagem();
        } else {
            JOptionPane.showMessageDialog(this, "Não foi possível cadastrar o usuário. Verifique se o login já existe.");
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtConfirmarSenha.setText("");
        comboPerfil.setSelectedIndex(0);
        chkAtivo.setSelected(true);
        txtNome.requestFocus();
    }
    
    private void voltarListagem() {

        Window janela = SwingUtilities.getWindowAncestor(this);

        if (janela instanceof TelaPrincipal) {
            TelaPrincipal telaPrincipal = (TelaPrincipal) janela;
            telaPrincipal.carregarPainel(new PainelListarUsuarios());
        }
    }
}
