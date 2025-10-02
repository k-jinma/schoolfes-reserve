-- システム状態の初期データ
INSERT INTO system_status (id, current_number, next_number) 
SELECT 1, 0, 1 
WHERE NOT EXISTS (SELECT 1 FROM system_status WHERE id = 1);