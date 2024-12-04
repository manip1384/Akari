package com.comp301.a09akari.view;

import com.comp301.a09akari.model.Model;
import javafx.scene.Parent;
import javafx.scene.control.Label;

public class MessageView implements FXComponent {
  private final Model model;

  public MessageView(Model model) {
    this.model = model;
  }

  @Override
  public Parent render() {
    Label message = new Label();
    if (model.isSolved()) {
      message.setText("Puzzle Solved! 🎉");
    } else {
      message.setText("Keep Going!");
    }
    return message;
  }
}
