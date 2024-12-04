package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.Puzzle;

public class ControllerImpl implements AlternateMvcController {
  private final Model model;

  // Constructor to initialize the ControllerImpl with the Model
  public ControllerImpl(Model model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.model = model;
  }

  @Override
  public void clickNextPuzzle() {
    // Move to the next puzzle, if possible
    int nextPuzzleIndex = model.getActivePuzzleIndex() + 1;
    if (nextPuzzleIndex < model.getPuzzleLibrarySize()) {
      model.setActivePuzzleIndex(nextPuzzleIndex);
    }
  }

  @Override
  public void clickPrevPuzzle() {
    // Move to the previous puzzle, if possible
    int prevPuzzleIndex = model.getActivePuzzleIndex() - 1;
    if (prevPuzzleIndex >= 0) {
      model.setActivePuzzleIndex(prevPuzzleIndex);
    }
  }

  @Override
  public void clickRandPuzzle() {
    // Move to a random puzzle
    int randomIndex = (int) (Math.random() * model.getPuzzleLibrarySize());
    model.setActivePuzzleIndex(randomIndex);
  }

  @Override
  public void clickResetPuzzle() {
    // Reset the active puzzle by removing all placed lamps
    model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) {
    // Add or remove a lamp from the cell depending on its current state
    if (model.isLamp(r, c)) {
      model.removeLamp(r, c);
    } else {
      model.addLamp(r, c);
    }
  }

  @Override
  public boolean isLit(int r, int c) {
    // Check if the cell is lit
    return model.isLit(r, c);
  }

  @Override
  public boolean isLamp(int r, int c) {
    // Check if the cell contains a lamp
    return model.isLamp(r, c);
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    // Check if the clue at the given cell is satisfied
    return model.isClueSatisfied(r, c);
  }

  @Override
  public boolean isSolved() {
    // Check if the active puzzle is solved
    return model.isSolved();
  }

  @Override
  public Puzzle getActivePuzzle() {
    // Get the active puzzle from the model
    return model.getActivePuzzle();
  }
}
