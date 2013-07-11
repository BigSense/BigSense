CREATE TABLE processors (
  id SERIAL PRIMARY KEY,
  class varchar(255)
);


CREATE TABLE sensor_processors (
  sensor_id SERIAL PRIMARY KEY,
  processor_id BIGINT,
  issued    TIMESTAMP,
  recalled  TIMESTAMP,
  CONSTRAINT fk_sensors_processors_sensor_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id),
  CONSTRAINT fk_sensors_processors_processor_id FOREIGN KEY ( processor_id ) REFERENCES processors(id)
);

