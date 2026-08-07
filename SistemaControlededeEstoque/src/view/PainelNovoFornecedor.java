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

import dao.FornecedorDAO;
import model.Fornecedor;

public class PainelNovoFornecedor extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtCnpj;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtCidade;

    public PainelNovoFornecedor() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Novo Fornecedor");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        lblTitulo.setBounds(230, 20, 350, 30);
        add(lblTitulo);

        JLabel lblNome = new JLabel("Nome do Fornecedor");
        lblNome.setBounds(80, 90, 180, 20);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(80, 115, 350, 30);
        add(txtNome);
        txtNome.setColumns(10);

        JLabel lblCnpj = new JLabel("CNPJ");
        lblCnpj.setBounds(460, 90, 150, 20);
        add(lblCnpj);

        txtCnpj = new JTextField();
        txtCnpj.setBounds(460, 115, 180, 30);
        add(txtCnpj);
        txtCnpj.setColumns(10);

        JLabel lblTelefone = new JLabel("Telefone");
        lblTelefone.setBounds(80, 165, 150, 20);
        add(lblTelefone);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(80, 190, 180, 30);
        add(txtTelefone);
        txtTelefone.setColumns(10);

        JLabel lblEmail = new JLabel("E-mail");
        lblEmail.setBounds(280, 165, 150, 20);
        add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(280, 190, 360, 30);
        add(txtEmail);
        txtEmail.setColumns(10);

        JLabel lblCidade = new JLabel("Cidade");
        lblCidade.setBounds(80, 240, 150, 20);
        add(lblCidade);

        txtCidade = new JTextField();
        txtCidade.setBounds(80, 265, 560, 30);
        add(txtCidade);
        txtCidade.setColumns(10);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarFornecedor();
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

    private void salvarFornecedor() {

        String nome = txtNome.getText().trim();
        String cnpj = txtCnpj.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String cidade = txtCidade.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe o nome do fornecedor.",
                    "Campo obrigatório",
                    JOptionPane.WARNING_MESSAGE
            );

            txtNome.requestFocus();
            return;
        }

        /*
         * Como o campo CNPJ está como UNIQUE no banco,
         * é melhor salvar NULL quando o campo estiver vazio.
         * Assim evitamos erro caso existam vários fornecedores sem CNPJ.
         */
        if (cnpj.isEmpty()) {
            cnpj = null;
        }

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setNome(nome);
        fornecedor.setCnpj(cnpj);
        fornecedor.setTelefone(telefone);
        fornecedor.setEmail(email);
        fornecedor.setCidade(cidade);

        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        boolean cadastrado = fornecedorDAO.cadastrar(fornecedor);

        if (cadastrado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Fornecedor cadastrado com sucesso.",
                    "Cadastro realizado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            limparCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível cadastrar o fornecedor. Verifique se o CNPJ já está cadastrado.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCnpj.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtCidade.setText("");

        txtNome.requestFocus();
    }
}