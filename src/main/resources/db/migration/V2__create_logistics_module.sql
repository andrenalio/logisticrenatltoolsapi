-- ===============================
--  TABELAS BASE DO MÓDULO LOGÍSTICO
-- ===============================

-- 1. Tabela: Client
CREATE TABLE client (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    document VARCHAR(50),
    phone VARCHAR(50),
    email VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabela: Address
CREATE TABLE address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(50),
    complement VARCHAR(255),
    neighborhood VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    state VARCHAR(100) NOT NULL,
    zip_code VARCHAR(20),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabela: Driver
CREATE TABLE driver (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    vehicle_plate VARCHAR(50),
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabela: DeliveryRequest
CREATE TABLE delivery_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_type VARCHAR(20) NOT NULL, -- ENTREGA / RETIRADA / TROCA
    request_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    scheduled_date TIMESTAMP,
    status VARCHAR(30) DEFAULT 'CRIADA',
    notes TEXT,

    client_id BIGINT,
    driver_id BIGINT,
    address_id BIGINT,
    created_by_user_id BIGINT, -- 👈 novo campo (usuário que criou)

    CONSTRAINT fk_delivery_request_client FOREIGN KEY (client_id) REFERENCES client(id),
    CONSTRAINT fk_delivery_request_driver FOREIGN KEY (driver_id) REFERENCES driver(id),
    CONSTRAINT fk_delivery_request_address FOREIGN KEY (address_id) REFERENCES address(id),
    CONSTRAINT fk_delivery_request_user FOREIGN KEY (created_by_user_id) REFERENCES user(id)
);

-- 5. Tabela: DeliveryItem
CREATE TABLE delivery_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    delivery_request_id BIGINT NOT NULL,
    equipment_name VARCHAR(255) NOT NULL,
    quantity INT DEFAULT 1,
    condition_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_delivery_item_request FOREIGN KEY (delivery_request_id) REFERENCES delivery_request(id)
);

-- 6. Tabela: WorkshopCheck
CREATE TABLE workshop_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    delivery_request_id BIGINT NOT NULL,
    general_condition VARCHAR(255),
    functioning_test BOOLEAN,
    damage_report TEXT,
    observations TEXT,
    checked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_workshop_check_request FOREIGN KEY (delivery_request_id) REFERENCES delivery_request(id)
);

-- 7. Tabela: ServiceOrder
CREATE TABLE service_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    delivery_request_id BIGINT,
    client_name VARCHAR(255),
    equipment_name VARCHAR(255),
    address_description TEXT,
    order_type VARCHAR(50), -- TROCA / DEVOLUÇÃO / CONSERTO / SUBSTITUIÇÃO / FINALIZAÇÃO
    defect_description TEXT,
    invoice_number VARCHAR(50),
    invoice_series VARCHAR(50),
    invoice_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_service_order_request FOREIGN KEY (delivery_request_id) REFERENCES delivery_request(id)
);

-- ===============================
--  ÍNDICES DE OTIMIZAÇÃO
-- ===============================
CREATE INDEX idx_delivery_request_client ON delivery_request(client_id);
CREATE INDEX idx_delivery_request_driver ON delivery_request(driver_id);
CREATE INDEX idx_delivery_request_user ON delivery_request(created_by_user_id);
CREATE INDEX idx_delivery_item_request ON delivery_item(delivery_request_id);
CREATE INDEX idx_workshop_check_request ON workshop_check(delivery_request_id);
CREATE INDEX idx_service_order_request ON service_order(delivery_request_id);
