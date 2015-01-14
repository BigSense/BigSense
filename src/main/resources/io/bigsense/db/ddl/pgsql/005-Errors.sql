CREATE TABLE sensor_errors (
  package_id BIGINT,
  error VARCHAR(255),
  CONSTRAINT fk_sensor_errors_package_id FOREIGN KEY ( package_id ) REFERENCES data_package(id)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_errors TO ${dbUser};
