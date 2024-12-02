package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final List<Puzzle> puzzleLibrary;
  private Puzzle activePuzzle;
  private int activePuzzleIndex;
  private final List<ModelObserver> observers;

  public ModelImpl() {
    this.puzzleLibrary = new ArrayList<>();
    this.activePuzzleIndex = 0;
    this.activePuzzle = null;
    this.observers = new ArrayList<>();
  }

  @Override
  public void addLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    if (isLamp(r, c)) {
      return;
    }
    activePuzzle = new PuzzleImpl(addLampToBoard(activePuzzle, r, c));
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    if (!isLamp(r, c)) {
      return;
    }
    activePuzzle = new PuzzleImpl(removeLampFromBoard(activePuzzle, r, c));
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    return checkIfLit(r, c);
  }

  @Override
  public boolean isLamp(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    return checkLampAt(r, c);
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell is not a corridor");
    }
    return checkLampIllegal(r, c);
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
    activePuzzleIndex = index;
    activePuzzle = puzzleLibrary.get(activePuzzleIndex);
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return puzzleLibrary.size();
  }

  @Override
  public void resetPuzzle() {
    activePuzzle = new PuzzleImpl(resetLampPositions(activePuzzle));
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    return checkPuzzleSolved();
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r < 0 || r >= activePuzzle.getHeight() || c < 0 || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }
    if (activePuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not a clue");
    }
    return checkClueSatisfaction(r, c);
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

  private int[][] addLampToBoard(Puzzle puzzle, int r, int c) {
    int[][] board = new int[puzzle.getHeight()][puzzle.getWidth()];
    for (int i = 0; i < puzzle.getHeight(); i++) {
      for (int j = 0; j < puzzle.getWidth(); j++) {
        board[i][j] = puzzle.getClue(i, j);
      }
    }
    board[r][c] = 7;
    return board;
  }

  private int[][] removeLampFromBoard(Puzzle puzzle, int r, int c) {
    int[][] board = new int[puzzle.getHeight()][puzzle.getWidth()];
    for (int i = 0; i < puzzle.getHeight(); i++) {
      for (int j = 0; j < puzzle.getWidth(); j++) {
        board[i][j] = puzzle.getClue(i, j);
      }
    }
    board[r][c] = 6;
    return board;
  }

  private boolean checkIfLit(int r, int c) {
    return false;
  }

  private boolean checkLampAt(int r, int c) {
    return activePuzzle.getClue(r, c) == 7;
  }

  private boolean checkLampIllegal(int r, int c) {
    return false;
  }

  private int[][] resetLampPositions(Puzzle puzzle) {
    int[][] board = new int[puzzle.getHeight()][puzzle.getWidth()];
    for (int i = 0; i < puzzle.getHeight(); i++) {
      for (int j = 0; j < puzzle.getWidth(); j++) {
        board[i][j] = puzzle.getClue(i, j);
      }
    }
    return board;
  }

  private boolean checkPuzzleSolved() {
    return false;
  }

  private boolean checkClueSatisfaction(int r, int c) {
    return false;
  }
}
