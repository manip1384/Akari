package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.AlternateMvcController;
import com.comp301.a09akari.model.Model;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class PuzzleView implements FXComponent {
  private final Model model;
  private final AlternateMvcController controller;

  public PuzzleView(Model model, AlternateMvcController controller) {
    this.model = model;
    this.controller = controller;
  }

  @Override
  public Parent render() {
    GridPane grid = new GridPane();

    int rows = model.getActivePuzzle().getHeight();
    int cols = model.getActivePuzzle().getWidth();

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Rectangle cell = new Rectangle(40, 40);
        StackPane cellStack = new StackPane(); // To hold both the rectangle and text

        switch (model.getActivePuzzle().getCellType(row, col)) {
          case CORRIDOR:
            if (model.isLamp(row, col)) {
              if (model.isLampIllegal(row, col)) {
                cell.setFill(Color.RED); // Illegal lamp placement
              } else {
                cell.setFill(Color.ORANGE); // Valid lamp placement
              }
            } else {
              cell.setFill(model.isLit(row, col) ? Color.YELLOW : Color.WHITE);
            }
            cellStack.getChildren().add(cell); // Add rectangle to stack

            break;

          case WALL:
            cell.setFill(Color.GRAY);
            cellStack.getChildren().add(cell); // Add rectangle to stack

            break;

          case CLUE:
            int clueNum = model.getActivePuzzle().getClue(row, col);
            Text clueText = new Text(String.valueOf(clueNum));
            clueText.setFill(Color.BLACK); // Ensure clue text is visible
            cellStack.getChildren().add(clueText);

            if (model.isClueSatisfied(row, col)) {
              clueText.setFill(Color.GREEN); // Satisfied clue
            }
            // Add clue number
            break;
        }

        // Add click handler
        int finalRow = row;
        int finalCol = col;
        cell.setOnMouseClicked(e -> controller.clickCell(finalRow, finalCol));

        grid.add(cellStack, col, row); // Add stack to grid
      }
    }

    return grid;
  }
}
