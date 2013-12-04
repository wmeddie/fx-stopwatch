/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import javafx.beans.property.{SimpleLongProperty, SimpleStringProperty}

case class Lap(numberProperty: SimpleStringProperty,
               timeProperty: SimpleStringProperty,
               timeStampProperty: SimpleLongProperty,
               noteProperty: SimpleStringProperty) {

  def getNumber = numberProperty.get()

  def getTime = timeProperty.get()

  def getTimeStamp = timeStampProperty.get()

  def getNote = noteProperty.get()

  def setNumber = numberProperty.set _

  def setTime = timeProperty.set _

  def setTimeStamp = timeStampProperty.set _

  def setNote = noteProperty.set _

  def updateTime(newTimeStamp: LongTimestamp) {
    timeProperty.set(newTimeStamp.toString())
    setTimeStamp(newTimeStamp.time)
  }
}

object Lap {
  def apply(number: Int, time: LongTimestamp, note: String) = {
    new Lap(
      new SimpleStringProperty(number.toString),
      new SimpleStringProperty(time.toString()),
      new SimpleLongProperty(time.time),
      new SimpleStringProperty(note))
  }
}
