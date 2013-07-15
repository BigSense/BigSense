CREATE TABLE processors (
  id SERIAL PRIMARY KEY,
  class varchar(255)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON processors TO db_bigsense;
GRANT USAGE ON SEQUENCE processors_id_seq TO db_bigsense;


CREATE TABLE sensor_processors (
  sensor_id SERIAL PRIMARY KEY,
  processor_id BIGINT,
  issued    TIMESTAMP WITHOUT TIME ZONE,
  recalled  TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT fk_sensors_processors_sensor_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id),
  CONSTRAINT fk_sensors_processors_processor_id FOREIGN KEY ( processor_id ) REFERENCES processors(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_processors TO db_bigsense;
GRANT USAGE ON SEQUENCE sensor_processors_sensor_id_seq TO db_bigsense;

