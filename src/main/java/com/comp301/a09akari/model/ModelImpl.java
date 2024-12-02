package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private List<Puzzle> puzzles;
  private int activePuzzleIndex;

  public ModelImpl() {
    this.puzzles = new ArrayList<>();
    this.activePuzzleIndex = 0; // default to the first puzzle
  }

  @Override
  public void addLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not of type CORRIDOR");
    }
    // Logic to add a lamp to the active puzzle (e.g., mark the cell as having a lamp)
    // This can be implemented in a variety of ways, depending on how the lamps are represented
    // For simplicity, we assume a boolean array for lamps is part of the puzzle data
    if (!isLamp(r, c)) {
      // Mark the cell as having a lamp (this could involve modifying a separate lamp grid in PuzzleImpl)
      // Example: setLamp(r, c, true); // Assuming a method for marking lamps exists
    }
  }

  @Override
  public void removeLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not of type CORRIDOR");
    }
    // Logic to remove a lamp from the active puzzle
    // Example: setLamp(r, c, false); // Assuming a method for removing lamps exists
  }

  @Override
  public boolean isLit(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not of type CORRIDOR");
    }
    // Logic to check if the cell is lit by a nearby lamp
    // This would require checking the row and column for lamps, and ensuring no walls or clues block the light
    return false; // Simplified for now, should return the correct value based on the game state
  }

  @Override
  public boolean isLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    // Logic to check if there's a lamp at the specified location
    return false; // Simplified for now
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (!isLamp(r, c)) {
      throw new IllegalArgumentException("No lamp in this cell");
    }
    // Logic to check if the lamp is in direct view of another lamp
    return false; // Simplified for now
  }

  @Override
  public Puzzle getActivePuzzle() {
    if (activePuzzleIndex < 0 || activePuzzleIndex >= puzzles.size()) {
      throw new IndexOutOfBoundsException("Invalid puzzle index");
    }
    return puzzles.get(activePuzzleIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return activePuzzleIndex;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= puzzles.size()) {
      throw new IndexOutOfBoundsException("Invalid puzzle index");
    }
    activePuzzleIndex = index;
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzles.size();
  }

  @Override
  public void resetPuzzle() {
    Puzzle activePuzzle = getActivePuzzle();
    // Logic to reset all lamps in the active puzzle
    // For example, clear a lamp grid or reset a status
  }

  @Override
  public boolean isSolved() {
    Puzzle activePuzzle = getActivePuzzle();
    // Logic to check if the puzzle is solved (i.e., every clue is satisfied and every corridor is lit)
    return false; // Simplified for now
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not of type CLUE");
    }
    // Logic to check if the clue is satisfied
    return false; // Simplified for now
  }

  @Override
  public void addObserver(ModelObserver observer) {
    // Logic to add observer (implementing observer pattern)
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    // Logic to remove observer (implementing observer pattern)
  }

  public void addPuzzle(Puzzle puzzle) {
    if (puzzle == null) {
      throw new IllegalArgumentException("Cannot add a null puzzle");
    }
    puzzles.add(puzzle);
  }

  public Puzzle getPuzzle(int index) {
    if (index < 0 || index >= puzzles.size()) {
      throw new IndexOutOfBoundsException("Invalid puzzle index");
    }
    return puzzles.get(index);
  }

  public void setActivePuzzle(Puzzle puzzle) {
    int index = puzzles.indexOf(puzzle);
    if (index == -1) {
      throw new IllegalArgumentException("Puzzle not found in library");
    }
    activePuzzleIndex = index;
  }
}
