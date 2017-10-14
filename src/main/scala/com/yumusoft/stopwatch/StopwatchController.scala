/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */
package com.yumusoft.stopwatch

import javafx.fxml.{Initializable, FXML}
import javafx.scene.control._
import javafx.event.{EventHandler, ActionEvent}
import java.net.URL
import java.util.{TimerTask, Timer, ResourceBundle}

//import javafx.animation.AnimationTimer

import javafx.collections.{ListChangeListener, FXCollections, ObservableList}
import javafx.scene.control.cell.{TextFieldTableCell, PropertyValueFactory}
import javafx.scene.control.TableColumn.CellEditEvent
import javafx.application.Platform
import javafx.stage.FileChooser
import javafx.stage.FileChooser.ExtensionFilter
import java.io.{File, FileWriter}
import javafx.collections.ListChangeListener.Change
import javafx.scene.input._
import scala.Some

class StopwatchController extends Initializable {

  private val StartLabel = "_Start"
  private val PauseLabel = "_Pause"
  private val TimeFormat = """(\d\d:\d\d:\d\d\.\d\d\d\d)""".r

  @volatile
  private var startTime: Option[Long] = None
  @volatile
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
  @FXML var noteField: TextField = _
  var application: Main = _

  private var timer = new Timer()

  private def timerTask = new TimerTask {
    override def run() {
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

      Platform.runLater(new Runnable {
        override def run() {
          updateTimeLabel()
        }
      })
    }
  }

  @FXML
  def doStartPause(event: ActionEvent) {
    if (startTime.isEmpty) {
      startTime = Some(System.currentTimeMillis())

      startPauseButton.setText(PauseLabel)
      timer = new Timer()
      timer.scheduleAtFixedRate(timerTask, 500, 500)
    } else {
      startTime = None

      startPauseButton.setText(StartLabel)
      timer.cancel()
    }
  }

  @FXML
  def doLap(event: ActionEvent) {
    val selectedIndex = timeLogTable.getSelectionModel.getSelectedIndex

    if (selectedIndex >= 0) {
      val selectedLap = log.get(selectedIndex)

      if (selectedLap.getNote == noteField.getText) {
        selectedLap.updateTime(state)

        log.set(selectedIndex, selectedLap)
      } else {
        log.add(Lap(log.size() + 1, state, noteField.getText))
      }
    } else {
      log.add(Lap(log.size() + 1, state, noteField.getText))
    }
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
    timer.cancel()
    Platform.exit()
  }

  @FXML
  def doSave(event: ActionEvent) {
    if (saveFile.isEmpty) {
      doSaveAs(event)
    }

    saveFile.map {
      file =>
        val writer = new FileWriter(file)
        val laps = log.toArray(Array[Lap]())

        for (lap <- laps) {
          writer.write(s"${lap.getNumber},${lap.getTime},${lap.getNote}\n")
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

  @FXML
  def doTableViewClicked(event: MouseEvent) {
    if (event.getClickCount > 1 && startTime.isEmpty && state.time == 0) {

      val lap = timeLogTable.getSelectionModel.getSelectedItem

      state = new LongTimestamp(lap.getTimeStamp)
      noteField.setText(lap.getNote)

      updateTimeLabel()
    }
  }

  @FXML
  def doTableViewKeyTyped(event: KeyEvent) {
    val code = event.getCode
    if (code == KeyCode.DELETE || code == KeyCode.BACK_SPACE) {
      val selectedIndex = timeLogTable.getSelectionModel.getSelectedIndex
      if (selectedIndex >= 0) {
        log.remove(selectedIndex)
        for (i <- 0 until log.size()) {
          log.get(i).numberProperty.set((i + 1).toString)
        }
      }
    }
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
