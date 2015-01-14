
CREATE TABLE sensor_locations (
    sensor_unique_id VARCHAR(50) PRIMARY KEY,
    location geography(POINT)
);

GRANT SELECT,INSERT,UPDATE,DELETE ON sensor_locations TO ${dbUser};


INSERT INTO sensor_locations VALUES('VRFLO001',ST_GeographyFromText('POINT(39.1367 -84.5083)'));
INSERT INTO sensor_locations VALUES('VRVOL001',ST_GeographyFromText('POINT(39.1377 -84.5083)'));
INSERT INTO sensor_locations VALUES('VRTEMP001',ST_GeographyFromText('POINT(39.1387 -84.5083)'));

INSERT INTO sensor_locations VALUES('VRFLO002',ST_GeographyFromText('POINT(39.1391 -84.5196)'));
INSERT INTO sensor_locations VALUES('VRVOL002',ST_GeographyFromText('POINT(39.1401 -84.5196)'));
INSERT INTO sensor_locations VALUES('VRTEMP002',ST_GeographyFromText('POINT(39.1411 -84.5196)'));

INSERT INTO sensor_locations VALUES('VRFLO003',ST_GeographyFromText('POINT(39.1924 -84.5011)'));
INSERT INTO sensor_locations VALUES('VRVOL003',ST_GeographyFromText('POINT(39.1934 -84.5011)'));
INSERT INTO sensor_locations VALUES('VRTEMP003',ST_GeographyFromText('POINT(39.1944 -84.5011)'));

INSERT INTO sensor_locations VALUES('C50000000FCB8F1D',ST_GeographyFromText('POINT(39.1346 -84.5080)'));
INSERT INTO sensor_locations VALUES('CA0008017D06F110',ST_GeographyFromText('POINT(39.1346 -84.5080)'));