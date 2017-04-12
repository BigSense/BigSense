DROP ROLE IF EXISTS ${dbUser};
CREATE ROLE ${dbUser} LOGIN PASSWORD '${dbPass}';

GRANT ALL ON DATABASE ${dbDatabase} to ${dbUser};

CREATE EXTENSION postgis;

CREATE OR REPLACE FUNCTION isnumeric(text) RETURNS BOOLEAN AS $$
DECLARE x NUMERIC;
BEGIN
    x = $1::NUMERIC;
    RETURN TRUE;
EXCEPTION WHEN others THEN
    RETURN FALSE;
END;
$$ LANGUAGE plpgsql IMMUTABLE;


CREATE TABLE relays (
  id SERIAL PRIMARY KEY,
  unique_id VARCHAR(36),
  key_pem TEXT
);


GRANT SELECT,INSERT,UPDATE,DELETE ON relays TO ${dbUser};
GRANT USAGE ON SEQUENCE relays_id_seq TO ${dbUser};

CREATE TABLE sensor_types (
  id BIGINT PRIMARY KEY,
  name VARCHAR(20)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_types TO ${dbUser};

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

GRANT SELECT,INSERT,UPDATE,DELETE ON sensors TO ${dbUser};
GRANT USAGE ON SEQUENCE sensors_id_seq TO ${dbUser};

CREATE TABLE data_package (
  id SERIAL PRIMARY KEY,
  rtime TIMESTAMP WITHOUT TIME ZONE,
  relay_id BIGINT,
  CONSTRAINT fk_package_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON data_package TO ${dbUser};
GRANT USAGE ON SEQUENCE data_package_id_seq TO ${dbUser};

CREATE TABLE sensor_data (
  id SERIAL PRIMARY KEY,
  package_id BIGINT,
  sensor_id BIGINT,
  data VARCHAR(50),
  CONSTRAINT fk_sensor_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id),
  CONSTRAINT fk_sensor_sensor_id  FOREIGN KEY (sensor_id) REFERENCES sensors(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_data TO ${dbUser};
GRANT USAGE ON SEQUENCE sensor_data_id_seq TO ${dbUser};

CREATE TABLE sensor_errors (
  package_id BIGINT,
  error VARCHAR(255),
  CONSTRAINT fk_sensor_errors_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_errors TO ${dbUser};

CREATE TABLE sensor_images (
  id SERIAL PRIMARY KEY,
  package_id BIGINT,
  sensor_id BIGINT,
  image BYTEA,
  itime TIMESTAMP WITHOUT TIME ZONE,
  CONSTRAINT fk_sensor_images_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id),
  CONSTRAINT fk_sensor_image_sensor_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id)

);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_images TO ${dbUser};
GRANT USAGE ON SEQUENCE sensor_images_id_seq TO ${dbUser};

INSERT INTO sensor_types (id,name) VALUES(5,'Image');

CREATE TABLE package_location (
    package_id INTEGER PRIMARY KEY,
    location geography(POINT),
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

GRANT SELECT,INSERT,UPDATE,DELETE ON package_location TO ${dbUser};
