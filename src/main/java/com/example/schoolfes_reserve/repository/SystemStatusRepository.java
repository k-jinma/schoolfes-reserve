package com.example.schoolfes_reserve.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.schoolfes_reserve.entity.SystemStatus;

@Repository
public class SystemStatusRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // システム状態を取得
    public SystemStatus findById(Long id) {
        String sql = "SELECT id, current_number, next_number FROM system_status WHERE id = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, this::mapRowToSystemStatus, id);
        } catch (EmptyResultDataAccessException e) {
            // 初期データが無い場合は作成
            return createInitialStatus();
        }
    }
    
    // システム状態を更新
    public void save(SystemStatus status) {
        String sql = """
            UPDATE system_status 
            SET current_number = ?, next_number = ? 
            WHERE id = ?
            """;
        
        int updated = jdbcTemplate.update(sql, 
            status.getCurrentNumber(), 
            status.getNextNumber(), 
            status.getId());
            
        if (updated == 0) {
            // レコードが存在しない場合は挿入
            String insertSql = """
                INSERT INTO system_status (id, current_number, next_number) 
                VALUES (?, ?, ?)
                """;
            jdbcTemplate.update(insertSql, 
                status.getId(), 
                status.getCurrentNumber(), 
                status.getNextNumber());
        }
    }
    
    private SystemStatus createInitialStatus() {
        SystemStatus status = new SystemStatus();
        status.setId(1L);
        status.setCurrentNumber(0);
        status.setNextNumber(1);
        save(status);
        return status;
    }
    
    private SystemStatus mapRowToSystemStatus(ResultSet rs, int rowNum) throws SQLException {
        SystemStatus status = new SystemStatus();
        status.setId(rs.getLong("id"));
        status.setCurrentNumber(rs.getInt("current_number"));
        status.setNextNumber(rs.getInt("next_number"));
        return status;
    }
}
