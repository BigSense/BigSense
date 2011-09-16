USE [GreenOven]
GO
/****** Object:  Trigger [dbo].[transferToArcGIS]    Script Date: 09/16/2011 05:26:35 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER TRIGGER [dbo].[transferToArcGIS]
   ON  [GreenOven].[dbo].[sensor_data]
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
  
	SET @relay			= 'undefined'
	SET @sensor			= 'undefined'
	SET @sensor_type	= 'undefined'
	SET @sensor_data	= 'undefined'
	SET @sensor_units	= 'undefined'
  
  --Joined Infomration
	SELECT 
		@relay			= REL.unique_id, 
		@sensor			= SNS.unique_id, 
		@packTime		= PAK.rtime,
		@sensor_units	= SNS.units, 
		@sensor_type	= TYP.name, 
		@sensor_data	= DAT.data 
/*
		-- USED for debgugging only
		-- TODO: rename columns				
		DAT.id,
		DAT.package_id,
		DAT.sensor_id,
		DAT.data,
		PAK.rtime AS relay_time,
		PAK.relay_id,
		REL.unique_id as relay_unique_id,
		SNS.units as sensor_units,
		SNS.unique_id as sensor_unique_id,
		TYP.name as sensor_type				
*/		
	FROM GreenOven.dbo.sensor_data DAT
	
	LEFT OUTER JOIN GreenOven.dbo.data_package PAK ON 
		DAT.package_id = PAK.id
    
    LEFT OUTER JOIN GreenOven.dbo.relays REL ON 
		PAK.relay_id = REL.id
    
    LEFT OUTER JOIN GreenOven.dbo.sensors SNS ON 
		DAT.sensor_id = SNS.id
    
    LEFT JOIN GreenOven.dbo.sensor_types TYP ON 
		SNS.sensor_type = TYP.id

    WHERE 
		DAT.id=@dataID 
		AND DAT.package_id=@packID 
		AND DAT.sensor_id = @sensorID

  
	INSERT INTO [SPATIAL].dbo.CGC_DATA
	(
		--is OBJECTID auto increment identity? if not where is it getting set?
		relay,
		package_id,
		[datetime],
		sensor,
		sensor_type,
		sensor_data,
		sensor_units,
		sensor_id
	)
	VALUES
	(
		@relay,
		@packID,
		@packTime,
		@sensor,
		@sensor_type,
		@sensor_data,
		@sensor_units,
		@sensorID		
	)
    
END

