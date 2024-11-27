package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final PuzzleLibrary puzzleLibrary;
  private int activePuzzleIndex;
  private final List<ModelObserver> observers;
  private final List<int[]> lampLocations;

  public ModelImpl(PuzzleLibrary library) {
    if (library == null) {
      throw new IllegalArgumentException("Puzzle library cannot be null");
    }
    this.puzzleLibrary = library;
    this.activePuzzleIndex = 0;
    this.observers = new ArrayList<>();
    this.lampLocations = new ArrayList<>();
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  @Override
  public void addLamp(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
    if (puzzle.getCellType(r, c) == CellType.CORRIDOR) {
      lampLocations.add(new int[] {r, c});
      notifyObservers();
    } else {
      throw new IllegalArgumentException("Cannot place lamp at this location");
    }
  }

  @Override
  public void removeLamp(int r, int c) {
    for (int i = 0; i < lampLocations.size(); i++) {
      int[] location = lampLocations.get(i);
      if (location[0] == r && location[1] == c) {
        lampLocations.remove(i);
        notifyObservers();
        return;
      }
    }
    throw new IllegalArgumentException("Lamp not found at this location");
  }

  @Override
  public boolean isSolved() {
    Puzzle puzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {
        if (puzzle.getCellType(r, c) == CellType.CORRIDOR && !isLit(r, c)) {
          return false;
        }
        if (puzzle.getCellType(r, c) == CellType.CLUE && !isClueSatisfied(r, c)) {
          return false;
        }
        if (puzzle.getCellType(r, c) == CellType.CLUE && isLampIllegal(r, c)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isLamp(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
    if (r < 0 || r >= puzzle.getHeight() || c < 0 || c >= puzzle.getWidth()) {
      return false;
    }
    for (int[] lamp : lampLocations) {
      if (lamp[0] == r && lamp[1] == c) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isLit(int r, int c) {
    for (int[] lamp : lampLocations) {
      int lampR = lamp[0];
      int lampC = lamp[1];
      if (Math.abs(lampR - r) <= 1 && Math.abs(lampC - c) <= 1) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
    if (puzzle.getCellType(r, c) == CellType.CLUE) {
      int clueValue = puzzle.getClue(r, c);
      int lampCount = 0;
      for (int[] lamp : lampLocations) {
        int lampR = lamp[0];
        int lampC = lamp[1];
        if (Math.abs(lampR - r) <= 1 && Math.abs(lampC - c) <= 1) {
          lampCount++;
        }
      }
      return lampCount == clueValue;
    }
    return true;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    Puzzle puzzle = puzzleLibrary.getPuzzle(activePuzzleIndex);
    return puzzle.getCellType(r, c) == CellType.CLUE
            || puzzle.getCellType(r, c) != CellType.CORRIDOR;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return puzzleLibrary.getPuzzle(activePuzzleIndex);
  }

  private void notifyObservers() {
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }

  public Puzzle getPuzzle() {
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
    lampLocations.clear();
    notifyObservers();
  }
}
