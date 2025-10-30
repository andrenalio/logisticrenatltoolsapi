-- =========================================================
-- Flyway Migration: V1__create_user_structure.sql
-- Estrutura inicial de usuários, perfis e permissões
-- =========================================================

-- =============== TABLE: permission =======================
CREATE TABLE IF NOT EXISTS permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============== TABLE: profile ==========================
CREATE TABLE IF NOT EXISTS profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============== TABLE: profile_permission ===============
CREATE TABLE IF NOT EXISTS profile_permission (
    profile_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (profile_id, permission_id),
    CONSTRAINT fk_profile_permission_profile FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE,
    CONSTRAINT fk_profile_permission_permission FOREIGN KEY (permission_id) REFERENCES permission(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============== TABLE: user =============================
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150),
    email VARCHAR(150) NOT NULL UNIQUE,
    position VARCHAR(100),
    active BOOLEAN DEFAULT TRUE,
    type VARCHAR(50) DEFAULT 'HUMANO',
    cognito_user_id VARCHAR(255),
    perfil_id BIGINT,
    date_register DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_profile FOREIGN KEY (perfil_id) REFERENCES profile(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================================================
-- Inserção de dados iniciais de perfil e permissões
-- =========================================================

-- Perfis base
INSERT INTO profile (name) VALUES ('Administrator'), ('Manager'), ('Technician');

-- Permissões base
INSERT INTO permission (name) VALUES
('USER_CREATE'),
('USER_VIEW'),
('USER_EDIT'),
('USER_DELETE'),
('EQUIPMENT_VIEW'),
('EQUIPMENT_EDIT'),
('MAINTENANCE_VIEW'),
('MAINTENANCE_EDIT');

-- Associação de permissões aos perfis
-- Administrator: todas as permissões
INSERT INTO profile_permission (profile_id, permission_id)
SELECT 1, p.id FROM permission p;

-- Manager: apenas visualizar e editar equipamentos e manutenção
INSERT INTO profile_permission (profile_id, permission_id)
SELECT 2, p.id FROM permission p WHERE p.name IN ('EQUIPMENT_VIEW', 'EQUIPMENT_EDIT', 'MAINTENANCE_VIEW', 'MAINTENANCE_EDIT');

-- Technician: somente visualizar manutenção
INSERT INTO profile_permission (profile_id, permission_id)
SELECT 3, p.id FROM permission p WHERE p.name IN ('MAINTENANCE_VIEW');

-- =========================================================
-- Final da migração
-- =========================================================
