package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {
  private final int[][] board;

  public PuzzleImpl(int[][] board) {
    this.board = board;
  }

  @Override
  public int getWidth() {
    return board[0].length;
  }

  @Override
  public int getHeight() {
    return board.length;
  }

  @Override
  public CellType getCellType(int r, int c) {
    if (r < 0 || r >= getHeight() || c < 0 || c >= getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }

    int cellValue = board[r][c];
    switch (cellValue) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
        return CellType.CLUE;
      case 5:
        return CellType.WALL;
      case 6:
        return CellType.CORRIDOR;
      default:
        throw new IllegalArgumentException("Invalid cell value: " + cellValue);
    }
  }

  @Override
  public int getClue(int r, int c) {
    if (r < 0 || r >= getHeight() || c < 0 || c >= getWidth()) {
      throw new IndexOutOfBoundsException("Row or column out of bounds");
    }

    int cellValue = board[r][c];
    if (cellValue >= 0 && cellValue <= 4) {
      return cellValue;
    } else {
      throw new IllegalArgumentException("Cell at (" + r + ", " + c + ") is not a clue");
    }
  }
}
