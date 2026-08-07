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

public class PainelEditarFornecedor extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtNome;
    private JTextField txtCnpj;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtCidade;

    private int idFornecedor;
    private TelaPrincipal telaPrincipal;

    public PainelEditarFornecedor() {
        setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Fornecedor");
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

        JButton btnSalvar = new JButton("Salvar Alterações");
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            }
        });
        btnSalvar.setBounds(80, 330, 160, 35);
        add(btnSalvar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                voltar();
            }
        });
        btnVoltar.setBounds(260, 330, 120, 35);
        add(btnVoltar);
    }

    public PainelEditarFornecedor(TelaPrincipal telaPrincipal, int idFornecedor) {
        this();
        this.telaPrincipal = telaPrincipal;
        this.idFornecedor = idFornecedor;
        carregarFornecedor();
    }

    private void carregarFornecedor() {

        FornecedorDAO fornecedorDAO = new FornecedorDAO();
        Fornecedor fornecedor = fornecedorDAO.buscarPorId(idFornecedor);

        if (fornecedor == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Fornecedor não encontrado.",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE
            );

            voltar();
            return;
        }

        txtNome.setText(fornecedor.getNome());
        txtCnpj.setText(fornecedor.getCnpj());
        txtTelefone.setText(fornecedor.getTelefone());
        txtEmail.setText(fornecedor.getEmail());
        txtCidade.setText(fornecedor.getCidade());
    }

    private void salvarAlteracoes() {

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
         * Como o campo CNPJ é UNIQUE no banco,
         * é melhor salvar NULL quando o campo estiver vazio.
         * Assim evitamos erro com vários fornecedores sem CNPJ.
         */
        if (cnpj.isEmpty()) {
            cnpj = null;
        }

        Fornecedor fornecedor = new Fornecedor();

        fornecedor.setIdFornecedor(idFornecedor);
        fornecedor.setNome(nome);
        fornecedor.setCnpj(cnpj);
        fornecedor.setTelefone(telefone);
        fornecedor.setEmail(email);
        fornecedor.setCidade(cidade);

        FornecedorDAO fornecedorDAO = new FornecedorDAO();

        boolean atualizado = fornecedorDAO.atualizar(fornecedor);

        if (atualizado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Fornecedor atualizado com sucesso.",
                    "Atualização realizada",
                    JOptionPane.INFORMATION_MESSAGE
            );

            voltar();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Não foi possível atualizar o fornecedor. Verifique se o CNPJ já está cadastrado para outro fornecedor.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void voltar() {

        if (telaPrincipal != null) {
            telaPrincipal.carregarPainel(new PainelListarFornecedores(telaPrincipal));
        }
    }
}