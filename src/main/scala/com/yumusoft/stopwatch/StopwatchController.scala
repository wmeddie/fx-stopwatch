/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import javafx.fxml.{Initializable, FXML}
import javafx.scene.control._
import javafx.event.{EventHandler, ActionEvent}
import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.collections.{ListChangeListener, FXCollections, ObservableList}
import javafx.scene.control.cell.{TextFieldTableCell, PropertyValueFactory}
import javafx.scene.control.TableColumn.CellEditEvent
import javafx.application.Platform
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import scala.Some
import java.io.{File, FileWriter}
import javafx.collections.ListChangeListener.Change
import javafx.scene.input.{DataFormat, ClipboardContent, Clipboard}

class StopwatchController extends Initializable {

  private val StartLabel = "_Start"
  private val PauseLabel = "_Pause"
  private val TimeFormat = """(\d\d:\d\d:\d\d\.\d\d\d\d)""".r

  private var startTime: Option[Long] = None
  private var state: LongTimestamp = new LongTimestamp(0)
  private val log: ObservableList[Lap] = FXCollections.observableArrayList()

  private var saveFile: Option[File] = None

  @FXML var timeLabel: Label = _
  @FXML var timeLogTable: TableView[Lap] = _
  @FXML var numberColumn: TableColumn[Lap, String] = _
  @FXML var timeColumn: TableColumn[Lap, String] = _
  @FXML var notesColumn: TableColumn[Lap, String] = _
  @FXML var startPauseButton: Button = _
  @FXML var lapButton: Button = _
  @FXML var resetButton: Button = _
  @FXML var clearButton: Button = _
  @FXML var saveMenuItem: MenuItem = _
  @FXML var saveAsMenuItem: MenuItem = _
  var application: Main = _

  private val timer = new AnimationTimer() {
    def handle(currentNanos: Long) {
      val currentMillis = System.currentTimeMillis()

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

  // Menu Items

  @FXML
  def doQuit(event: ActionEvent) {
    Platform.exit()
  }

  @FXML
  def doSave(event: ActionEvent) {
    if (saveFile.isEmpty) {
      doSaveAs(event)
    }

    saveFile.map { file =>
      val writer = new FileWriter(file)
      val laps = log.toArray(Array[Lap]())

      for (lap <- laps) {
        writer.write(s"${lap.number},${lap.time},${lap.getNote}\n")
      }
      writer.close()
    }
  }

  @FXML
  def doSaveAs(event: ActionEvent) {
    val fileChooser = new FileChooser()

    val csvFilter = new ExtensionFilter("Comma Separated Values", "*.csv")
    fileChooser.getExtensionFilters.add(csvFilter)

    val file = fileChooser.showSaveDialog(timeLabel.getScene.getWindow)
    if (file != null) {
      saveFile = Some(file)
      doSave(event)
    }
  }

  @FXML
  def doCopy(event: ActionEvent) {
    val content = new ClipboardContent()
    content.putString(state.toString())

    val clipboard = Clipboard.getSystemClipboard
    clipboard.setContent(content)
  }

  @FXML
  def doAbout(event: ActionEvent) {
    application.showAboutDialog()
  }

  private def updateTimeLabel() {
    timeLabel.setText(state.toString())
  }

  def initialize(url: URL, bundle: ResourceBundle) {
    numberColumn.setCellValueFactory(new PropertyValueFactory[Lap, String]("number"))
    timeColumn.setCellValueFactory(new PropertyValueFactory[Lap, String]("time"))
    notesColumn.setCellValueFactory(new PropertyValueFactory[Lap, String]("note"))
    notesColumn.setCellFactory(TextFieldTableCell.forTableColumn())
    notesColumn.setOnEditCommit(new EventHandler[CellEditEvent[Lap, String]] {
      override def handle(event: CellEditEvent[Lap, String]) {
        val row = event.getRowValue
        val newVal = event.getNewValue
        row.setNote(newVal)
      }
    })

    timeLogTable.setItems(log)

    log.addListener(new ListChangeListener[Lap] {
      def onChanged(p1: Change[_ <: Lap]) {
        if (log.isEmpty) {
          saveMenuItem.setDisable(true)
          saveAsMenuItem.setDisable(true)
        } else {
          saveMenuItem.setDisable(false)
          saveAsMenuItem.setDisable(false)
        }
      }
    })
  }
}
