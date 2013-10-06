/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

class LongTimestamp(val time: Long) extends AnyVal {

  override def toString() = {
    val milliseconds = time % 1000
    val seconds = (time / 1000) % 60
    val minutes = (time / (1000 * 60)) % 60
    val hours = (time / (1000 * 3600)) % 60

    f"$hours%02d:$minutes%02d:$seconds%02d.$milliseconds%03d"
  }

  def +(other: Long) = {
    new LongTimestamp(time + other)
  }
}
