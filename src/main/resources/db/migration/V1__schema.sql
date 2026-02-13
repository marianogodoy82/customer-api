DROP TABLE IF EXISTS clientes;

CREATE TABLE clientes (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          apellido VARCHAR(100) NOT NULL,
                          razon_social VARCHAR(150) NOT NULL,
                          cuit VARCHAR(20) NOT NULL UNIQUE,
                          fecha_nacimiento DATE NOT NULL,
                          telefono_celular VARCHAR(30) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          fecha_creacion TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                          fecha_modificacion TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION set_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
  NEW.fecha_modificacion = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_clientes_fecha_modificacion ON clientes;
CREATE TRIGGER trg_clientes_fecha_modificacion
    BEFORE UPDATE ON clientes
    FOR EACH ROW
    EXECUTE FUNCTION set_fecha_modificacion();

INSERT INTO clientes (
    nombre, apellido, razon_social, cuit, fecha_nacimiento,
    telefono_celular, email
) VALUES
      ('Juan', 'Pérez', 'JP Servicios SRL', '20-12345678-9', '1985-06-15', '1165874210', 'juan.perez@example.com'),
      ('María', 'Gómez', 'MG Soluciones', '27-23456789-0', '1990-09-21', '1165874221', 'maria.gomez@example.com'),
      ('Carlos', 'López', 'CL Construcciones', '23-34567890-1', '1978-01-10', '1165874332', 'carlos.lopez@example.com'),
      ('Lucía', 'Martínez', 'LM Consultora', '27-45678901-2', '1992-03-05', '1165874443', 'lucia.martinez@example.com'),
      ('Diego', 'Fernández', 'DF Diseño', '20-56789012-3', '1988-11-22', '1165874554', 'diego.fernandez@example.com');

