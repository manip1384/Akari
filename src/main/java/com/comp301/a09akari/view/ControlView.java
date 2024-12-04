package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.AlternateMvcController;
import com.comp301.a09akari.model.Model;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlView implements FXComponent {
  private final Model model;
  private final AlternateMvcController controller;

  public ControlView(Model model, AlternateMvcController controller) {
    this.model = model;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox controls = new HBox(10);

    Button nextButton = new Button("Next");
    nextButton.setOnAction(e -> controller.clickNextPuzzle());

    Button prevButton = new Button("Previous");
    prevButton.setOnAction(e -> controller.clickPrevPuzzle());

    Button resetButton = new Button("Reset");
    resetButton.setOnAction(e -> controller.clickResetPuzzle());

    Button randomButton = new Button("Random");
    randomButton.setOnAction(e -> controller.clickRandPuzzle());

    controls.getChildren().addAll(prevButton, nextButton, resetButton, randomButton);

    return controls;
  }
}
