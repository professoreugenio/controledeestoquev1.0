# Sistema de Controle de Estoque

Projeto desenvolvido durante o curso **Programador de Sistemas**, com o objetivo de aplicar conceitos de **Java**, **Programação Orientada a Objetos**, **Java Swing**, **JDBC** e **banco de dados MySQL**.

O sistema permite controlar produtos, categorias, fornecedores, clientes, usuários e movimentações de estoque por meio de uma aplicação desktop.

---
## 📌 Acessando o sistema
### Admin:
usuário: admin
senha: 123456
### Operador
usuário: operador
senha: 123456


## 📌 Sobre o projeto

O **Sistema de Controle de Estoque** foi criado para auxiliar no gerenciamento de produtos e movimentações de entrada e saída.

A aplicação possui uma interface gráfica desenvolvida com **Java Swing**, organização em camadas e comunicação com banco de dados utilizando **JDBC**.

O projeto foi desenvolvido no **Eclipse IDE**, utilizando o **JDK 21**.

---

## 🎯 Objetivos do projeto

- Aplicar os fundamentos da linguagem Java.
- Utilizar Programação Orientada a Objetos.
- Criar interfaces gráficas com Java Swing.
- Integrar uma aplicação Java ao banco de dados.
- Implementar operações de cadastro, consulta, edição e exclusão.
- Controlar entradas e saídas de produtos.
- Trabalhar com autenticação e permissões de usuários.
- Organizar o código em pacotes e responsabilidades.

---

## ⚙️ Funcionalidades

### 🔐 Usuários

- Login de usuários.
- Cadastro de usuários.
- Edição de usuários.
- Exclusão de usuários.
- Controle de perfil e permissões.
- Criptografia de senha.
- Validação dos dados informados.

### 📦 Produtos

- Cadastro de produtos.
- Listagem de produtos.
- Pesquisa por nome.
- Edição de produtos.
- Exclusão de produtos.
- Controle de preço e quantidade.
- Associação do produto com categoria e fornecedor.

### 🗂️ Categorias

- Cadastro de categorias.
- Consulta de categorias.
- Edição e exclusão.
- Associação com produtos.

### 🚚 Fornecedores

- Cadastro de fornecedores.
- Consulta de fornecedores.
- Edição de informações.
- Exclusão de registros.

### 👥 Clientes

- Cadastro de clientes.
- Listagem e pesquisa.
- Edição dos dados cadastrados.
- Exclusão de clientes.

### 📊 Estoque

- Registro de entrada de produtos.
- Registro de saída de produtos.
- Atualização automática da quantidade disponível.
- Consulta das movimentações.
- Registro de número da nota fiscal.
- Valor unitário da movimentação.
- Observações sobre a entrada ou saída.
- Filtro de movimentações por data.

### 📈 Dashboard

- Total de produtos cadastrados.
- Total de clientes cadastrados.
- Produtos com estoque baixo.
- Valor estimado do estoque.
- Resumo das informações do sistema.

---

## 🛠️ Tecnologias utilizadas

- Java
- JDK 21
- Eclipse IDE
- WindowBuilder
- Java Swing
- JDBC
- MySQL
- MySQL Workbench
- MySQL Connector/J
- SQL
- Git
- GitHub

---

## 🧱 Organização do projeto

O projeto está organizado em pacotes para facilitar a manutenção e separar as responsabilidades da aplicação.

```text
SistemaControledeEstoque/
│
├── src/
│   ├── app/
│   │   └── Main.java
│   │
│   ├── dao/
│   │   ├── CategoriaDAO.java
│   │   ├── ClienteDAO.java
│   │   ├── ConexaoDAO.java
│   │   ├── DashboardDAO.java
│   │   ├── FornecedorDAO.java
│   │   ├── MovimentacaoEstoqueDAO.java
│   │   ├── ProdutoDAO.java
│   │   ├── TesteConexao.java
│   │   └── UsuarioDAO.java
│   │
│   ├── model/
│   │   ├── Categoria.java
│   │   ├── Cliente.java
│   │   ├── Fornecedor.java
│   │   ├── MovimentacaoEstoque.java
│   │   ├── Produto.java
│   │   └── Usuario.java
│   │
│   ├── util/
│   │   ├── CriptografiaSenha.java
│   │   ├── Formatador.java
│   │   ├── PermissaoUtil.java
│   │   ├── SenhaUtil.java
│   │   └── Validador.java
│   │
│   └── view/
│       ├── PainelCadastrarCliente.java
│       ├── PainelCadastrarProduto.java
│       ├── PainelDashboard.java
│       ├── PainelEditarCliente.java
│       ├── PainelEditarPerfil.java
│       ├── PainelEditarProduto.java
│       ├── PainelEditarUsuario.java
│       ├── PainelEntradaEstoque.java
│       ├── PainelListarClientes.java
│       ├── PainelListarEstoque.java
│       ├── PainelListarProdutos.java
│       ├── PainelListarUsuarios.java
│       ├── TelaLogin.java
│       └── TelaPrincipal.java
│
└── README.md
```

---

## 📚 Responsabilidade dos pacotes

### `app`

Contém a classe principal responsável por iniciar o sistema.

### `model`

Contém as classes que representam as entidades do sistema, como produto, cliente, fornecedor e usuário.

### `dao`

Contém as classes responsáveis pela comunicação com o banco de dados.

O termo **DAO** significa *Data Access Object*, ou Objeto de Acesso a Dados.

Essas classes executam comandos como:

- `INSERT`
- `SELECT`
- `UPDATE`
- `DELETE`

### `view`

Contém as telas e os painéis da aplicação desenvolvidos com Java Swing.

### `util`

Contém classes auxiliares utilizadas em diferentes partes do projeto, como validação, formatação, criptografia e controle de permissões.

---

## 🗄️ Banco de dados

O sistema utiliza um banco de dados MySQL.

Exemplo de criação do banco:

```sql
CREATE DATABASE controle_estoque_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE controle_estoque_db;
```

Principais tabelas utilizadas:

- `usuarios`
- `produtos`
- `categorias`
- `fornecedores`
- `clientes`
- `movimentacoes_estoque`

> O script completo do banco de dados pode ser colocado em uma pasta chamada `database`.

Exemplo:

```text
database/
└── controle_estoque_db.sql
```

---

## 🔌 Configuração da conexão

A configuração da conexão com o banco está localizada na classe:

```text
src/dao/ConexaoDAO.java
```

Exemplo de configuração:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/controle_estoque_db";

private static final String USUARIO = "root";
private static final String SENHA = "";
```

Ajuste o usuário, a senha, a porta e o nome do banco de acordo com o ambiente utilizado.

> Evite publicar senhas reais no GitHub. Em projetos profissionais, utilize variáveis de ambiente ou um arquivo de configuração externo.

---

## 📦 Dependências

O projeto necessita do driver JDBC do MySQL:

```text
mysql-connector-j
```

No Eclipse:

1. Clique com o botão direito no projeto.
2. Acesse **Build Path**.
3. Clique em **Configure Build Path**.
4. Abra a aba **Libraries**.
5. Clique em **Add External JARs**.
6. Selecione o arquivo do MySQL Connector/J.
7. Clique em **Apply and Close**.

---

## ▶️ Como executar o projeto

### 1. Requisitos

Antes de executar, instale:

- JDK 21.
- Eclipse IDE.
- MySQL Server.
- MySQL Workbench.
- MySQL Connector/J.

### 2. Importar o projeto no Eclipse

1. Abra o Eclipse.
2. Clique em **File > Import**.
3. Escolha **Existing Projects into Workspace**.
4. Selecione a pasta do projeto.
5. Clique em **Finish**.

### 3. Criar o banco de dados

Execute o script SQL no MySQL Workbench.

### 4. Configurar a conexão

Abra a classe `ConexaoDAO.java` e informe:

- endereço do servidor;
- porta;
- nome do banco;
- usuário;
- senha.

### 5. Testar a conexão

Execute a classe:

```text
src/dao/TesteConexao.java
```

A mensagem esperada é semelhante a:

```text
Conexão realizada com sucesso!
```

### 6. Iniciar o sistema

Execute a classe:

```text
src/app/Main.java
```

No Eclipse:

1. Clique com o botão direito em `Main.java`.
2. Escolha **Run As**.
3. Clique em **Java Application**.

---

## 🔐 Segurança

O sistema possui classes auxiliares para tratamento de senhas:

```text
CriptografiaSenha.java
SenhaUtil.java
```

Boas práticas adotadas:

- Uso de `PreparedStatement`.
- Validação de campos.
- Tratamento de exceções.
- Restrição de funcionalidades por perfil.
- Proteção contra SQL Injection.
- Senhas armazenadas de forma protegida.
- Fechamento automático de conexões com `try-with-resources`.

> Para sistemas reais, recomenda-se utilizar algoritmos próprios para senha, como BCrypt, PBKDF2 ou Argon2. SHA-256 puro não deve ser usado sozinho para armazenar senhas.

---

## ✅ Boas práticas aplicadas

- Separação do projeto em pacotes.
- Uso de classes de modelo.
- Uso do padrão DAO.
- Nomes claros para classes, métodos e variáveis.
- Reutilização de código.
- Validação antes de gravar no banco.
- Uso de `PreparedStatement`.
- Tratamento de erros com `try/catch`.
- Organização das telas em painéis.
- Controle de permissões dos usuários.
- Formatação de valores e datas.

---

## ⚠️ Erros comuns

### Erro de conexão com o banco

Verifique:

- se o MySQL está iniciado;
- se a porta está correta;
- se o banco existe;
- se o usuário e a senha estão corretos;
- se o MySQL Connector/J foi adicionado ao projeto.

### `ClassNotFoundException`

Normalmente ocorre quando o driver JDBC não foi adicionado ao Build Path.

### `Access denied for user`

O usuário ou a senha do MySQL está incorreto.

### `Unknown database`

O banco informado na conexão ainda não foi criado.

### `UnsupportedClassVersionError`

O programa foi compilado em uma versão mais nova do Java do que a instalada no computador.

Utilize a mesma versão do Java para compilar e executar o sistema.

---

## 🖥️ Geração do arquivo executável

No Eclipse:

1. Clique com o botão direito no projeto.
2. Selecione **Export**.
3. Escolha **Runnable JAR file**.
4. Selecione a classe `Main`.
5. Informe o local de destino.
6. Escolha a opção para incluir as bibliotecas.
7. Clique em **Finish**.

Para executar em outro computador, será necessário:

- possuir uma versão compatível do Java;
- ter acesso ao banco de dados;
- configurar corretamente a conexão;
- disponibilizar as bibliotecas necessárias.

---

## 🚀 Melhorias futuras

- Cadastro de imagem dos produtos.
- Relatórios em PDF.
- Exportação de dados para Excel.
- Histórico detalhado de alterações.
- Backup automático do banco.
- Recuperação de senha.
- Controle de níveis de acesso mais detalhado.
- Alertas de estoque mínimo.
- Gráficos no dashboard.
- Geração de código de barras.
- Controle de vendas.
- Instalador para Windows.

---

## 👨‍🏫 Contexto educacional

Este projeto foi desenvolvido como atividade prática do curso **Programador de Sistemas**.

Durante o desenvolvimento, foram trabalhados os seguintes conteúdos:

- lógica de programação;
- linguagem Java;
- orientação a objetos;
- interface gráfica;
- eventos;
- tratamento de erros;
- coleções;
- banco de dados;
- SQL;
- JDBC;
- organização em camadas;
- CRUD;
- autenticação;
- controle de acesso;
- geração de arquivo executável.

---

## 👨‍💻 Autor

**Professor Eugênio**

Projeto educacional desenvolvido para apoiar o ensino de programação e desenvolvimento de sistemas.

---

## 📄 Licença

Este projeto possui finalidade educacional.

A utilização, adaptação e distribuição devem respeitar os critérios definidos pelo autor e pela instituição responsável pelo curso.
