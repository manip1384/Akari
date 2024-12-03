package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelImpl implements Model {
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

  private final PuzzleLibrary puzzleLibrary;
  private int puzzleIndex;
  private final List<Set<Position>> lampsByPuzzle;
  private final List<ModelObserver> observerList;

  public ModelImpl(PuzzleLibrary library) {
    if (library == null) throw new IllegalArgumentException("Library cannot be null");

    this.puzzleLibrary = library;
    this.puzzleIndex = 0;
    this.observerList = new ArrayList<>();
    this.lampsByPuzzle = new ArrayList<>();

    for (int i = 0; i < library.size(); i++) {
      lampsByPuzzle.add(new HashSet<>());
    }
  }

  private void validatePosition(int row, int col, CellType expectedType) {
    Puzzle puzzle = getActivePuzzle();
    if (row < 0 || col < 0 || row >= puzzle.getHeight() || col >= puzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Position out of bounds");
    }
    if (expectedType != null && puzzle.getCellType(row, col) != expectedType) {
      throw new IllegalArgumentException("Invalid cell type");
    }
  }

  @Override
  public void addLamp(int row, int col) {
    validatePosition(row, col, CellType.CORRIDOR);
    lampsByPuzzle.get(puzzleIndex).add(new Position(row, col));
    notifyObservers();
  }

  @Override
  public void removeLamp(int row, int col) {
    validatePosition(row, col, CellType.CORRIDOR);
    lampsByPuzzle.get(puzzleIndex).remove(new Position(row, col));
    notifyObservers();
  }

  @Override
  public boolean isLamp(int row, int col) {
    validatePosition(row, col, CellType.CORRIDOR);
    return getCurrentPuzzleLamps().contains(new Position(row, col));
  }

  @Override
  public boolean isLit(int row, int col) {
    validatePosition(row, col, CellType.CORRIDOR);
    Position pos = new Position(row, col);
    return getCurrentPuzzleLamps().stream()
        .anyMatch(lamp -> pos.equals(lamp) || isInLineOfSight(pos, lamp));
  }

  private Set<Position> getCurrentPuzzleLamps() {
    return lampsByPuzzle.get(puzzleIndex);
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

  @Override
  public boolean isLampIllegal(int row, int col) {
    validatePosition(row, col, null);
    if (!isLamp(row, col)) {
      throw new IllegalArgumentException("No lamp at position");
    }
    Position lampPos = new Position(row, col);
    return getCurrentPuzzleLamps().stream()
        .anyMatch(otherLamp -> !lampPos.equals(otherLamp) && isInLineOfSight(lampPos, otherLamp));
  }

  @Override
  public boolean isClueSatisfied(int row, int col) {
    validatePosition(row, col, CellType.CLUE);
    int clue = getActivePuzzle().getClue(row, col);
    int count = 0;

    int[][] adjacent = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] dir : adjacent) {
      int newRow = row + dir[0], newCol = col + dir[1];
      if (newRow >= 0
          && newRow < getActivePuzzle().getHeight()
          && newCol >= 0
          && newCol < getActivePuzzle().getWidth()
          && getActivePuzzle().getCellType(newRow, newCol) == CellType.CORRIDOR
          && isLamp(newRow, newCol)) {
        count++;
      }
    }
    return count == clue;
  }

  @Override
  public boolean isSolved() {
    Puzzle puzzle = getActivePuzzle();
    for (int row = 0; row < puzzle.getHeight(); row++) {
      for (int col = 0; col < puzzle.getWidth(); col++) {
        CellType type = puzzle.getCellType(row, col);
        if (type == CellType.CORRIDOR && !isLit(row, col)) return false;
        if (type == CellType.CLUE && !isClueSatisfied(row, col)) return false;
        if (type == CellType.CORRIDOR && isLamp(row, col) && isLampIllegal(row, col)) return false;
      }
    }
    return true;
  }

  @Override
  public void resetPuzzle() {
    getCurrentPuzzleLamps().clear();
    notifyObservers();
  }

  @Override
  public Puzzle getActivePuzzle() {
    return puzzleLibrary.getPuzzle(puzzleIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return puzzleIndex;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= puzzleLibrary.size()) {
      throw new IndexOutOfBoundsException("Invalid puzzle index");
    }
    puzzleIndex = index;
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observerList.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observerList.remove(observer);
  }

  private void notifyObservers() {
    for (ModelObserver observer : observerList) {
      observer.update(this);
    }
  }
}
