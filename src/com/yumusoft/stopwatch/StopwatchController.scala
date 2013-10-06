/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import javafx.fxml.FXML
import javafx.scene.control.{Button, TableColumn, TableView, Label}
import javafx.event.ActionEvent
import java.net.URL
import java.util.{Date, ResourceBundle}
import javafx.animation.AnimationTimer

class StopwatchController {

  private val StartLabel = "_Start"
  private val PauseLabel = "_Pause"

  private var startTime: Option[Long] = None
  private var state: LongTimestamp = new LongTimestamp(0)

  @FXML var timeLabel: Label = _
  @FXML var timeLogTable: TableView[_] = _
  @FXML var numberColumn: TableColumn[_, _] = _
  @FXML var timeColumn: TableColumn[_, _] = _
  @FXML var startPauseButton: Button = _
  @FXML var lapButton: Button = _
  @FXML var resetButton: Button = _
  @FXML var clearButton: Button = _

  private val timer = new AnimationTimer() {
    def handle(currentTime: Long) {
      val currentMillis = currentTime / 1000000

      startTime match {
        case Some(time) => {
          val diff = currentMillis - time
          state += diff
          startTime = Some(currentMillis)
        }
        case None => {
          state = new LongTimestamp(0)
        }
      }

      updateTimeLabel()
    }
  }

  @FXML
  def doStartPause(event: ActionEvent) {
    if (startTime.isEmpty) {
      startTime = Some(System.currentTimeMillis())

      startPauseButton.setText(PauseLabel)
      timer.start()
    } else {
      startTime = None

      startPauseButton.setText(StartLabel)
      timer.stop()
    }
  }

  @FXML
  def doLap(event: ActionEvent) {

  }

  @FXML
  def doReset(event: ActionEvent) {
    if (startTime.isDefined) {
      doStartPause(event)
    }

    startTime = None
    state = new LongTimestamp(0)

    updateTimeLabel()
  }

  @FXML
  def doClear(event: ActionEvent) {

  }

  def updateTimeLabel() {
    timeLabel.setText(state.toString())
  }

}
