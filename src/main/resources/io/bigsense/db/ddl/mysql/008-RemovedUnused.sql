ALTER TABLE sensor_processors DROP FOREIGN KEY fk_sensors_processors_sensor_id;
ALTER TABLE sensor_processors DROP FOREIGN KEY fk_sensors_processors_processor_id;
DROP TABLE processors;
DROP TABLE sensor_processors;
DROP TABLE sensor_locations;