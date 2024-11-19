create database eventitobd;
use eventitobd;

CREATE TABLE Usuarios(
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    tipo_usuario ENUM('visitante', 'organizador', 'colaborador') NOT NULL,
    nome_usuario VARCHAR(100) NOT NULL,
    email_usuario VARCHAR(100) NOT NULL,
    qr_code_usuario VARCHAR(255),
    conquistas_usuario TEXT,
    CHECK (tipo_usuario != 'visitante' OR qr_code_usuario IS NOT NULL),
    CHECK (tipo_usuario != 'visitante' OR conquistas_usuario IS NOT NULL)
);

CREATE TABLE Eventos(
    id_evento INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    nome_evento VARCHAR(100) NOT NULL,
    data_evento DATE NOT NULL,
    local VARCHAR(200) NOT NULL,
    total_checkins INT DEFAULT 0,
    total_interacoes INT DEFAULT 0,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Interacoes(
    id_interacao INT AUTO_INCREMENT PRIMARY KEY,
    id_evento INT NOT NULL,
    id_usuario INT NOT NULL,
    tipo ENUM('checkin', 'feedback', 'leitura_qr') NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_evento) REFERENCES Eventos(id_evento),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Conquistas(
    id_conquistas INT AUTO_INCREMENT PRIMARY KEY,
    id_evento INT NOT NULL,
    descricao_conquista VARCHAR(255) NOT NULL,
    checkins_realizados INT DEFAULT 0, 
    pontos INT DEFAULT 0,
    FOREIGN KEY (id_evento) REFERENCES Eventos(id_evento)
);
