
ALTER TABLE data_package ADD CONSTRAINT fk_relay_meta_data_relays_id FOREIGN KEY ( relay_id ) REFERENCES relays(id)
ALTER TABLE sensor_data ADD CONSTRAINT fk_sensor_meta_data_sensors_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id) 
