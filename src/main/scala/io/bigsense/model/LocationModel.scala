/**
 *
 *
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.model

case class GPSModel(location : Option[LocationModel],
                    delta : Option[DeltaModel],
                    accuracy : Option[AccuracyModel])

case class LocationModel(longitude : Option[Double],
                         latitude : Option[Double],
                         altitude : Option[Double])

case class DeltaModel(speed : Option[Double], climb : Option[Double], track : Option[Double])

case class AccuracyModel(longitudeError: Option[Double], latitudeError: Option[Double],
                     altitudeError: Option[Double], speedError: Option[Double], climbError: Option[Double],
                     trackError: Option[Double])