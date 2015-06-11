package io.bigsense.db

sealed trait AggregateType { def sql : String }
case object AggregateSum extends AggregateType { val sql =  "totalDataForInterval->sql" }
case object AggregateAverage extends AggregateType { val sql =  "averageDataForInterval->sql" }
