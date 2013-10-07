/* Stopwatch. A JavaFX-based Stopwatch app.
 * Copyright 2013 Eduardo Gonzalez
 * See LICENSE for details.
 */

package com.yumusoft.stopwatch

import javafx.scene.{Scene, Parent}
import javafx.fxml.FXMLLoader
import javafx.stage.Stage
import javafx.application.Application

class Main extends Application {
  def start(primaryStage: Stage) {
    val root: Parent = FXMLLoader.load(getClass.getResource("stopwatch.fxml"))
    primaryStage.setTitle("Stopwatch")
    primaryStage.setScene(new Scene(root, 300, 420))
    primaryStage.setMinWidth(300)
    primaryStage.setMinHeight(200)
    primaryStage.show()
  }
}

object Main {
  def main(args: Array[String]) {
    Application.launch(classOf[Main])
  }
}
