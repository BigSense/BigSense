/**
 *
 *
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.model

class GPSModel(val location : LocationModel,
               val delta : Option[DeltaModel],
               val accuracy : Option[AccuracyModel])

class LocationModel(val longitude : Double, val latitude : Double,
                    val altitude : Double)

class DeltaModel(val speed : Double, val climb : Double, val track : Double)

class AccuracyModel(val longitudeError: Double, val latitudeError: Double,
                     val altitudeError: Double, val speedError: Double, val climbError: Double,
                     val trackError: Double)