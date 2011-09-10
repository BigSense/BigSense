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
  
  SET @relay = 'undefined'
  SET @sensor = 'undefined'
  SET @sensor_type = 'undefined'
  SET @sensor_data = 'undefined'
  SET @sensor_units = 'undefined'  
  
  --Joined Infomration
  SELECT @relay = relay.unique_id, @sensor = sen.unique_id, @packTime = pack.rtime,
    @sensor_units = sen.units, @sensor_type = tps.name, @sensor_data = data.data 
    FROM GreenOven.dbo.sensor_data data
    LEFT JOIN GreenOven.dbo.data_package pack ON pack.id = data.package_id
    LEFT JOIN GreenOven.dbo.relays relay ON relay.id = pack.relay_id
    LEFT JOIN GreenOven.dbo.sensors sen ON sen.relay_id = relay.id
    LEFT JOIN GreenOven.dbo.sensors sen2 ON sen2.id = data.sensor_id
    LEFT JOIN GreenOven.dbo.sensor_types tps ON tps.id = sen.sensor_type
    WHERE data.id=@dataID AND pack.id=@packID AND sen.id=@sensorID
  
  INSERT INTO SpatialData.dbo.CGC_DATA(relay,package_id,datetime,sensor,sensor_type,sensor_data,sensor_units)
    VALUES(@relay,@packID,@packTime,@sensor,@sensor_type,@sensor_data,@sensor_units)
    
END
GO
