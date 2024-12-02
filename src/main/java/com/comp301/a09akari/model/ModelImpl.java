package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private PuzzleLibrary puzzleLibrary;
  private List<int[]> lampLocations;
  private int activePuzzleIndex;

  public ModelImpl(PuzzleLibrary library) {
    this.puzzleLibrary = library;
    this.lampLocations = new ArrayList<>();
    this.activePuzzleIndex = 0;
  }

  @Override
  public void addLamp(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Row or column is out of bounds.");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not of type CORRIDOR.");
    }

    int[] lampPosition = new int[]{r, c};
    if (!lampExists(lampPosition)) {
      lampLocations.add(lampPosition);
    }
  }

  @Override
  public void removeLamp(int r, int c) {
    int[] lampPosition = new int[]{r, c};
    lampLocations.removeIf(lamp -> lamp[0] == r && lamp[1] == c);
  }

  @Override
  public boolean isLit(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Row or column is out of bounds.");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not of type CORRIDOR.");
    }

    for (int[] lamp : lampLocations) {
      if (lamp[0] == r || lamp[1] == c) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    for (int[] lamp : lampLocations) {
      if (lamp[0] == r && lamp[1] == c) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (!isLamp(r, c)) {
      throw new IllegalArgumentException("No lamp at the given position.");
    }

    for (int[] lamp : lampLocations) {
      if (lamp[0] != r || lamp[1] != c) {
        if ((lamp[0] == r || lamp[1] == c) && isBlocked(r, c, lamp)) {
          return true;
        }
      }
    }
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
      throw new IndexOutOfBoundsException("Invalid puzzle index.");
    }
    activePuzzleIndex = index;
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    lampLocations.clear();
  }

  @Override
  public boolean isSolved() {
    Puzzle puzzle = getActivePuzzle();

    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {
        if (puzzle.getCellType(r, c) == CellType.CORRIDOR && !isLit(r, c)) {
          return false;
        }
      }
    }

    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {
        if (puzzle.getCellType(r, c) == CellType.CLUE && !isClueSatisfied(r, c)) {
          return false;
        }
      }
    }

    for (int[] lamp : lampLocations) {
      if (isLampIllegal(lamp[0], lamp[1])) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    Puzzle puzzle = getActivePuzzle();
    if (puzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not a clue.");
    }

    int clue = puzzle.getClue(r, c);
    int lampCount = 0;

    for (int[] lamp : lampLocations) {
      if (Math.abs(lamp[0] - r) <= 1 && Math.abs(lamp[1] - c) <= 1) {
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

  private boolean lampExists(int[] lampPosition) {
    for (int[] lamp : lampLocations) {
      if (lamp[0] == lampPosition[0] && lamp[1] == lampPosition[1]) {
        return true;
      }
    }
    return false;
  }

  private boolean isBlocked(int r, int c, int[] lamp) {
    return false;
  }
}
