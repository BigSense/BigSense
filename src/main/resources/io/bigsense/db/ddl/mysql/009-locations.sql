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