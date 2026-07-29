package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.UsuarioDAO;
import model.Usuario;

public class TelaLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JCheckBox chkMostrarSenha;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaLogin frame = new TelaLogin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaLogin() {
        setTitle("Sistema Controle de Estoque - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 360);
        setLocationRelativeTo(null);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Sistema de Controle de Estoque");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblTitulo.setBounds(35, 30, 360, 30);
        contentPane.add(lblTitulo);

        JLabel lblLogin = new JLabel("Login");
        lblLogin.setBounds(70, 95, 100, 20);
        contentPane.add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setBounds(70, 120, 290, 30);
        contentPane.add(txtLogin);
        txtLogin.setColumns(10);

        JLabel lblSenha = new JLabel("Senha");
        lblSenha.setBounds(70, 160, 100, 20);
        contentPane.add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(70, 185, 290, 30);
        contentPane.add(txtSenha);

        chkMostrarSenha = new JCheckBox("Mostrar senha");
        chkMostrarSenha.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarSenha();
            }
        });
        chkMostrarSenha.setBounds(70, 220, 140, 25);
        contentPane.add(chkMostrarSenha);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                entrar();
            }
        });
        btnEntrar.setBounds(70, 260, 135, 35);
        contentPane.add(btnEntrar);

        JButton btnSair = new JButton("Sair");
        btnSair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sair();
            }
        });
        btnSair.setBounds(225, 260, 135, 35);
        contentPane.add(btnSair);
    }

    private void entrar() {

        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (login.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o login.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            txtLogin.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe a senha.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            txtSenha.requestFocus();
            return;
        }

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.autenticar(login, senha);

        if (usuario != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Bem-vindo, " + usuario.getNome() + "!",
                    "Login realizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            TelaPrincipal telaPrincipal = new TelaPrincipal(usuario);
            telaPrincipal.setVisible(true);

            dispose();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Login ou senha inválidos.",
                    "Acesso negado",
                    JOptionPane.ERROR_MESSAGE
            );

            txtSenha.setText("");
            txtLogin.requestFocus();
        }
    }

    private void mostrarSenha() {

        if (chkMostrarSenha.isSelected()) {
            txtSenha.setEchoChar((char) 0);
        } else {
            txtSenha.setEchoChar('•');
        }
    }

    private void sair() {

        int resposta = JOptionPane.showConfirmDialog(
                this,
                "Deseja realmente sair?",
                "Confirmar saída",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (resposta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
