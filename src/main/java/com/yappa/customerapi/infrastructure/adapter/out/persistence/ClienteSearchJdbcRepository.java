package com.yappa.customerapi.infrastructure.adapter.out.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

import com.yappa.customerapi.domain.model.Cliente;
import com.yappa.customerapi.domain.model.Cuit;
import com.yappa.customerapi.domain.model.Email;
import com.yappa.customerapi.domain.model.Telefono;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteSearchJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public ClienteSearchJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Cliente> searchByNombre(String q) {
        return jdbcTemplate.query("SELECT * FROM search_clientes_by_nombre(?)", new Object[]{q}, new ClienteRowMapper());
    }

    private static class ClienteRowMapper implements RowMapper<Cliente> {
        @Override
        public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Cliente.reconstituir(
                    rs.getLong("id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("razon_social"),
                    new Cuit(rs.getString("cuit")),
                    rs.getDate("fecha_nacimiento").toLocalDate(),
                    new Telefono(rs.getString("telefono_celular")),
                    new Email(rs.getString("email")),
                    rs.getObject("fecha_creacion", OffsetDateTime.class),
                    rs.getObject("fecha_modificacion", OffsetDateTime.class)
            );
        }
    }
}
