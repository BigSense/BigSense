CREATE TABLE processors (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  class varchar(255)
);


CREATE TABLE sensor_processors (
  sensor_id BIGINT IDENTITY(1,1) PRIMARY KEY,
  processor_id BIGINT,
  issued    DATETIME,
  recalled  DATETIME,
  CONSTRAINT fk_sensors_processors_sensor_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id),
  CONSTRAINT fk_sensors_processors_processor_id FOREIGN KEY ( processor_id ) REFERENCES processors(id)
);

