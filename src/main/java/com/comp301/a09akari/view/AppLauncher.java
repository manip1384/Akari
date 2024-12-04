package com.comp301.a09akari.view;

import com.comp301.a09akari.SamplePuzzles;
import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.controller.AlternateMvcController;
import com.comp301.a09akari.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppLauncher extends Application {

  @Override
  public void start(Stage stage) {
    // Step 1: Create the Puzzle Library and Model
    PuzzleLibrary library = new PuzzleLibraryImpl();
    library.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_01));
    library.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_02));
    library.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_03));
    library.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_04));
    library.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_05));
    Model model = new ModelImpl(library);

    // Step 2: Create the Controller
    AlternateMvcController controller = new ControllerImpl(model);

    // Step 3: Create FXComponent Instances
    FXComponent puzzleView = new PuzzleView(model, controller);
    FXComponent controlView = new ControlView(model, controller);
    FXComponent messageView = new MessageView(model);

    // Step 4: Create and Compose the Root Layout
    BorderPane root = new BorderPane();
    root.setCenter(puzzleView.render());
    root.setBottom(controlView.render());
    root.setTop(messageView.render());

    // Step 5: Create the Scene
    Scene scene = new Scene(root, 600, 600);
    scene.getStylesheets().add("main.css");
    stage.setScene(scene);

    // Step 6: Register Observer to Dynamically Update the View
    model.addObserver(
        m -> {
          root.setCenter(puzzleView.render());
          root.setBottom(controlView.render());
          root.setTop(messageView.render());
        });

    // Step 7: Set the Stage and Show the Application
    stage.setTitle("Akari");
    stage.show();
  }
}
