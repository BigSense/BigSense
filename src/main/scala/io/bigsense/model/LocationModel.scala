/**
 *
 *
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.model

case class GPSModel(val location : Option[LocationModel],
               val delta : Option[DeltaModel],
               val accuracy : Option[AccuracyModel])

case class LocationModel(val longitude : Option[Double], val latitude : Option[Double],
                    val altitude : Option[Double])

case class DeltaModel(val speed : Option[Double], val climb : Option[Double], val track : Option[Double])

case class AccuracyModel(val longitudeError: Option[Double], val latitudeError: Option[Double],
                     val altitudeError: Option[Double], val speedError: Option[Double], val climbError: Option[Double],
                     val trackError: Option[Double])