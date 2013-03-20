package org.bigsense.db

sealed trait AggregateType { def sql : String ; def sensorType: String }
case object AggVolumeOverTime extends AggregateType { val sql =  "totalDataForInterval->sql"; val sensorType = "Volume" }
case object AggAverageTemperature extends AggregateType { val sql =  "averageDataForInterval->sql"; val sensorType = "Temperature" }
case object AggAverageFlow extends AggregateType { val sql =  "averageDataForInterval->sql"; val sensorType = "FlowRate" }

