/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import scala.beans.BeanProperty

case class Lap(
  @BeanProperty number: String,
  @BeanProperty time: String
)

object Lap {
  def apply(number: Int, time: LongTimestamp) = {
    new Lap(number.toString, time.toString())
  }
}
