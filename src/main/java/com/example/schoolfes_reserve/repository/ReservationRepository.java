package com.example.schoolfes_reserve.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.schoolfes_reserve.entity.Reservation;

@Repository
public class ReservationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // 予約を保存
    public Reservation save(Reservation reservation) {
        String sql = """
            INSERT INTO reservations (name, email, ticket_number, created_at, status, email_sent) 
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, reservation.getName());
            ps.setString(2, reservation.getEmail());
            ps.setInt(3, reservation.getTicketNumber());
            ps.setTimestamp(4, Timestamp.valueOf(reservation.getCreatedAt()));
            ps.setString(5, reservation.getStatus());
            ps.setBoolean(6, reservation.getEmailSent());
            return ps;
        }, keyHolder);
        
        // IDのみを取得
        Number generatedId = (Number) keyHolder.getKeys().get("ID");
        reservation.setId(generatedId.longValue());
        
        return reservation;
    }
    
    // 待機中の予約を取得
    public List<Reservation> findWaitingList() {
        String sql = """
            SELECT id, name, email, ticket_number, created_at, status, email_sent 
            FROM reservations 
            WHERE status = '待機中' 
            ORDER BY ticket_number
            """;
        
        return jdbcTemplate.query(sql, this::mapRowToReservation);
    }
    
    // 全ての予約を取得
    public List<Reservation> findAll() {
        String sql = """
            SELECT id, name, email, ticket_number, created_at, status, email_sent 
            FROM reservations 
            ORDER BY ticket_number
            """;
        
        return jdbcTemplate.query(sql, this::mapRowToReservation);
    }
    
    // 待機中の件数を取得
    public int countWaitingReservations() {
        String sql = "SELECT COUNT(*) FROM reservations WHERE status = '待機中'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
    
    // 番号で予約を検索
    public Reservation findByTicketNumber(int ticketNumber) {
        String sql = """
            SELECT id, name, email, ticket_number, created_at, status, email_sent 
            FROM reservations 
            WHERE ticket_number = ? AND status = '待機中'
            """;
        
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToReservation, ticketNumber);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
    
    // メール送信フラグを更新
    public void updateEmailSent(Long id, boolean emailSent) {
        String sql = "UPDATE reservations SET email_sent = ? WHERE id = ?";
        jdbcTemplate.update(sql, emailSent, id);
    }
    
    // 予約状況を更新
    public void updateStatus(Long id, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, id);
    }
    
    // 番号で状態を更新
    public void updateStatusByTicketNumber(int ticketNumber, String status) {
        String sql = "UPDATE reservations SET status = ? WHERE ticket_number = ?";
        jdbcTemplate.update(sql, status, ticketNumber);
    }
    
    // ResultSetをReservationオブジェクトにマッピング
    private Reservation mapRowToReservation(ResultSet rs, int rowNum) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setId(rs.getLong("id"));
        reservation.setName(rs.getString("name"));
        reservation.setEmail(rs.getString("email"));
        reservation.setTicketNumber(rs.getInt("ticket_number"));
        reservation.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        reservation.setStatus(rs.getString("status"));
        reservation.setEmailSent(rs.getBoolean("email_sent"));
        return reservation;
    }

    public int countRemainingAfter(int currentNumber) {
        String sql = """
            SELECT COUNT(*)
              FROM reservations
             WHERE ticket_number > ?
               AND status <> '完了'
            """;
        Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, currentNumber);
        return cnt == null ? 0 : cnt;
    }
}
