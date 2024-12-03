package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class ModelImpl implements Model {
  private PuzzleLibrary puzzleLibrary;
  private int activePuzzleIndex;
  private List<ModelObserver> observers;
  private List<int[]> currentPuzzleLamps;  // List of int arrays to store row, col as positions

  public ModelImpl(PuzzleLibrary puzzleLibrary) {
    if (puzzleLibrary == null) {
      throw new IllegalArgumentException("PuzzleLibrary cannot be null");
    }
    this.puzzleLibrary = puzzleLibrary;
    this.activePuzzleIndex = 0;
    this.observers = new ArrayList<>();
    this.currentPuzzleLamps = new ArrayList<>(); // Initialize the lamps list
  }

  @Override
  public void addLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor to add a lamp");
    }

    // Add the lamp to the list as an int array {row, col}
    currentPuzzleLamps.add(new int[] {r, c});
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor to remove a lamp");
    }

    // Remove the lamp by matching row and col in the array
    currentPuzzleLamps.removeIf(lamp -> lamp[0] == r && lamp[1] == c);
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    validatePosition(r, c, CellType.CORRIDOR);

    int[] pos = new int[] {r, c};
    return currentPuzzleLamps.stream()
            .anyMatch(lamp -> (pos[0] == lamp[0] && pos[1] == lamp[1]) || isInLineOfSight(pos, lamp));
  }

  @Override
  public boolean isLamp(int r, int c) {
    return currentPuzzleLamps.stream()
            .anyMatch(lamp -> lamp[0] == r && lamp[1] == c);
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
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
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    currentPuzzleLamps.clear();
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
      }
    }
    return currentPuzzleLamps.stream().noneMatch(lamp -> isLampIllegal(lamp[0], lamp[1]));
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    validatePosition(r, c, CellType.CLUE);

    int clue = activePuzzle.getClue(r, c);
    int count = 0;

    int[][] adjacent = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] dir : adjacent) {
      int newR = r + dir[0], newC = c + dir[1];
      if (newR >= 0 && newR < activePuzzle.getHeight() &&
              newC >= 0 && newC < activePuzzle.getWidth() &&
              activePuzzle.getCellType(newR, newC) == CellType.CORRIDOR &&
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

  private boolean isInLineOfSight(int[] pos1, int[] pos2) {
    return false;
  }

  private void validatePosition(int r, int c, CellType expectedType) {
    Puzzle puzzle = getActivePuzzle();
    if (r < 0 || r >= puzzle.getHeight() || c < 0 || c >= puzzle.getWidth() ||
            puzzle.getCellType(r, c) != expectedType) {
      throw new IllegalArgumentException("Invalid position for " + expectedType);
    }
  }
}
