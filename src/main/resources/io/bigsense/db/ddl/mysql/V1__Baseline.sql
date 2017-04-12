GRANT SELECT,INSERT,UPDATE,DELETE,EXECUTE ON ${dbDatabase}.* TO '${dbUser}'@'%' IDENTIFIED BY '${dbPass}';

delimiter $$
drop function if exists ${dbDatabase}.isnumeric $$
create function ${dbDatabase}.isnumeric (s varchar(255)) returns int deterministic
begin
set @match =
   '^(([0-9+-.$]{1})|([+-]?[$]?[0-9]*(([.]{1}[0-9]*)|([.]?[0-9]+))))$';

return if(s regexp @match, 1, 0);
end $$

delimiter ;

GRANT EXECUTE ON FUNCTION ${dbDatabase}.isnumeric TO '${dbUser}'@'%';

drop function if exists ${dbDatabase}.earth_dt;
create function ${dbDatabase}.earth_dt (p1 Geometry, p2long Double, p2lat Double) returns double deterministic
  return 6371000 * acos (
      cos ( radians(Y(p1)) )
      * cos( radians( p2lat ) )
      * cos( radians( p2long ) - radians(X(p1)) )
      + sin ( radians(Y(p1)) )
      * sin( radians( p2lat ) )
    );

GRANT EXECUTE ON FUNCTION ${dbDatabase}.earth_dt TO '${dbUser}'@'%';

CREATE TABLE relays (
  id BIGINT auto_increment PRIMARY KEY,
  unique_id VARCHAR(36),
  key_pem TEXT
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
  id BIGINT auto_increment PRIMARY KEY,
  unique_id VARCHAR(50),
  relay_id BIGINT,
  sensor_type BIGINT,
  units VARCHAR(20),
  CONSTRAINT fk_sensors_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id),
  CONSTRAINT fk_sensors_type_id FOREIGN KEY  ( sensor_type ) REFERENCES sensor_types(id)
);

CREATE TABLE data_package (
  id BIGINT auto_increment PRIMARY KEY,
  rtime DATETIME,
  relay_id BIGINT,
  CONSTRAINT fk_package_relay_id FOREIGN KEY ( relay_id ) REFERENCES relays(id)
);

CREATE TABLE sensor_data (
  id BIGINT auto_increment PRIMARY KEY,
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
  id BIGINT auto_increment PRIMARY KEY,
  package_id BIGINT,
  sensor_id BIGINT,
  image LONGBLOB,
  itime DATETIME,
  CONSTRAINT fk_sensor_images_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id),
  CONSTRAINT fk_sensor_image_sensor_id FOREIGN KEY ( sensor_id ) REFERENCES sensors(id)

);

INSERT INTO sensor_types (id,name) VALUES(5,'Image');

CREATE TABLE package_location (
    package_id BIGINT PRIMARY KEY,
    location Geometry,
    altitude DOUBLE,
    speed DOUBLE,
    climb DOUBLE,
    track DOUBLE,
    longitude_error DOUBLE,
    latitude_error DOUBLE,
    altitude_error DOUBLE,
    speed_error DOUBLE,
    climb_error DOUBLE,
    track_error DOUBLE,
    CONSTRAINT fk_data_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id)
);