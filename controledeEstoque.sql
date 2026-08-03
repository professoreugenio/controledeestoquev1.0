-- =========================================================
-- BANCO DE DADOS: CONTROLE DE ESTOQUE
-- Script corrigido para MySQL 8.0+
-- =========================================================

SET NAMES utf8mb4;
SET SQL_MODE = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE DATABASE IF NOT EXISTS controle_estoque_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE controle_estoque_db;

-- Desativa temporariamente as validações para permitir recriar as tabelas.
SET FOREIGN_KEY_CHECKS = 0;

-- A exclusão deve começar pelas tabelas filhas.
DROP TABLE IF EXISTS movimentacoes_estoque;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS fornecedores;
DROP TABLE IF EXISTS categorias;

SET FOREIGN_KEY_CHECKS = 1;

-- =========================================================
-- 1. TABELA: categorias
-- =========================================================
CREATE TABLE categorias (
    id_categoria INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255) NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,

    CONSTRAINT pk_categorias PRIMARY KEY (id_categoria),
    CONSTRAINT uk_categorias_nome UNIQUE (nome),
    CONSTRAINT chk_categorias_ativo CHECK (ativo IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- =========================================================
-- 2. TABELA: fornecedores
-- =========================================================
CREATE TABLE fornecedores (
    id_fornecedor INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    cnpj VARCHAR(20) NOT NULL,
    telefone VARCHAR(20) NULL,
    email VARCHAR(120) NULL,
    cidade VARCHAR(80) NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_fornecedores PRIMARY KEY (id_fornecedor),
    CONSTRAINT uk_fornecedores_cnpj UNIQUE (cnpj),
    CONSTRAINT chk_fornecedores_ativo CHECK (ativo IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- =========================================================
-- 3. TABELA: clientes
-- =========================================================
CREATE TABLE clientes (
    id_cliente INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    cpf VARCHAR(20) NULL,
    telefone VARCHAR(20) NULL,
    email VARCHAR(120) NULL,
    cidade VARCHAR(80) NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_clientes PRIMARY KEY (id_cliente),
    CONSTRAINT chk_clientes_ativo CHECK (ativo IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- =========================================================
-- 4. TABELA: usuarios
-- =========================================================
CREATE TABLE usuarios (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    perfil ENUM('ADMINISTRADOR', 'OPERADOR') NOT NULL DEFAULT 'OPERADOR',
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_usuarios PRIMARY KEY (id_usuario),
    CONSTRAINT uk_usuarios_login UNIQUE (login),
    CONSTRAINT chk_usuarios_ativo CHECK (ativo IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- =========================================================
-- 5. TABELA: produtos
-- Depende de categorias e fornecedores.
-- =========================================================
CREATE TABLE produtos (
    id_produto INT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(255) NULL,
    valor_custo DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    valor_venda DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    quantidade_estoque INT NOT NULL DEFAULT 0,
    estoque_minimo INT NOT NULL DEFAULT 0,
    id_categoria INT NOT NULL,
    id_fornecedor INT NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_produtos PRIMARY KEY (id_produto),

    CONSTRAINT fk_produtos_categorias
        FOREIGN KEY (id_categoria)
        REFERENCES categorias (id_categoria)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT fk_produtos_fornecedores
        FOREIGN KEY (id_fornecedor)
        REFERENCES fornecedores (id_fornecedor)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    CONSTRAINT chk_produtos_valor_custo CHECK (valor_custo >= 0),
    CONSTRAINT chk_produtos_valor_venda CHECK (valor_venda >= 0),
    CONSTRAINT chk_produtos_quantidade CHECK (quantidade_estoque >= 0),
    CONSTRAINT chk_produtos_estoque_minimo CHECK (estoque_minimo >= 0),
    CONSTRAINT chk_produtos_ativo CHECK (ativo IN (0, 1)),

    INDEX idx_produtos_categoria (id_categoria),
    INDEX idx_produtos_fornecedor (id_fornecedor),
    INDEX idx_produtos_nome (nome)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- =========================================================
-- 6. TABELA: movimentacoes_estoque
-- Depende de produtos; por isso deve ser criada depois.
-- =========================================================
CREATE TABLE movimentacoes_estoque (
    id_movimentacao INT NOT NULL AUTO_INCREMENT,
    id_produto INT NOT NULL,
    tipo ENUM('ENTRADA', 'SAIDA') NOT NULL,
    nr_notafiscal VARCHAR(50) NULL,
    quantidade INT NOT NULL,
    valor_unitario DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    observacao VARCHAR(255) NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_movimentacoes_estoque PRIMARY KEY (id_movimentacao),

    CONSTRAINT fk_movimentacoes_produtos
        FOREIGN KEY (id_produto)
        REFERENCES produtos (id_produto)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_movimentacoes_quantidade CHECK (quantidade > 0),
    CONSTRAINT chk_movimentacoes_valor CHECK (valor_unitario >= 0),

    INDEX idx_movimentacoes_produto (id_produto),
    INDEX idx_movimentacoes_nota_fiscal (nr_notafiscal),
    INDEX idx_movimentacoes_criado_em (criado_em),
    INDEX idx_movimentacoes_tipo_data (tipo, criado_em)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- =========================================================
-- DADOS: categorias
-- =========================================================
INSERT INTO categorias
    (id_categoria, nome, descricao, ativo)
VALUES
    (1, 'Informática', 'Produtos de informática e acessórios', 1),
    (2, 'Escritório', 'Materiais de escritório', 1),
    (3, 'Limpeza', 'Produtos de limpeza', 1);

-- =========================================================
-- DADOS: fornecedores
-- =========================================================
INSERT INTO fornecedores
    (id_fornecedor, nome, cnpj, telefone, email, cidade, ativo, criado_em)
VALUES
    (1, 'Distribuidora Alfa', '00.000.000/0001-00', '(85) 99999-0001', 'contato@alfa.com', 'Fortaleza', 1, '2026-08-01 14:00:22'),
    (2, 'Comercial Beta', '11.111.111/0001-11', '(85) 99999-0002', 'contato@beta.com', 'Maracanaú', 1, '2026-08-01 14:00:22');

-- =========================================================
-- DADOS: clientes
-- =========================================================
INSERT INTO clientes
    (id_cliente, nome, cpf, telefone, email, cidade, ativo, criado_em)
VALUES
    (1, 'Maria Silva', '000.000.000-00', '(85) 98888-1111', 'maria@email.com', 'Fortaleza', 1, '2026-08-01 15:09:10'),
    (2, 'João Souza LS', '111.111.111-11', '(85) 98888-2222', 'joao@email.com', 'Maracanaú', 1, '2026-08-01 15:09:10'),
    (3, 'Teste Usuário LS', '123456789-12', '85996968596', 'teste@teste.com', 'Fortaleza', 0, '2026-08-01 15:14:02');

-- =========================================================
-- DADOS: usuarios
-- Senha armazenada em SHA-256, conforme o sistema atual.
-- O hash informado corresponde à senha 123456.
-- =========================================================
INSERT INTO usuarios
    (id_usuario, nome, login, senha, perfil, ativo, data_cadastro)
VALUES
    (1, 'Master Admin', 'admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'ADMINISTRADOR', 1, '2026-07-11 10:37:20'),
    (2, 'Pedro Lima', 'operador', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'OPERADOR', 1, '2026-08-01 10:51:23');

-- =========================================================
-- DADOS: produtos
-- =========================================================
INSERT INTO produtos
    (
        id_produto,
        nome,
        descricao,
        valor_custo,
        valor_venda,
        quantidade_estoque,
        estoque_minimo,
        id_categoria,
        id_fornecedor,
        ativo,
        criado_em
    )
VALUES
    (1, 'Mouse USB', 'Mouse óptico com conexão USB', 25.00, 50.00, 32, 5, 1, 1, 1, '2026-08-01 14:00:43'),
    (2, 'Teclado USB', 'Teclado padrão ABNT2', 40.00, 80.00, 100, 5, 1, 1, 1, '2026-08-01 14:00:43'),
    (3, 'Papel A4', 'Resma de papel A4', 18.00, 30.00, 50, 10, 2, 2, 1, '2026-08-01 14:00:43'),
    (4, 'Detergente X', 'Detergente neutro 500ml', 2.00, 4.50, 110, 20, 3, 2, 1, '2026-08-01 14:00:43'),
    (5, 'teste', 'teste', 100.00, 180.00, 330, 50, 2, 2, 0, '2026-08-01 14:08:20');

-- =========================================================
-- DADOS: movimentacoes_estoque
-- Devem ser inseridos após os produtos.
-- =========================================================
INSERT INTO movimentacoes_estoque
    (
        id_movimentacao,
        id_produto,
        tipo,
        nr_notafiscal,
        quantidade,
        valor_unitario,
        observacao,
        criado_em
    )
VALUES
    (1, 4, 'ENTRADA', NULL, 50, 5.50, 'teste', '2026-08-01 15:12:07'),
    (2, 5, 'ENTRADA', NULL, 100, 5.00, 'teste', '2026-08-01 15:12:45'),
    (3, 5, 'ENTRADA', NULL, 200, 5.00, 'teste', '2026-08-01 15:15:01'),
    (4, 4, 'SAIDA', NULL, 150, 20.00, 'sdsd', '2026-08-01 15:15:22'),
    (5, 4, 'ENTRADA', NULL, 100, 12.50, 'teste', '2026-08-02 11:39:05'),
    (6, 4, 'ENTRADA', 'B5256', 10, 5.75, 'TESTE DE CONTEÚDO', '2026-08-02 14:33:46'),
    (7, 1, 'ENTRADA', 'JGB-4552', 30, 35.90, 'TESTE TESTE RTES teste produto.', '2026-08-02 16:20:45');

-- Ajusta os próximos valores de AUTO_INCREMENT.
ALTER TABLE categorias AUTO_INCREMENT = 4;
ALTER TABLE fornecedores AUTO_INCREMENT = 3;
ALTER TABLE clientes AUTO_INCREMENT = 4;
ALTER TABLE usuarios AUTO_INCREMENT = 3;
ALTER TABLE produtos AUTO_INCREMENT = 6;
ALTER TABLE movimentacoes_estoque AUTO_INCREMENT = 8;

-- =========================================================
-- CONSULTAS DE CONFERÊNCIA
-- =========================================================
SELECT 'categorias' AS tabela, COUNT(*) AS total FROM categorias
UNION ALL
SELECT 'fornecedores', COUNT(*) FROM fornecedores
UNION ALL
SELECT 'clientes', COUNT(*) FROM clientes
UNION ALL
SELECT 'usuarios', COUNT(*) FROM usuarios
UNION ALL
SELECT 'produtos', COUNT(*) FROM produtos
UNION ALL
SELECT 'movimentacoes_estoque', COUNT(*) FROM movimentacoes_estoque;

SELECT
    p.id_produto,
    p.nome AS produto,
    c.nome AS categoria,
    f.nome AS fornecedor,
    p.quantidade_estoque,
    p.estoque_minimo,
    p.valor_custo,
    p.valor_venda,
    p.ativo
FROM produtos AS p
INNER JOIN categorias AS c
    ON c.id_categoria = p.id_categoria
LEFT JOIN fornecedores AS f
    ON f.id_fornecedor = p.id_fornecedor
ORDER BY p.id_produto;
