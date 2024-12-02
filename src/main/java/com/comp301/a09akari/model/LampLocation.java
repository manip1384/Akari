package com.comp301.a09akari.model;

import java.util.List;

public class LampLocation {
    private int row;
    private int col;

    public LampLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isLit(int r, int c) {
        if (this.row == r && this.col == c) {
            return true;
        }

        for (LampLocation lamp : lamps) {
            if ((lamp.getRow() == r || lamp.getCol() == c) && !isBlocked(r, c, lamp)) {
                return true;
            }
        }

        return false;
    }

    public boolean isIllegal(List<LampLocation> lamps) {
        for (LampLocation lamp : lamps) {
            if (this.row == lamp.getRow() && this.col == lamp.getCol()) {
                continue;
            }
            if ((this.row == lamp.getRow() || this.col == lamp.getCol()) && !isBlocked(this.row, this.col, lamp)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAdjacent(int r, int c) {
        return (Math.abs(this.row - r) == 1 && this.col == c) || (Math.abs(this.col - c) == 1 && this.row == r);
    }

    private boolean isBlocked(int r, int c, LampLocation lamp) {
        // Add logic to check if a wall or other obstruction blocks the line of sight
        return false;
    }
}
