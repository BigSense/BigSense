EXEC sp_rename 'relays.[public_key]', 'key_pem', 'COLUMN';
ALTER TABLE relays ALTER COLUMN key_pem VARCHAR(MAX);