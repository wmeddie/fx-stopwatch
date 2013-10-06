/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import javafx.beans.property.SimpleStringProperty
import scala.beans.BeanProperty

case class Lap(
  @BeanProperty number: SimpleStringProperty,
  @BeanProperty time: SimpleStringProperty
)

object Lap {
  def apply(number: Int, time: LongTimestamp) = {
    val numberProperty = new SimpleStringProperty(number.toString)
    val timeProperty = new SimpleStringProperty(time.toString)

    new Lap(numberProperty, timeProperty)
  }
}
