/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */

package com.yumusoft.stopwatch

import javafx.scene.{Scene, Parent}
import javafx.fxml.FXMLLoader
import javafx.stage.{StageStyle, Stage}
import javafx.application.{Platform, Application}

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

    primaryWindow = Some(primaryStage)

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
