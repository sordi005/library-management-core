-- =====================================================================
-- DATOS DE PRUEBA PARA LIBRARY MANAGEMENT SYSTEM
-- =====================================================================

-- Insertar direcciones de ejemplo
INSERT INTO address (id, street, number, city, province, created_at, updated_at) VALUES
('Av. Corrientes', '1234', 'Buenos Aires', 'CABA', NOW(), NOW()),
('Calle San Martín', '567', 'Rosario', 'Santa Fe', NOW(), NOW()),
('Av. Libertador', '890', 'Córdoba', 'Córdoba', NOW(), NOW()),
('Calle Belgrano', '321', 'Mendoza', 'Mendoza', NOW(), NOW()),
('Av. 9 de Julio', '654', 'La Plata', 'Buenos Aires', NOW(), NOW());

-- Insertar usuarios de ejemplo
INSERT INTO user (id, first_name, last_name, email, phone_number, dni, date_of_birth, address_id, created_at, updated_at) VALUES
(1, 'Juan', 'Pérez', 'juan.perez@email.com', '11-1234-5678', '12345678', '1990-05-15', 1, NOW(), NOW()),
(2, 'María', 'González', 'maria.gonzalez@email.com', '11-8765-4321', '87654321', '1985-08-22', 2, NOW(), NOW()),
(3, 'Carlos', 'López', 'carlos.lopez@email.com', '11-5555-1111', '11111111', '1992-12-03', 3, NOW(), NOW()),
(4, 'Ana', 'Martínez', 'ana.martinez@email.com', '11-9999-2222', '22222222', '1988-03-18', 4, NOW(), NOW()),
(5, 'Pedro', 'Rodriguez', 'pedro.rodriguez@email.com', '11-7777-3333', '33333333', '1995-07-10', 5, NOW(), NOW());

-- Resetear auto_increment para próximos registros
ALTER TABLE address AUTO_INCREMENT = 6;
ALTER TABLE user AUTO_INCREMENT = 6;
