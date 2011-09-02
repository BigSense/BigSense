ALTER TRIGGER transferToArcGIS
   ON  GreenOven.dbo.sensor_data
   AFTER INSERT
AS 
BEGIN

	SET NOCOUNT ON;
	
  --Incomming Data
  DECLARE @dataID AS BIGINT
  DECLARE @packID AS BIGINT
  DECLARE @sensorID AS VARCHAR(20)
  SET @dataID = (SELECT id FROM INSERTED)
  SET @packID = (SELECT package_id FROM INSERTED)
  SET @sensorID = (SELECT sensor_id FROM INSERTED)
  
  --variables
  DECLARE @packTime AS DATETIME
  DECLARE @relay AS CHAR(36)
  DECLARE @sensor CHAR(20)
  DECLARE @sensor_type CHAR(20)
  DECLARE @sensor_data CHAR(50)
  DECLARE @sensor_units CHAR(20)
  
  
  --Joined Infomration
  SELECT  @packTime = pck.rtime,  @relay = rel.unique_id,  @sensor_data = dat.data, 
    @sensor_units = snsr.units, @sensor_type = typ.name, @sensor = snsr.unique_id
    FROM GreenOven.dbo.data_package pck 
    LEFT JOIN GreenOven.dbo.relays rel ON rel.id=relay_id 
    LEFT JOIN GreenOven.dbo.sensor_data dat ON dat.package_id=pck.id 
    LEFT JOIN GreenOven.dbo.sensors snsr ON snsr.relay_id=rel.id 
    LEFT JOIN GreenOven.dbo.sensor_types typ ON typ.id=snsr.sensor_type
    WHERE pck.id=@packID AND dat.id=@dataID AND dat.sensor_id=@sensorID
  
  INSERT INTO SpatialData.dbo.CGC_DATA(relay,package_id,datetime,sensor,sensor_type,sensor_data,sensor_units)
    VALUES(@relay,@packID,@packTime,@sensor,@sensor_type,@sensor_data,@sensor_units)
    
END
GO
