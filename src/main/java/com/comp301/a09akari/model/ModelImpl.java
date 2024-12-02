package com.comp301.a09akari.model;

public class ModelImpl implements Model {
  private PuzzleLibrary puzzleLibrary;
  private int activePuzzleIndex;

  public ModelImpl(PuzzleLibrary puzzleLibrary) {
    if (puzzleLibrary == null) {
      throw new IllegalArgumentException("PuzzleLibrary cannot be null");
    }
    this.puzzleLibrary = puzzleLibrary;
    this.activePuzzleIndex = 0;
  }

  @Override
  public void addLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor to add a lamp");
    }
    // Add lamp logic
  }

  @Override
  public void removeLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor to remove a lamp");
    }
    // Remove lamp logic
  }

  @Override
  public boolean isLit(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    // Check if the cell is lit
    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    // Check if the cell contains a lamp
    return false;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    // Check if the lamp placement is illegal
    return false;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return puzzleLibrary.getPuzzle(activePuzzleIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return activePuzzleIndex;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= puzzleLibrary.size()) {
      throw new IndexOutOfBoundsException("Puzzle index out of bounds");
    }
    this.activePuzzleIndex = index;
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    // Reset puzzle (remove all lamps)
  }

  @Override
  public boolean isSolved() {
    Puzzle activePuzzle = getActivePuzzle();
    // Implement the logic to check if the puzzle is solved
    return false;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    // Check if the clue is satisfied
    return false;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    // Add observer logic
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    // Remove observer logic
  }
}
