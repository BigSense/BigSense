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