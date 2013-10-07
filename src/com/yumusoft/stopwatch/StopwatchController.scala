/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import javafx.fxml.{Initializable, FXML}
import javafx.scene.control.{Button, TableColumn, TableView, Label}
import javafx.event.{EventHandler, ActionEvent}
import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control.cell.{TextFieldTableCell, PropertyValueFactory}
import javafx.scene.control.TableColumn.CellEditEvent

class StopwatchController extends Initializable {

  private val StartLabel = "_Start"
  private val PauseLabel = "_Pause"

  private var startTime: Option[Long] = None
  private var state: LongTimestamp = new LongTimestamp(0)
  private val log: ObservableList[Lap] = FXCollections.observableArrayList()

  @FXML var timeLabel: Label = _
  @FXML var timeLogTable: TableView[Lap] = _
  @FXML var numberColumn: TableColumn[Lap, String] = _
  @FXML var timeColumn: TableColumn[Lap, String] = _
  @FXML var notesColumn: TableColumn[Lap, String] = _
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
    log.add(Lap(log.size() + 1, state))
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
    log.clear()
  }

  def updateTimeLabel() {
    timeLabel.setText(state.toString())
  }

  def initialize(url: URL, bundle: ResourceBundle) {
    numberColumn.setCellValueFactory(new PropertyValueFactory[Lap, String]("number"))
    timeColumn.setCellValueFactory(new PropertyValueFactory[Lap, String]("time"))
    notesColumn.setCellValueFactory(new PropertyValueFactory[Lap, String]("notes"))
    notesColumn.setCellFactory(TextFieldTableCell.forTableColumn())
    notesColumn.setOnEditCommit(new EventHandler[CellEditEvent[Lap, String]] {
      override def handle(event: CellEditEvent[Lap, String]) {
        val row = event.getRowValue
        val newVal = event.getNewValue
        row.setNotes(newVal)
      }
    })

    timeLogTable.setItems(log)
  }
}
