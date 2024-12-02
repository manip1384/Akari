package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final PuzzleLibrary puzzleLibrary;
  private int activePuzzleIndex;
  private final List<ModelObserver> observers;
  private Puzzle activePuzzle;

  public ModelImpl(PuzzleLibrary library) {
    this.puzzleLibrary = library;
    this.activePuzzleIndex = 0;
    this.activePuzzle = library.getPuzzle(activePuzzleIndex);
    this.observers = new ArrayList<>();
  }

  @Override
  public void addLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("The cell is not a CORRIDOR");
    }

    if (isLamp(r, c)) {
      return;
    }

    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("The cell is not a CORRIDOR");
    }

    if (!isLamp(r, c)) {
      return;
    }

    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("The cell is not a CORRIDOR");
    }

    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    return false;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("The cell is not a CORRIDOR");
    }

    if (!isLamp(r, c)) {
      throw new IllegalArgumentException("No lamp at the specified location");
    }

    return false;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return activePuzzle;
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
    activePuzzleIndex = index;
    activePuzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
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
    return false;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("The cell is not a CLUE");
    }

    return false;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null");
    }
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null");
    }
    observers.remove(observer);
  }

  private void notifyObservers() {
    for (ModelObserver observer : observers) {
      observer.update();
    }
  }
}
