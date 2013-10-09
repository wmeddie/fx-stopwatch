package com.yumusoft.stopwatch

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.event.ActionEvent

class AboutController {
  @FXML var okButton: Button = _

  @FXML
  def doOk(event: ActionEvent) {
    okButton.getScene.getWindow.hide()
  }
}
