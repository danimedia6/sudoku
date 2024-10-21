package com.example.sudoku.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code SudokuBoard} class represents the model of a 6x6 SudokuBoard board.
 * It provides the necessary methods to interact with the board, including
 * retrieving, setting numbers, validating moves, and resetting the board.
 */
public class Sudoku {
    public ArrayList<int[]> sudokuTable;
    private boolean[][] editable;
    /**
     * Initializes the SudokuBoard board with predefined numbers and editable cells.
     * The board is represented as a 6x6 grid, where 0 indicates an empty cell.
     */
    public Sudoku() {
        // Definir 5 tableros diferentes
        selectRandomBoard();

        // Define las celdas editables (puedes personalizar esto para cada tablero)
        editable = new boolean[][]{
                {true, true, false, true, true, true},
                {false, false, false, false, true, false},
                {true, false, true, true, false, true},
                {false, true, true, false, false, true},
                {true, false, false, true, true, false},
                {false, false, true, false, false, true}
        };
    }

    public void selectRandomBoard(){
        int[][] board1 = {
                {1, 0, 4, 0, 0, 6},
                {0, 0, 0, 4, 0, 0},
                {0, 0, 1, 0, 0, 4},
                {4, 0, 0, 0, 2, 0},
                {6, 0, 0, 0, 4, 0},
                {0, 0, 2, 0, 0, 5}
        };

        int[][] board2 = {
                {0, 1, 0, 0, 3, 0},
                {5, 0, 0, 0, 0, 6},
                {0, 0, 3, 6, 0, 0},
                {1, 0, 0, 0, 0, 3},
                {0, 4, 0, 0, 0, 2},
                {0, 0, 1, 0, 5, 0}
        };

        int[][] board3 = {
                {0, 1, 0, 4, 0, 0},
                {4, 0, 0, 6, 0, 0},
                {2, 0, 0, 0, 5, 0},
                {0, 0, 1, 0, 6, 0},
                {0, 5, 0, 1, 0, 0},
                {1, 0, 0, 0, 2, 0}
        };

        int[][] board4 = {
                {0, 0, 0, 4, 0, 0},
                {4, 3, 0, 0, 0, 1},
                {0, 4, 6, 0, 0, 0},
                {0, 0, 0, 1, 4, 0},
                {5, 0, 0, 0, 0, 2},
                {2, 0, 0, 6, 0, 0}
        };

        int[][] board5 = {
                {0, 1, 0, 0, 0, 3},
                {0, 3, 0, 1, 0, 0},
                {5, 0, 0, 6, 0, 0},
                {0, 0, 4, 3, 0, 0},
                {2, 5, 0, 0, 0, 0},
                {0, 0, 0, 2, 0, 5}
        };

        // Almacena los tableros en una lista
        List<int[][]> boards = Arrays.asList(board1, board2, board3, board4, board5);

        // Selecciona uno de los tableros aleatoriamente

        int randomIndex = (int) (Math.random() * boards.size());
        sudokuTable = new ArrayList<>(Arrays.asList(boards.get(randomIndex)));



    }

    /**
     * Retrieves the number at the specified position on the board.
     *
     * @param row The row index (0-5).
     * @param column The column index (0-5).
     * @return The number at the specified position, or 0 if the cell is empty.
     */
    public int getNumber(int row, int column){

        return sudokuTable.get(row)[column];
    }

    public void generateNewBoard() {
        // Reinicia el tablero
        for (int row = 0; row < 6; row++) {
            Arrays.fill(sudokuTable.get(row), 0); // Rellena la fila con ceros
        }

        // Lógica simple para llenar el tablero (mejorar esta parte según las reglas del SudokuBoard)
        // Este es solo un ejemplo. Necesitarás implementar un algoritmo adecuado para generar tableros de SudokuBoard.
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int num;
                do {
                    num = (int) (Math.random() * 6) + 1; // Genera un número entre 1 y 6
                } while (!isValidMove(i, j, num)); // Verifica si el número es válido
                sudokuTable.get(i)[j] = num; // Coloca el número en la celda
            }
        }
    }


    /**
     * Sets a number at the specified position on the board.
     *
     * @param number The number to set (1-6).
     * @param row The row index (0-5).
     * @param column The column index (0-5).
     */
    public void setNumber(int number, int row, int column){

        sudokuTable.get(row)[column] = number;
    }
    /**
     * Resets the board by clearing all editable cells to 0.
     * Non-editable cells remain unchanged.
     */
    public void resetBoard() {
        generateNewBoard();

    }
    /**
     * Checks if a specific cell is editable.
     *
     * @param row The row index (0-5).
     * @param column The column index (0-5).
     * @return {@code true} if the cell is editable; {@code false} otherwise.
     */
    public boolean isEditable(int row, int column) {
        return editable[row][column];
    }
    /**
     * Validates whether placing a number in the specified cell is a valid move.
     * A move is valid if the number does not already exist in the same row,
     * column, or 2x3 quadrant.
     *
     * @param row The row index (0-5).
     * @param col The column index (0-5).
     * @param number The number to place (1-6).
     * @return {@code true} if the move is valid; {@code false} otherwise.
     */
    public boolean isValidMove(int row, int col, int number) {
        // Verifica si el número ya está en la fila
        for (int i = 0; i < 6; i++) {
            if (getNumber(row, i) == number) {
                return false; // El número ya está en la fila
            }
        }

        // Verifica si el número ya está en la columna
        for (int i = 0; i < 6; i++) {
            if (getNumber(i, col) == number) {
                return false; // El número ya está en la columna
            }
        }

        // Verifica si el número ya está en el cuadrante correspondiente
        int qRow = (row / 2) * 2; // Encuentra la fila superior izquierda del cuadrante
        int qCol = (col / 3) * 3; // Encuentra la columna superior izquierda del cuadrante
        for (int i = qRow; i < qRow + 2; i++) {
            for (int j = qCol; j < qCol + 3; j++) {
                if (getNumber(i, j) == number) {
                    return false; // El número ya está en el cuadrante
                }
            }
        }

        return true; // El número se puede colocar en la celda
    }


}
