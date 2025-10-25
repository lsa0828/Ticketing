package org.example.dao;

import org.example.model.Concert;
import org.example.model.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ConcertDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Concert> findById(Long id) {
        String sql = "SELECT * FROM concerts WHERE id = ?";
        List<Concert> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Concert.class), id);
        return list.stream().findFirst();
    }
}
