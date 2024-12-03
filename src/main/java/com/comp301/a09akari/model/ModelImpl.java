package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelImpl implements Model {
  private final PuzzleLibrary puzzleLibrary;
  private int currentPuzzleIndex;
  private final List<Set<Position>> puzzleLamps;
  private final List<ModelObserver> observers;

  // Inner class to represent a board position
  private static class Position {
    final int row, col;

    Position(int row, int col) {
      this.row = row;
      this.col = col;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Position)) return false;
      Position position = (Position) o;
      return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
      return 31 * row + col;
    }
  }

  public ModelImpl(PuzzleLibrary library) {
    if (library == null) throw new IllegalArgumentException("Library cannot be null");

    this.puzzleLibrary = library;
    this.currentPuzzleIndex = 0;
    this.observers = new ArrayList<>();
    this.puzzleLamps = new ArrayList<>();

    // Initialize empty lamp sets for each puzzle
    for (int i = 0; i < library.size(); i++) {
      puzzleLamps.add(new HashSet<>());
    }
  }

  private void validatePosition(int r, int c, CellType expectedType) {
    Puzzle puzzle = getActivePuzzle();
    if (r < 0 || c < 0 || r >= puzzle.getHeight() || c >= puzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Position out of bounds");
    }
    if (expectedType != null && puzzle.getCellType(r, c) != expectedType) {
      throw new IllegalArgumentException("Invalid cell type");
    }
  }

  private boolean isInLineOfSight(Position p1, Position p2) {
    if (p1.row == p2.row) {
      int minCol = Math.min(p1.col, p2.col);
      int maxCol = Math.max(p1.col, p2.col);
      for (int col = minCol + 1; col < maxCol; col++) {
        if (getActivePuzzle().getCellType(p1.row, col) != CellType.CORRIDOR) {
          return false;
        }
      }
      return true;
    } else if (p1.col == p2.col) {
      int minRow = Math.min(p1.row, p2.row);
      int maxRow = Math.max(p1.row, p2.row);
      for (int row = minRow + 1; row < maxRow; row++) {
        if (getActivePuzzle().getCellType(row, p1.col) != CellType.CORRIDOR) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private Set<Position> getCurrentPuzzleLamps() {
    return puzzleLamps.get(currentPuzzleIndex);
  }

  @Override
  public void addLamp(int r, int c) {
    validatePosition(r, c, CellType.CORRIDOR);
    getCurrentPuzzleLamps().add(new Position(r, c));
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    validatePosition(r, c, CellType.CORRIDOR);
    getCurrentPuzzleLamps().remove(new Position(r, c));
    notifyObservers();
  }

  @Override
  public boolean isLamp(int r, int c) {
    validatePosition(r, c, CellType.CORRIDOR);
    return getCurrentPuzzleLamps().contains(new Position(r, c));
  }

  @Override
  public boolean isLit(int r, int c) {
    validatePosition(r, c, CellType.CORRIDOR);
    Position pos = new Position(r, c);
    return getCurrentPuzzleLamps().stream()
            .anyMatch(lamp -> pos.equals(lamp) || isInLineOfSight(pos, lamp));
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    validatePosition(r, c, null);
    if (!isLamp(r, c)) throw new IllegalArgumentException("No lamp at position");

    Position lampPos = new Position(r, c);
    return getCurrentPuzzleLamps().stream()
            .anyMatch(otherLamp -> !lampPos.equals(otherLamp) && isInLineOfSight(lampPos, otherLamp));
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    validatePosition(r, c, CellType.CLUE);
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
  public boolean isSolved() {
    Puzzle puzzle = getActivePuzzle();
    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {
        CellType type = puzzle.getCellType(r, c);
        if (type == CellType.CORRIDOR && !isLit(r, c)) return false;
        if (type == CellType.CLUE && !isClueSatisfied(r, c)) return false;
      }
    }
    return getCurrentPuzzleLamps().stream().noneMatch(lamp -> isLampIllegal(lamp.row, lamp.col));
  }

  @Override
  public void resetPuzzle() {
    getCurrentPuzzleLamps().clear();
    notifyObservers();
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= puzzleLibrary.size()) {
      throw new IndexOutOfBoundsException("Invalid puzzle index");
    }
    currentPuzzleIndex = index;
    notifyObservers();
  }

  private void notifyObservers() {
    observers.forEach(observer -> observer.update(this));
  }

  // Other required methods with straightforward implementations
  @Override
  public Puzzle getActivePuzzle() {
    return puzzleLibrary.getPuzzle(currentPuzzleIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return currentPuzzleIndex;
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }
}