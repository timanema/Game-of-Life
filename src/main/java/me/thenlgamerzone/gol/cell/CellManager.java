package me.thenlgamerzone.gol.cell;

import me.thenlgamerzone.gol.Settings;

import java.util.HashSet;
import java.util.Set;

/*
 * Copyright (c) 2016 Tim & Lukas
 * See LICENSE for license
 */
public class CellManager {
    private Cell[][] currentCells;
    private Set<Cell> allCells;

    public CellManager(int xCoord, int yCoord) {
        currentCells = new Cell[xCoord][yCoord];
        allCells = new HashSet<Cell>();

        // Populate arrays
        for (int column = 0; column < Settings.WIDTH.getSetting(); column++)
            for (int row = 0; row < Settings.HEIGTH.getSetting(); row++) {
                Cell cell = new Cell(column, row, Cell.CELL_STATE.DEAD);

                // Add cell to arrays
                currentCells[column][row] = cell;
                allCells.add(cell);
            }
    }

    /**
     * Returns the cell at a given location. Can be null if the location is invalid
     * @param x X coordinate
     * @param y Y coordinate
     * @return The cell
     */
    public Cell getCellAt(int x, int y) {
        return x >= Settings.WIDTH.getSetting() ||
                y >= Settings.HEIGTH.getSetting() ||
                x < 0 ||
                y < 0
                ? null : currentCells[x][y];
    }

    /**
     * Get the amount of neighbours of a cell at a given location
     * @param x X coordinate
     * @param y Y coordinate
     * @return Number of neighbours
     */
    public int getNeighbours(int x, int y) {
        return getNeighbours(getCellAt(x, y));
    }

    /**
     * Get the amount of neighbours of a cell
     * @param cell Cell to be checked
     * @return Number of neighbours
     */
    public int getNeighbours(Cell cell) {
        int aliveNeighbours = 0;

        // Loop though all the surrounding cells
        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            // Check if the current X is out of bounds
            if (x < 0 || x >= Settings.WIDTH.getSetting())
                continue;

            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                // Check if the current Y is out of bounds or if the current cell is the given cell
                if (y < 0 || y >= Settings.HEIGTH.getSetting() || (x == cell.getX() && y == cell.getY()))
                    continue;

                // Increase aliveNeighbours by one if the neighbour is alive
                if (getCellAt(x, y).isAlive())
                    aliveNeighbours++;
            }
        }
        return aliveNeighbours;
    }

    /**
     * Checks the next state and gives it that state
     * @param cell The cell to be checked
     */
    public void checkCell(Cell cell) {
        // Check if the cell is alive
        if(cell.isAlive()) {
            // Check for overpopulation
            if(getNeighbours(cell) >= 4)
                cell.setNextCellState(Cell.CELL_STATE.DEAD);

            // Check for starvation
            else if (getNeighbours(cell) < 2)
                cell.setNextCellState(Cell.CELL_STATE.DEAD);

            // Cell will survive this round
            cell.setNextCellState(Cell.CELL_STATE.ALIVE);
        } else {
            // Check for growth
            if(getNeighbours(cell) == 3)
                cell.setNextCellState(Cell.CELL_STATE.ALIVE);

            // Cell won't grow
            cell.setNextCellState(Cell.CELL_STATE.DEAD);
        }
    }

    /**
     * Updates the state of all cells
     */
    public void nextRound() {
        for(Cell cell : allCells) {
            // Change states
            cell.setState(cell.getNextCellState());
            cell.setNextCellState(null);
        }
    }

    /**
     * Returns a set with all the cells, dead or alive
     * @return Set containing all the cells
     */
    public Set<Cell> getAllCells() {
        return allCells;
    }

    /**
     * Returns a set with all the cells that will be alive next round
     * @return Set containing all the cells that will be alive next round
     */
    public Set<Cell> getNextAliveCells() {
        Set<Cell> nextCells = new HashSet<Cell>();

        for (Cell cell : allCells)
            if (cell.getNextCellState() == Cell.CELL_STATE.ALIVE)
                nextCells.add(cell);

        return nextCells;
    }
}
