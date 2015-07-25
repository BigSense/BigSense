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