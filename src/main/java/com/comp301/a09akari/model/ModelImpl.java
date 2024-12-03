package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelImpl implements Model {
  private PuzzleLibrary puzzleLibrary;
  private Puzzle activePuzzle;
  private boolean[][] lamps;

  public ModelImpl(PuzzleLibrary puzzleLibrary) {
    if (puzzleLibrary == null || puzzleLibrary.size() == 0) {
      throw new IllegalArgumentException("Puzzle library cannot be null or empty.");
    }
    this.puzzleLibrary = puzzleLibrary;
    this.activePuzzle = puzzleLibrary.getPuzzle(0);
    this.lamps = new boolean[activePuzzle.getHeight()][activePuzzle.getWidth()];
  }

  @Override
  public Puzzle getActivePuzzle() {
    return activePuzzle;
  }

  @Override
  public int getActivePuzzleIndex() {
    return 0;
  }

  @Override
  public void setActivePuzzleIndex(int index) {

  }

  @Override
  public int getPuzzleLibrarySize() {
    return 0;
  }

  @Override
  public boolean isSolved() {
    return allCorridorsLit() && allCluesSatisfied() && noIllegalLamps();
  }

  private boolean allCorridorsLit() {
    for (int r = 0; r < activePuzzle.getHeight(); r++) {
      for (int c = 0; c < activePuzzle.getWidth(); c++) {
        if (activePuzzle.getCellType(r, c) == CellType.CORRIDOR && !isLit(r, c)) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean allCluesSatisfied() {
    for (int r = 0; r < activePuzzle.getHeight(); r++) {
      for (int c = 0; c < activePuzzle.getWidth(); c++) {
        if (activePuzzle.getCellType(r, c) == CellType.CLUE) {
          if (!isClueSatisfied(r, c)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private boolean noIllegalLamps() {
    for (int r = 0; r < activePuzzle.getHeight(); r++) {
      for (int c = 0; c < activePuzzle.getWidth(); c++) {
        if (isLamp(r, c) && isLampIllegal(r, c)) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int row, int col) {
    int clue = activePuzzle.getClue(row, col);
    int lampCount = 0;
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] dir : directions) {
      int adjRow = row + dir[0];
      int adjCol = col + dir[1];
      if (isValidCell(adjRow, adjCol) && isLamp(adjRow, adjCol)) {
        lampCount++;
      }
    }
    return lampCount == clue;
  }

  @Override
  public void addObserver(ModelObserver observer) {

  }

  @Override
  public void removeObserver(ModelObserver observer) {

  }

  @Override
  public boolean isLit(int row, int col) {
    if (isLamp(row, col)) {
      return true;
    }
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] dir : directions) {
      int currentRow = row + dir[0];
      int currentCol = col + dir[1];
      while (isValidCell(currentRow, currentCol)) {
        if (isLamp(currentRow, currentCol)) {
          return true;
        }
        if (activePuzzle.getCellType(currentRow, currentCol) != CellType.CORRIDOR) {
          break;
        }
        currentRow += dir[0];
        currentCol += dir[1];
      }
    }
    return false;
  }

  @Override
  public boolean isLampIllegal(int row, int col) {
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    for (int[] dir : directions) {
      int currentRow = row + dir[0];
      int currentCol = col + dir[1];
      while (isValidCell(currentRow, currentCol)) {
        if (isLamp(currentRow, currentCol)) {
          return true;
        }
        if (activePuzzle.getCellType(currentRow, currentCol) != CellType.CORRIDOR) {
          break;
        }
        currentRow += dir[0];
        currentCol += dir[1];
      }
    }
    return false;
  }

  @Override
  public boolean isLamp(int row, int col) {
    return isValidCell(row, col) && lamps[row][col];
  }

  private boolean isValidCell(int row, int col) {
    return row >= 0 && row < activePuzzle.getHeight() && col >= 0 && col < activePuzzle.getWidth();
  }

  @Override
  public void addLamp(int row, int col) {
    if (activePuzzle.getCellType(row, col) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cannot place a lamp on non-corridor cells.");
    }
    lamps[row][col] = true;
  }

  @Override
  public void removeLamp(int row, int col) {
    if (!isLamp(row, col)) {
      throw new IllegalArgumentException("No lamp present at this location.");
    }
    lamps[row][col] = false;
  }

  public boolean isLampAt(int row, int col) {
    return isLamp(row, col);
  }

  @Override
  public void resetPuzzle() {
    lamps = new boolean[activePuzzle.getHeight()][activePuzzle.getWidth()];
  }
}
