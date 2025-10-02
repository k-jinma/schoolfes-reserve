
-- 予約テーブル
CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL,
    ticket_number INTEGER UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT '待機中',
    email_sent BOOLEAN DEFAULT FALSE
);

-- システム状態テーブル
CREATE TABLE IF NOT EXISTS system_status (
    id BIGINT PRIMARY KEY,
    current_number INTEGER DEFAULT 0,
    next_number INTEGER DEFAULT 1
);