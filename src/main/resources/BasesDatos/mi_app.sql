DROP DATABASE miapp;
CREATE DATABASE miapp;

USE miapp;

CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password_hash VARCHAR(500) NOT NULL,
                          creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


