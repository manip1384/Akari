package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelImpl implements Model {
  private final PuzzleLibrary puzzleLibrary;
  private int activePuzzleIndex;
  private final Set<int[]> lampLocations;
  private final List<ModelObserver> observers;

  public ModelImpl(PuzzleLibrary library) {
    if (library == null || library.size() == 0) {
      throw new IllegalArgumentException("PuzzleLibrary cannot be null or empty");
    }
    this.puzzleLibrary = library;
    this.activePuzzleIndex = 0;
    this.lampLocations = new HashSet<>();
    this.observers = new ArrayList<>();
  }

  private void notifyObservers() {
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }

  @Override
  public void addLamp(int r, int c) {
    if (!isValidCell(r, c)) {
      throw new IndexOutOfBoundsException("Cell coordinates out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be of type CORRIDOR to add a lamp");
    }
    if (!isLamp(r, c)) {
      lampLocations.add(new int[] {r, c});
      notifyObservers();
    }
  }

  @Override
  public void removeLamp(int r, int c) {
    if (!isValidCell(r, c)) {
      throw new IndexOutOfBoundsException("Cell coordinates out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be of type CORRIDOR to remove a lamp");
    }
    lampLocations.removeIf(lamp -> lamp[0] == r && lamp[1] == c);
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    if (!isValidCell(r, c)) {
      throw new IndexOutOfBoundsException("Cell coordinates out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be of type CORRIDOR to check lighting");
    }
    return lampLocations.stream().anyMatch(lamp -> lamp[0] == r || lamp[1] == c);
  }

  @Override
  public boolean isLamp(int r, int c) {
    return lampLocations.stream().anyMatch(lamp -> lamp[0] == r && lamp[1] == c);
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (!isLamp(r, c)) {
      throw new IllegalArgumentException("Cell must contain a lamp to check legality");
    }
    return lampLocations.stream()
        .anyMatch(
            lamp ->
                lamp[0] == r && lamp[1] != c
                    || lamp[1] == c && lamp[0] != r); // Same row or column without being blocked
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
      throw new IndexOutOfBoundsException("Index out of bounds");
    }
    activePuzzleIndex = index;
    lampLocations.clear();
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
    Puzzle puzzle = getActivePuzzle();
    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {
        CellType type = puzzle.getCellType(r, c);
        if (type == CellType.CORRIDOR && !isLit(r, c)) return false;
        if (type == CellType.CLUE && !isClueSatisfied(r, c)) return false;
        if (type == CellType.CORRIDOR && isLamp(r, c) && isLampIllegal(r, c)) return false;
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (!isValidCell(r, c)) {
      throw new IndexOutOfBoundsException("Cell coordinates out of bounds");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell must be of type CLUE to check satisfaction");
    }
    int clue = getActivePuzzle().getClue(r, c);
    long adjacentLamps =
        lampLocations.stream()
            .filter(
                lamp ->
                    Math.abs(lamp[0] - r) + Math.abs(lamp[1] - c)
                        == 1) // Directly adjacent (Manhattan distance = 1)
            .count();
    return adjacentLamps == clue;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  private boolean isValidCell(int r, int c) {
    Puzzle puzzle = getActivePuzzle();
    return r >= 0 && r < puzzle.getHeight() && c >= 0 && c < puzzle.getWidth();
  }
}
