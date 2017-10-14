/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */

package com.yumusoft.stopwatch

import java.beans.EventHandler
import javafx.scene.{Parent, Scene}
import javafx.fxml.FXMLLoader
import javafx.stage.{Stage, StageStyle, WindowEvent}
import javafx.application.{Application, Platform}

class Main extends Application {
  private var primaryWindow: Option[Stage] = None
  private var aboutWindow: Option[Stage] = None

  def start(primaryStage: Stage) {
    val rootLoader = new FXMLLoader(getClass.getResource("stopwatch.fxml"))

    val root: Parent = rootLoader.load().asInstanceOf[Parent]
    val rootController = rootLoader.getController[StopwatchController]

    rootController.application = this

    primaryStage.setTitle("Stopwatch")
    primaryStage.setScene(new Scene(root, 300, 420))
    primaryStage.setMinWidth(300)
    primaryStage.setMinHeight(200)
    primaryStage.setAlwaysOnTop(true)
    primaryStage.setOnHidden(new javafx.event.EventHandler[WindowEvent] {
      override def handle(event: WindowEvent): Unit = rootController.doQuit(null)
    })

    primaryWindow = Some(primaryStage)

    Platform.setImplicitExit(true)

    primaryStage.show()
  }

  private[stopwatch] def showAboutDialog() {
    aboutWindow.map { stage =>
      stage.show()
    }.getOrElse {
      val about: Parent = FXMLLoader.load(getClass.getResource("about.fxml"))

      val stage = new Stage()

      stage.initStyle(StageStyle.UTILITY)

      stage.setTitle("About Stopwatch")
      stage.setScene(new Scene(about, 300, 170))

      stage.setMinWidth(300)
      stage.setMinHeight(170)

      aboutWindow = Some(stage)

      stage.show()
    }
  }
}

object Main {
  def main(args: Array[String]) {
    Application.launch(classOf[Main])
  }
}
