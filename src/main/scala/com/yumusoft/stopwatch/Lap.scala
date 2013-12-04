/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import scala.beans.BeanProperty
import javafx.beans.property.SimpleStringProperty

case class Lap(
                @BeanProperty number: String,
                @BeanProperty time: String,
                note: SimpleStringProperty) {

  def getNote = {
    note.get()
  }

  def setNote(value: String) = {
    note.set(value)
  }
}

object Lap {
  def apply(number: Int, time: LongTimestamp, note: String) = {
    new Lap(number.toString, time.toString(), new SimpleStringProperty(note))
  }
}
