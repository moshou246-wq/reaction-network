-- 创建用户表
CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100),
  `real_name` VARCHAR(100),
  `status` INT DEFAULT 1,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建化合物表
CREATE TABLE IF NOT EXISTS `compounds` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(200) NOT NULL,
  `formula` VARCHAR(100) NOT NULL,
  `description` TEXT,
  `molar_mass` DECIMAL(10, 4),
  `energy` DECIMAL(15, 6),
  `image_url` VARCHAR(500),
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX `idx_name` (`name`),
  INDEX `idx_formula` (`formula`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建反应路径表
CREATE TABLE IF NOT EXISTS `reaction_paths` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `from_compound_id` BIGINT NOT NULL,
  `to_compound_id` BIGINT NOT NULL,
  `reaction_type` VARCHAR(100),
  `energy_change` DECIMAL(15, 6),
  `activation_energy` DECIMAL(15, 6),
  `description` TEXT,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`from_compound_id`) REFERENCES `compounds` (`id`),
  FOREIGN KEY (`to_compound_id`) REFERENCES `compounds` (`id`),
  INDEX `idx_from_compound` (`from_compound_id`),
  INDEX `idx_to_compound` (`to_compound_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入测试数据
INSERT INTO `users` (`username`, `password`, `email`, `real_name`, `status`) 
VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye4z/kycE7jXDKbVP0iWKvqJCwCSS8eha', 'admin@example.com', 'Administrator', 1),
('user', '$2a$10$N9qo8uLOickgx2ZMRZoMye4z/kycE7jXDKbVP0iWKvqJCwCSS8eha', 'user@example.com', 'Test User', 1);

INSERT INTO `compounds` (`name`, `formula`, `description`, `molar_mass`, `energy`) 
VALUES 
('Hydrogen', 'H2', 'Hydrogen gas', '2.016', '-286.0'),
('Oxygen', 'O2', 'Oxygen gas', '32.00', '-498.0'),
('Water', 'H2O', 'Water molecule', '18.015', '-572.0'),
('Carbon Dioxide', 'CO2', 'Carbon dioxide', '44.009', '-393.5'),
('Methane', 'CH4', 'Methane gas', '16.043', '-890.0');

INSERT INTO `reaction_paths` (`from_compound_id`, `to_compound_id`, `reaction_type`, `energy_change`, `activation_energy`) 
VALUES 
(1, 3, 'Oxidation', '-286.0', '50.0'),
(2, 3, 'Reduction', '-286.0', '45.0'),
(1, 4, 'Combustion', '-393.5', '100.0'),
(5, 1, 'Decomposition', '890.0', '200.0'),
(3, 4, 'Dehydration', '-108.0', '80.0');
