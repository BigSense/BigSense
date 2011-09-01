CREATE TRIGGER [dbo].[transferToArcGIS]
   ON  [dbo].[GreenOven].[sensor_data]
   AFTER INSERT
AS 
BEGIN

	SET NOCOUNT ON;
	
  --Incomming Data
  SET @dataID = (SELECT id FROM INSERTED)
  SET @packID = (SELECT package_id FROM INSERTED)
  SET @sensorID = (SELECT sensor_id FROM INSERTED)
  
  --Joined Infomration
  SELECT  pck.rtime,  rel.unique_id AS relay_uid,  dat.data, snsr.units, 
  typ.name AS sensor_type, snsr.unique_id AS sensor_uid, dat.id AS data_id, pck.id AS package_id
  FROM data_package pck 
  LEFT JOIN relays rel ON rel.id=relay_id 
  LEFT JOIN sensor_data dat ON dat.package_id=pck.id 
  LEFT JOIN sensors snsr ON snsr.relay_id=rel.id 
  LEFT JOIN sensor_types typ ON typ.id=snsr.sensor_type
  WHERE pck.id=@packID AND dat.id=@dataID AND dat.sensor_id=@dataID
  