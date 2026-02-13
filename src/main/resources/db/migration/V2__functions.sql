CREATE OR REPLACE FUNCTION search_clientes_by_nombre(p_q TEXT)
RETURNS TABLE (
  id INT,
  nombre VARCHAR,
  apellido VARCHAR,
  razon_social VARCHAR,
  cuit VARCHAR,
  fecha_nacimiento DATE,
  telefono_celular VARCHAR,
  email VARCHAR,
  fecha_creacion TIMESTAMP,
  fecha_modificacion TIMESTAMP
)
AS $$
BEGIN
RETURN QUERY
SELECT c.id, c.nombre, c.apellido, c.razon_social, c.cuit, c.fecha_nacimiento,
       c.telefono_celular, c.email, c.fecha_creacion, c.fecha_modificacion
FROM clientes c
WHERE c.nombre ILIKE '%' || p_q || '%'
ORDER BY c.id ASC;
END;
$$ LANGUAGE plpgsql;