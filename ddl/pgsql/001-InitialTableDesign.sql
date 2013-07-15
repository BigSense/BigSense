CREATE TABLE relays (
  id SERIAL PRIMARY KEY,
  unique_id VARCHAR(36),
  public_key VARCHAR(500)
);


GRANT SELECT,INSERT,UPDATE,DELETE ON relays TO db_bigsense;
GRANT USAGE ON SEQUENCE relays_id_seq TO db_bigsense;

CREATE TABLE sensor_types (
  id BIGINT PRIMARY KEY,
  name VARCHAR(20)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_types TO db_bigsense;

INSERT INTO sensor_types (id,name) VALUES(1,'Temperature');
INSERT INTO sensor_types (id,name) VALUES(2,'FlowRate');
INSERT INTO sensor_types (id,name) VALUES(3,'Volume');
INSERT INTO sensor_types (id,name) VALUES(4,'WindSpeed');

CREATE TABLE sensors (
  id SERIAL PRIMARY KEY,
  unique_id VARCHAR(50),
  relay_id BIGINT,
  sensor_type BIGINT,
  units VARCHAR(20),
  CONSTRAINT fk_sensors_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id),
  CONSTRAINT fk_sensors_type_id FOREIGN KEY  ( sensor_type ) REFERENCES sensor_types(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensors TO db_bigsense;
GRANT USAGE ON SEQUENCE sensors_id_seq TO db_bigsense;

CREATE TABLE data_package (
  id SERIAL PRIMARY KEY,
  rtime TIMESTAMP WITHOUT TIME ZONE,
  relay_id BIGINT,
  CONSTRAINT fk_package_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON data_package TO db_bigsense;
GRANT USAGE ON SEQUENCE data_package_id_seq TO db_bigsense;

CREATE TABLE sensor_data (
  id SERIAL PRIMARY KEY,
  package_id BIGINT,
  sensor_id BIGINT,
  data VARCHAR(50),
  CONSTRAINT fk_sensor_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id),
  CONSTRAINT fk_sensor_sensor_id  FOREIGN KEY (sensor_id) REFERENCES sensors(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_data TO db_bigsense;
GRANT USAGE ON SEQUENCE sensor_data_id_seq TO db_bigsense;

CREATE TABLE meta_data_types (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON meta_data_types TO db_bigsense;
GRANT USAGE ON SEQUENCE meta_data_types_id_seq TO db_bigsense;

CREATE TABLE relay_meta_data (
  relay_id BIGINT,
  meta_data_type_id BIGINT,
  val VARCHAR(255),
  CONSTRAINT fk_relay_meta_data_type_id FOREIGN KEY ( meta_data_type_id ) REFERENCES meta_data_types(id),
  CONSTRAINT fk_relay_meta_data_relays_id FOREIGN KEY ( relay_id ) REFERENCES relays(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON relay_meta_data TO db_bigsense;

CREATE TABLE sensor_meta_data (
  sensor_id BIGINT,
  meta_data_type_id BIGINT,
  val VARCHAR(255),
  CONSTRAINT fk_sensor_meta_data_type_id FOREIGN KEY ( meta_data_type_id ) REFERENCES meta_data_types(id),
  CONSTRAINT fk_sensor_meta_data_sensors_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_meta_data TO db_bigsense;
