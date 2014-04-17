CREATE TABLE sensor_locations (
    sensor_unique_id VARCHAR(50) PRIMARY KEY,
    location Geometry
);

INSERT INTO sensor_locations VALUES('VRFLO001',GeomFromText('POINT(39.1367 -84.5083)',4326));
INSERT INTO sensor_locations VALUES('VRVOL001',GeomFromText('POINT(39.1377 -84.5083)',4326));
INSERT INTO sensor_locations VALUES('VRTEMP001',GeomFromText('POINT(39.1387 -84.5083)',4326));

INSERT INTO sensor_locations VALUES('VRFLO002',GeomFromText('POINT(39.1391 -84.5196)',4326));
INSERT INTO sensor_locations VALUES('VRVOL002',GeomFromText('POINT(39.1401 -84.5196)',4326));
INSERT INTO sensor_locations VALUES('VRTEMP002',GeomFromText('POINT(39.1411 -84.5196)',4326));

INSERT INTO sensor_locations VALUES('VRFLO003',GeomFromText('POINT(39.1924 -84.5011)',4326));
INSERT INTO sensor_locations VALUES('VRVOL003',GeomFromText('POINT(39.1934 -84.5011)',4326));
INSERT INTO sensor_locations VALUES('VRTEMP003',GeomFromText('POINT(39.1944 -84.5011)',4326));

INSERT INTO sensor_locations VALUES('C50000000FCB8F1D',GeomFromText('POINT(39.1346 -84.5080)',4326));
INSERT INTO sensor_locations VALUES('CA0008017D06F110',GeomFromText('POINT(39.1346 -84.5080)',4326));