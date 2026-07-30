package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.ClienteDAO;
import model.Cliente;

public class PainelCadastrarCliente extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtCidade;

    public PainelCadastrarCliente() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Cadastro de Clientes");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(230, 20, 350, 30);
        add(lblTitulo);

        JLabel lblNome = new JLabel("Nome");
        lblNome.setBounds(80, 90, 100, 20);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(80, 115, 350, 30);
        add(txtNome);
        txtNome.setColumns(10);

        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setBounds(460, 90, 100, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(460, 115, 180, 30);
        add(txtCpf);
        txtCpf.setColumns(10);

        JLabel lblTelefone = new JLabel("Telefone");
        lblTelefone.setBounds(80, 165, 100, 20);
        add(lblTelefone);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(80, 190, 180, 30);
        add(txtTelefone);
        txtTelefone.setColumns(10);

        JLabel lblEmail = new JLabel("E-mail");
        lblEmail.setBounds(290, 165, 100, 20);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(290, 190, 350, 30);
        add(txtEmail);
        txtEmail.setColumns(10);

        JLabel lblCidade = new JLabel("Cidade");
        lblCidade.setBounds(80, 240, 100, 20);
        add(lblCidade);

        txtCidade = new JTextField();
        txtCidade.setBounds(80, 265, 350, 30);
        add(txtCidade);
        txtCidade.setColumns(10);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarCliente();
            }
        });
        btnSalvar.setBounds(80, 330, 120, 35);
        add(btnSalvar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        btnLimpar.setBounds(220, 330, 120, 35);
        add(btnLimpar);
    }

    private void salvarCliente() {

        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String cidade = txtCidade.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do cliente.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );
            txtNome.requestFocus();
            return;
        }

        if (!email.isEmpty() && !email.contains("@")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe um e-mail válido.",
                    "E-mail inválido",
                    JOptionPane.WARNING_MESSAGE
            );
            txtEmail.requestFocus();
            return;
        }

        Cliente cliente = new Cliente(nome, cpf, telefone, email, cidade);

        ClienteDAO clienteDAO = new ClienteDAO();
        boolean cadastrado = clienteDAO.cadastrar(cliente);

        if (cadastrado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cliente cadastrado com sucesso.",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível cadastrar o cliente.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtCidade.setText("");
        txtNome.requestFocus();
    }
}
