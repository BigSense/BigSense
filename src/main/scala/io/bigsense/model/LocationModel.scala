/**
 *
 *
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: BigSense</p>
 * @author Sumit Khanna <sumit@penguindreams.org>
 */
package io.bigsense.model

class LocationModel(val x : Double, val y : Double,
                    val accuracy : Double, val altitude : Double) extends ModelTrait {}