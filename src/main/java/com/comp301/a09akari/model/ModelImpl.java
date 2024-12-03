package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private PuzzleLibrary puzzleLibrary;
  private int activePuzzleIndex;
  private List<ModelObserver> observers;

  public ModelImpl(PuzzleLibrary puzzleLibrary) {
    if (puzzleLibrary == null) {
      throw new IllegalArgumentException("PuzzleLibrary cannot be null");
    }
    this.puzzleLibrary = puzzleLibrary;
    this.activePuzzleIndex = 0;
    this.observers = new ArrayList<>();
  }

  @Override
  public void addLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor to add a lamp");
    }
    // Logic to add lamp

    // Notify observers that the model has changed
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor to remove a lamp");
    }
    // Logic to remove lamp

    // Notify observers that the model has changed
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    // Logic to check if the cell is lit
    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    // Logic to check if the cell contains a lamp
    return false;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    // Logic to check if the lamp placement is illegal
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
    // Notify observers that the active puzzle index has changed
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    // Logic to reset the puzzle (remove all lamps)
    // Notify observers that the puzzle has been reset
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    Puzzle activePuzzle = getActivePuzzle();
    // Logic to check if the puzzle is solved
    return false;
  }

  private void correctPosition(int r, int c, CellType expectedType) {
    Puzzle puzzle = getActivePuzzle();
    if (r < 0 || c < 0 || r >= puzzle.getHeight() || c >= puzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Out of bounds");
    }
    if (expectedType != null && puzzle.getCellType(r, c) != expectedType) {
      throw new IllegalArgumentException("Invalid cell type");
    }
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    correctPosition(r, c, CellType.CLUE);
    int clue = getActivePuzzle().getClue(r, c);
    int count = 0;

    int[][] adjacent = {{-1,0}, {1,0}, {0,-1}, {0,1}};
    for (int[] dir : adjacent) {
      int newR = r + dir[0], newC = c + dir[1];
      if (newR >= 0 && newR < getActivePuzzle().getHeight() &&
              newC >= 0 && newC < getActivePuzzle().getWidth() &&
              getActivePuzzle().getCellType(newR, newC) == CellType.CORRIDOR &&
              isLamp(newR, newC)) {
        count++;
      }
    }
    return count == clue;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    if (observer != null) {
      observers.add(observer);
    }
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  private void notifyObservers() {
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }
}
