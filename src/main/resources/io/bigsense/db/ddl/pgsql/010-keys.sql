ALTER TABLE relays RENAME COLUMN public_key TO key_pem;
ALTER TABLE relays ALTER COLUMN key_pem TYPE TEXT;