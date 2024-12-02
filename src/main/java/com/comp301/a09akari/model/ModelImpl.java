package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private PuzzleLibrary puzzleLibrary;
  private Puzzle activePuzzle;
  private int activePuzzleIndex;
  private List<ModelObserver> observers;
  private List<LampLocation> lampLocations;

  public ModelImpl(PuzzleLibrary library) {
    this.puzzleLibrary = library;
    this.activePuzzleIndex = 0;
    this.activePuzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
    this.observers = new ArrayList<>();
    this.lampLocations = new ArrayList<>();
  }

  @Override
  public void addLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Invalid cell location");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }

    if (!isLamp(r, c)) {
      lampLocations.add(new LampLocation(r, c));
      notifyObservers();
    }
  }

  @Override
  public void removeLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Invalid cell location");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }

    lampLocations.removeIf(lamp -> lamp.getRow() == r && lamp.getCol() == c);
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Invalid cell location");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }

    for (LampLocation lamp : lampLocations) {
      if (lamp.isLit(r, c)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Invalid cell location");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }

    return lampLocations.stream().anyMatch(lamp -> lamp.getRow() == r && lamp.getCol() == c);
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Invalid cell location");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }

    for (LampLocation lamp : lampLocations) {
      if (lamp.getRow() == r && lamp.getCol() == c && lamp.isIllegal(lampLocations)) {
        return true;
      }
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
      throw new IndexOutOfBoundsException("Index out of bounds");
    }
    this.activePuzzleIndex = index;
    this.activePuzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    lampLocations.clear();
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    for (int r = 0; r < activePuzzle.getHeight(); r++) {
      for (int c = 0; c < activePuzzle.getWidth(); c++) {
        if (activePuzzle.getCellType(r, c) == CellType.CORRIDOR && !isLit(r, c)) {
          return false;
        }
        if (activePuzzle.getCellType(r, c) == CellType.CLUE && !isClueSatisfied(r, c)) {
          return false;
        }
        if (activePuzzle.getCellType(r, c) == CellType.CORRIDOR && isLampIllegal(r, c)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Invalid cell location");
    }

    if (activePuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not a clue");
    }

    int clue = activePuzzle.getClue(r, c);
    int lampCount = 0;

    for (LampLocation lamp : lampLocations) {
      if (lamp.isAdjacent(r, c)) {
        lampCount++;
      }
    }

    return lampCount == clue;
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
