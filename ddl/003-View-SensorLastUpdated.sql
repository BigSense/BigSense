USE [GreenOven]
GO

/****** Object:  View [dbo].[VW_SENSOR_LAST_REPORTED]    Script Date: 09/15/2011 07:31:52 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO



-- Displays the elapsed time since the last report of a sensor.

CREATE VIEW [dbo].[VW_SENSOR_LAST_REPORTED]
AS

SELECT
	LRT.SENSOR_ID,
	LRT.LAST_REPORT_TIME,
	CONVERT(VARCHAR(12),DATEADD(ms,DATEDIFF(ms,LRT.LAST_REPORT_TIME,GETDATE()),0),114)	AS TIME_SINCE_LAST_REPORT,
	SNS.unique_id	AS SENSOR_UID,
	TYP.name		AS SENSOR_TYPE
FROM
(
	SELECT
		sensor_id	AS SENSOR_ID,
		MAX(rtime)	AS LAST_REPORT_TIME
	FROM data_package PAK

	INNER JOIN sensor_data DAT ON
		PAK.id = DAT.package_id
	GROUP BY
		sensor_id
) LRT

INNER JOIN sensors SNS ON
	LRT.SENSOR_ID = SNS.id
	
INNER JOIN sensor_types TYP ON
	SNS.sensor_type = TYP.id
	

GO



