CREATE TABLE relays (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  unique_id VARCHAR(20)
)

CREATE TABLE sensor_types (
  id BIGINT PRIMARY KEY,
  name VARCHAR(20)
)
INSERT INTO sensor_types (id,name) VALUES(1,'Temperature')
INSERT INTO sensor_types (id,name) VALUES(2,'Counter')

CREATE TABLE sensors (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  unique_id VARCHAR(20),
  relay_id BIGINT,
  sensor_type BIGINT,
  units VARCHAR(6),
  CONSTRAINT fk_sensors_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id),
  CONSTRAINT fk_sensors_type_id FOREIGN KEY  ( sensor_type ) REFERENCES sensor_types(id) 
)

CREATE TABLE data_package (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  rtime DATETIME,
  relay_id BIGINT,
  CONSTRAINT fk_package_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id)
)

CREATE TABLE sensor_data (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  package_id BIGINT,
  data VARCHAR(50),
  CONSTRAINT fk_sensor_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id)
)

CREATE TABLE meta_data_types (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  name VARCHAR(100)
)

CREATE TABLE relay_meta_data (
  relay_id BIGINT,
  meta_data_type_id BIGINT,
  val VARCHAR(255),
  CONSTRAINT fk_relay_meta_data_type_id FOREIGN KEY ( meta_data_type_id ) REFERENCES meta_data_types(id)
)

CREATE TABLE sensor_meta_data (
  sensor_id BIGINT,
  meta_data_type_id BIGINT,
  val VARCHAR(255),
  CONSTRAINT fk_sensor_meta_data_type_id FOREIGN KEY ( meta_data_type_id ) REFERENCES meta_data_types(id)
)
