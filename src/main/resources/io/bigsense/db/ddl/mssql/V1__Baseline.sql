CREATE LOGIN [${dbUser}] WITH PASSWORD=N'${dbPass}', DEFAULT_DATABASE=[${dbDatabase}], CHECK_EXPIRATION=OFF, CHECK_POLICY=OFF;
CREATE USER [${dbUser}] FOR LOGIN [${dbUser}];
EXEC sp_addrolemember N'db_datareader', N'${dbUser}';
EXEC sp_addrolemember N'db_datawriter', N'${dbUser}';

CREATE TABLE relays (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  unique_id VARCHAR(36),
  key_pem VARCHAR(MAX)
);

CREATE TABLE sensor_types (
  id BIGINT PRIMARY KEY,
  name VARCHAR(20)
);

INSERT INTO sensor_types (id,name) VALUES(1,'Temperature');
INSERT INTO sensor_types (id,name) VALUES(2,'FlowRate');
INSERT INTO sensor_types (id,name) VALUES(3,'Volume');
INSERT INTO sensor_types (id,name) VALUES(4,'WindSpeed');

CREATE TABLE sensors (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  unique_id VARCHAR(50),
  relay_id BIGINT,
  sensor_type BIGINT,
  units VARCHAR(20),
  CONSTRAINT fk_sensors_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id),
  CONSTRAINT fk_sensors_type_id FOREIGN KEY  ( sensor_type ) REFERENCES sensor_types(id)
);

CREATE TABLE data_package (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  rtime DATETIME,
  relay_id BIGINT,
  CONSTRAINT fk_package_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id),
);

CREATE TABLE sensor_data (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  package_id BIGINT,
  sensor_id BIGINT,
  data VARCHAR(50),
  CONSTRAINT fk_sensor_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id),
  CONSTRAINT fk_sensor_sensor_id  FOREIGN KEY (sensor_id) REFERENCES sensors(id)
);

CREATE TABLE sensor_errors (
  package_id BIGINT,
  error VARCHAR(255),
  CONSTRAINT fk_sensor_errors_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id)
);

CREATE TABLE sensor_images (
  id BIGINT IDENTITY(1,1) PRIMARY KEY,
  package_id BIGINT,
  sensor_id BIGINT,
  image IMAGE,
  itime DATETIME,
  CONSTRAINT fk_sensor_images_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id),
  CONSTRAINT fk_sensor_image_sensor_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id)

);

INSERT INTO sensor_types (id,name) VALUES(5,'Image');

CREATE TABLE package_location (
    package_id BIGINT PRIMARY KEY,
    location geography,
    altitude double precision,
    speed double precision,
    climb double precision,
    track double precision,
    longitude_error double precision,
    latitude_error double precision,
    altitude_error double precision,
    speed_error double precision,
    climb_error double precision,
    track_error double precision,
    CONSTRAINT fk_data_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id)
);