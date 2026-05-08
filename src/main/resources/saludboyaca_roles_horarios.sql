-- Script complementario para roles y horarios (SaludBoyaca)
-- Ejecutar despues de crear la base de datos principal.

-- Roles requeridos por la especificacion:
-- MEDICO: acceso alto y CRUD completo.
-- RECEPCIONISTA: CRUD de pacientes y citas, horarios en solo lectura.
-- ENFERMERO: solo visualizacion; no puede editar ni eliminar.

ALTER TABLE usuarios MODIFY rol VARCHAR(30) NOT NULL;

CREATE TABLE IF NOT EXISTS horarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    dia_semana VARCHAR(20) NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    medico VARCHAR(120) NOT NULL,
    especialidad VARCHAR(80) NOT NULL,
    estado ENUM('DISPONIBLE','OCUPADO','INACTIVO') NOT NULL DEFAULT 'DISPONIBLE'
);

INSERT INTO horarios (dia_semana, hora_inicio, hora_fin, medico, especialidad, estado) VALUES
('Lunes', '08:00:00', '12:00:00', 'Carlos Ernesto Pedraza Rondon', 'Medicina General', 'DISPONIBLE'),
('Martes', '14:00:00', '18:00:00', 'Maria Eugenia Suarez Cely', 'Pediatria', 'DISPONIBLE'),
('Miercoles', '08:00:00', '12:00:00', 'Carlos Ernesto Pedraza Rondon', 'Medicina General', 'DISPONIBLE'),
('Jueves', '14:00:00', '18:00:00', 'Maria Eugenia Suarez Cely', 'Pediatria', 'DISPONIBLE'),
('Viernes', '08:00:00', '12:00:00', 'Carlos Ernesto Pedraza Rondon', 'Medicina General', 'DISPONIBLE');

-- Usuarios de prueba sugeridos por la especificacion.
INSERT INTO usuarios (nombres, apellidos, documento, email, username, password, rol, especialidad, institucion) VALUES
('Maria Eugenia', 'Suarez Cely', '1052345679', 'msuarez@saludboyaca.gov.co', 'msuarez', '123', 'MEDICO', 'Pediatria', 'Centro de Salud Municipal de Paipa'),
('Jorge Hernando', 'Baez Morales', '1052345680', 'jbaez@saludboyaca.gov.co', 'jbaez', '123', 'ENFERMERO', NULL, 'Centro de Salud Municipal de Paipa'),
('Paola Andrea', 'Perez Gomez', '1052345681', 'pperez@saludboyaca.gov.co', 'pperez', '123', 'RECEPCIONISTA', NULL, 'Centro de Salud Municipal de Paipa');
