-- ==========================================
--  Script: V1__create_user_structure.sql
--  Objetivo: Estrutura inicial de usuários e perfis
-- ==========================================

CREATE TABLE IF NOT EXISTS perfil (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS permissao (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS perfil_permissao (
    perfil_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (perfil_id, permissao_id),
    CONSTRAINT fk_perfil_permissao_perfil FOREIGN KEY (perfil_id) REFERENCES perfil(id) ON DELETE CASCADE,
    CONSTRAINT fk_perfil_permissao_permissao FOREIGN KEY (permissao_id) REFERENCES permissao(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    cognito_sub VARCHAR(255), -- ID do usuário no Cognito
    perfil_id BIGINT,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_perfil FOREIGN KEY (perfil_id) REFERENCES perfil(id)
);

-- Índices adicionais
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_perfil_nome ON perfil(nome);
CREATE INDEX idx_permissao_nome ON permissao(nome);
