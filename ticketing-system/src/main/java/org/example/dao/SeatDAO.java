package org.example.dao;

import org.example.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SeatDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Seat> findByConcertId(Long concertId) {
        String sql = "SELECT s.* FROM seats s JOIN concerts c ON s.venue_id = c.venue_id WHERE c.id = ?";
        return jdbcTemplate.query(sql, new SeatRowMapper(), concertId);
    }

    public List<Seat> findByVenueId(Long venueId) {
        String sql = "SELECT * FROM seats WHERE venue_id = ?";
        return jdbcTemplate.query(sql, new SeatRowMapper(), venueId);
    }

    private static class SeatRowMapper implements RowMapper<Seat> {
        @Override
        public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Seat(
                    rs.getLong("id"),
                    rs.getLong("venue_id"),
                    rs.getString("section"),
                    rs.getInt("number"),
                    rs.getInt("pos_x"),
                    rs.getInt("pos_y")
            );
        }
    }
}
