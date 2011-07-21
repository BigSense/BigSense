CREATE TABLE relays (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  unique_id VARCHAR(20)
)

CREATE TABLE sensors (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  unique_id VARCHAR(20),
  relay_id BIGINT,
  sensor_type VARCHAR(20),
  units VARCHAR(6),
  FOREIGN KEY fk_relay_id ( relay_id ) REFERENCES relays(id),
  FOREIGN KEY fk_type_id ( sensor_type ) REFERENCES sensor_types(id) 
)

CREATE TABLE sensor_types (
  id BIGINT PRIMARY KEY,
  name VARCHAR(20)
)

CREATE TABLE data_package (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  rtime DATETIME,
  relay_id BIGINT,
  FOREIGN KEY fk_relay_id ( relay_id ) REFERENCES relays(id)
)

CREATE TABLE sensor_data (
  id
  package_id
  data
)

CREATE TABLE relay_meta_data (
  relay_id
  var
  value
)

CREATE TABLE sensor_meta_data (
  sensor_id
  var
  value
)
