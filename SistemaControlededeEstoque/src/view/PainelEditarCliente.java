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

public class PainelEditarCliente extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtCidade;

    private int idCliente;
    private TelaPrincipal telaPrincipal;

    public PainelEditarCliente() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Cliente");
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

        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setBounds(460, 90, 100, 20);
        add(lblCpf);

        txtCpf = new JTextField();
        txtCpf.setBounds(460, 115, 180, 30);
        add(txtCpf);

        JLabel lblTelefone = new JLabel("Telefone");
        lblTelefone.setBounds(80, 165, 100, 20);
        add(lblTelefone);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(80, 190, 180, 30);
        add(txtTelefone);

        JLabel lblEmail = new JLabel("E-mail");
        lblEmail.setBounds(290, 165, 100, 20);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(290, 190, 350, 30);
        add(txtEmail);

        JLabel lblCidade = new JLabel("Cidade");
        lblCidade.setBounds(80, 240, 100, 20);
        add(lblCidade);

        txtCidade = new JTextField();
        txtCidade.setBounds(80, 265, 350, 30);
        add(txtCidade);

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            }
        });
        btnSalvar.setBounds(80, 330, 170, 35);
        add(btnSalvar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltar();
            }
        });
        btnVoltar.setBounds(270, 330, 120, 35);
        add(btnVoltar);
    }

    public PainelEditarCliente(TelaPrincipal telaPrincipal, int idCliente) {
        this();
        this.telaPrincipal = telaPrincipal;
        this.idCliente = idCliente;
        carregarCliente();
    }

    private void carregarCliente() {

        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.buscarPorId(idCliente);

        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Cliente não encontrado.");
            return;
        }

        txtNome.setText(cliente.getNome());
        txtCpf.setText(cliente.getCpf());
        txtTelefone.setText(cliente.getTelefone());
        txtEmail.setText(cliente.getEmail());
        txtCidade.setText(cliente.getCidade());
    }

    private void salvarAlteracoes() {

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

        if (nome.length() < 3) {
            JOptionPane.showMessageDialog(
                    this,
                    "O nome deve ter pelo menos 3 caracteres.",
                    "Nome inválido",
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

        Cliente cliente = new Cliente();

        cliente.setIdCliente(idCliente);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setTelefone(telefone);
        cliente.setEmail(email);
        cliente.setCidade(cidade);

        ClienteDAO clienteDAO = new ClienteDAO();
        boolean atualizado = clienteDAO.atualizar(cliente);

        if (atualizado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Cliente atualizado com sucesso.",
                    "Atualização realizada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            voltar();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível atualizar o cliente.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void voltar() {

        if (telaPrincipal != null) {
            telaPrincipal.carregarPainel(new PainelListarClientes(telaPrincipal));
        }
    }
}
