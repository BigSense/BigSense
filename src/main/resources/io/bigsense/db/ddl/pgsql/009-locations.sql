ALTER TABLE data_package ADD COLUMN location geography(POINT);
ALTER TABLE data_package ADD COLUMN accuracy decimal;
ALTER TABLE data_package ADD COLUMN altitude decimal;