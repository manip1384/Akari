package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final PuzzleLibrary puzzleLibrary;
  private int activePuzzleIndex;
  private final List<ModelObserver> observers;

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
    Puzzle puzzle = getActivePuzzle();
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    if (isLamp(r, c)) {
      return;
    }
    puzzle.getCellType(r, c);
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    Puzzle puzzle = getActivePuzzle();
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    if (!isLamp(r, c)) {
      return;
    }
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    Puzzle puzzle = getActivePuzzle();
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    Puzzle puzzle = getActivePuzzle();
    return false;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    Puzzle puzzle = getActivePuzzle();
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
      throw new IndexOutOfBoundsException("Invalid puzzle index");
    }
    activePuzzleIndex = index;
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    Puzzle puzzle = getActivePuzzle();
    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {
        if (puzzle.getCellType(r, c) == CellType.CLUE) {
          if (!isClueSatisfied(r, c)) {
            return false;
          }
        }
        if (puzzle.getCellType(r, c) == CellType.CORRIDOR) {
          if (!isLit(r, c)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    Puzzle puzzle = getActivePuzzle();
    if (puzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not a clue");
    }
    int clueValue = puzzle.getClue(r, c);
    int adjacentLamps = 0;
    if (r > 0 && isLamp(r - 1, c)) adjacentLamps++;
    if (r < puzzle.getHeight() - 1 && isLamp(r + 1, c)) adjacentLamps++;
    if (c > 0 && isLamp(r, c - 1)) adjacentLamps++;
    if (c < puzzle.getWidth() - 1 && isLamp(r, c + 1)) adjacentLamps++;
    return adjacentLamps == clueValue;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
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
