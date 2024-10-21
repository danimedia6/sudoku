package com.example.sudoku.controller;
import javafx.scene.Node;
import com.example.sudoku.model.Sudoku;
import com.example.sudoku.view.GameStage;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Main controller class that manages the logic of the SudokuBoard game.
 * It handles interactions between the game model and the user interface.
 */

public class GameController {
    Sudoku sudoku = new Sudoku();
    TextField[][] textFields = new TextField[6][6];
    int numAyudas = 3;
    @FXML
    private GridPane sudokuGridPane;

    /**
     * Starts a new game by resetting the board and clearing editable cells.
     */
    @FXML
    private void handleNewGame() {
        System.out.println("Prueba");
        Alert alertReset = new Alert(Alert.AlertType.CONFIRMATION);
        alertReset.setHeaderText("¿Estás seguro que deseas iniciar nuevamente el juego?");
        alertReset.setContentText("Se perderá todo tu progreso hasta ahora.");
        alertReset.setTitle("Reinicio de partida");
        alertReset.showAndWait();
            if (alertReset.getResult() == ButtonType.OK) {

                // limpia solo los textFields editables en la interfaz
                for (Node node : sudokuGridPane.getChildren()) {
                    if (node instanceof TextField) {
                        TextField textField = (TextField) node;
                        int row = GridPane.getRowIndex(textField);
                        int col = GridPane.getColumnIndex(textField);

                        // Limpiar las celdas que no son editables (editable == false)
                        if (!sudoku.isEditable(row, col)) {
                            textField.setStyle("");
                            textField.setText(""); // Limpiar el TextField
                        }
                    }
                }
                initialize();
            }


    }

    /**
     * Displays a help alert and suggests a number for an empty cell.
     */

    @FXML
    private Button helpButton;

    @FXML
    private void handleHelp() {
        if(numAyudas>0) {
            Alert alertHelp = new Alert(Alert.AlertType.INFORMATION);
            alertHelp.setTitle("Ayuda generada");
            alertHelp.setHeaderText("Has obtenido una ayuda, te quedan " + (numAyudas-1) + " ayudas. Ingresa el número sugerido en color rosado");
            alertHelp.showAndWait();
            int[] emptyCell = findEmptyCell();
            if (emptyCell != null) {
                int row = emptyCell[0];
                int col = emptyCell[1];
                int suggestedNumber = suggestNumber(row, col);
                if (suggestedNumber != -1) {
                    TextField suggestedField = textFields[row][col];
                    suggestedField.setText(String.valueOf(suggestedNumber));
                    suggestedField.setStyle("-fx-background-color: pink;-fx-font-size: 30; -fx-font-weight: bold;"); // Estilo destacado
                } else {
                    showAlert("No hay sugerencias disponibles en este momento.");
                }
            } else {
                showAlert("No hay celdas vacías disponibles.");
            }
            numAyudas--;
        } else {
            helpButton.setDisable(true);
            Alert alertHelp = new Alert(Alert.AlertType.INFORMATION);
            alertHelp.setTitle("Ayudas deshabilitadas");
            alertHelp.setHeaderText("Lo sentimos, ya has agotado todas las ayudas disponibles.");
            alertHelp.showAndWait();
        }
    }


    /**
     * Displays an alert with the given message.
     *
     * @param message The message to display in the alert.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Finds the first empty cell in the SudokuBoard grid.
     *
     * @return An array with the row and column of the empty cell, or {@code null} if no empty cells are found.
     */
    private int[] findEmptyCell() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (sudoku.getNumber(i, j) == 0) { // 0 indica que la celda está vacía
                    return new int[]{i, j}; // Retorna la fila y columna de la celda vacía
                }
            }
        }
        return null; // No hay celdas vacías
    }

    /**
     * Suggests a valid number for the specified cell.
     *
     * @param row The row of the cell.
     * @param col The column of the cell.
     * @return A valid number or -1 if no valid number is found.
     */
    private int suggestNumber(int row, int col) {
        for (int number = 1; number <= 6; number++) {
            if (sudoku.isValidMove(row, col, number)) { // Verifica si el movimiento es válido
                return number; // Retorna el primer número válido encontrado
            }
        }
        return -1; // No se encontró un número válido
    }

    /**
     * Exits the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }
    /**
     * Initializes the SudokuBoard grid with the current board state.
     */
    @FXML
    public void initialize(){
        System.out.println("El juego ha iniciado nuevamente");
        sudokuGridPane.getChildren().clear();
        System.out.println("Se ha limpiado el tablero.");
        sudoku.selectRandomBoard();
        System.out.println("Se ha cargado un tablero aleatorio: " +sudoku.sudokuTable);

        for (int i=0; i<6; i++){
            for (int j=0; j<6; j++){
                TextField textField = new TextField();
                textField.setPrefWidth(50);
                textField.setPrefHeight(50);
                textField.setPadding(new Insets(0));
                textField.setAlignment(Pos.CENTER);
                textField.setStyle("-fx-font-size: 30; -fx-font-family: Calibri; -fx-text-fill: TEAL");
                textField.setTranslateX(((int) (i/2))*10);
                textField.setTranslateY(((int) (j/3))*10);

                int number = sudoku.getNumber(i, j);
                System.out.println("Número en [" + i + "][" + j + "]: " + number);
                if(number != 0){
                    textField.setText(String.valueOf(number));
                    textField.setDisable(true);
                    textField.setStyle("-fx-font-size: 30; -fx-font-family: Calibri; -fx-text-fill: BLACK; -fx-font-weight: 600" );
                }
                textField.setOnKeyTyped(this::onlyNumbersInField);
                textFields[i][j] = textField;
                sudokuGridPane.add(textField, i, j);
            }
        }
    }




    /**
     * Handles key events to ensure only numbers between 1 and 6 are entered.
     *
     * @param event The key event triggered by user input.
     */
    private void onlyNumbersInField(KeyEvent event){
        TextField field = (TextField) event.getSource();
        if(field.getText() != ""){
            char textNumber = field.getText().charAt(0);
            field.setText("");
            if (Character.isDigit(textNumber)) {
                int number = Character.getNumericValue(textNumber);
                if (number >= 1 && number <= 6) {  // Validar que sea entre 1 y 6
                    field.setText(String.valueOf(number));
                    sudoku.setNumber(number, getFieldIndex(field)[0], getFieldIndex(field)[1]);
                    checkNumber(field, number, getFieldIndex(field)[0], getFieldIndex(field)[1]);
                    if (checkWin()) {
                        if (lastCheck()) {
                            showAlertWin();
                        }
                    }
                }
            }
        } else if(field.getText() == ""){
            sudoku.setNumber(0,getFieldIndex(field)[0], getFieldIndex(field)[1]);
            emptyStyle(field);
        }
    }
    /**
     * Checks if the entered number is valid and updates the cell style accordingly.
     */
    private void checkNumber(TextField field, int number, int row, int column){
        boolean isNumberOk = true;
        for (int i=0; i<6; i++){
            if(i != row){
                if(number == sudoku.getNumber(i, column)){
                    wrongStyle(field);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("El valor ingresado no es válido");
                    alert.show();
                    isNumberOk = false;}
            }
        }for (int i=0; i<6; i++){
            if(i != column){
                if(number == sudoku.getNumber(row, i)){
                    wrongStyle(field);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("El valor ingresado no es válido");
                    alert.show();
                    isNumberOk = false;}
            }
        }
        int qRow = row/2;
        int qColumn = column/3;
        for (int i = qRow*2; i< (qRow + 1)*2; i++){
            for(int j = qColumn*3; j< (qColumn + 1)*3; j++){
                if(number == sudoku.getNumber(i,j) && i != row && j != column){
                    wrongStyle(field);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("El valor ingresado no es válido");
                    alert.show();
                    isNumberOk = false;}
            }
        }
        if (isNumberOk)
            emptyStyle(field);
    }
    /**
     * Checks if the player has completed the puzzle.
     *
     * @return {@code true} if the puzzle is completed; {@code false} otherwise.
     */
    private boolean checkWin(){
        for (int i=0; i<6;i++){
            for(int j=0; j<6; j++){
                if (sudoku.getNumber(i,j) == 0){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates the final solution of the SudokuBoard puzzle.
     *
     * @return {@code true} if the solution is correct; {@code false} otherwise.
     */
    private boolean lastCheck(){
        for(int i=0; i<6; i++){
            int checkRow = 0, checkColumn = 0;
            for (int j=0; j<6; j++){
                checkRow += sudoku.getNumber(i,j);
                checkColumn += sudoku.getNumber(j,i);
            }
            if(checkRow != 21 || checkColumn != 21)
                return false;
        }
        for (int bigI = 0; bigI < 3; bigI++){
            for (int bigJ = 0; bigJ < 2; bigJ++){
                int checkQuadrante = 0;
                for (int i = bigI*2; i< (bigI + 1)*2; i++) {
                    for (int j = bigJ * 3; j < (bigJ + 1) * 3; j++) {
                        checkQuadrante += sudoku.getNumber(i,j);
                    }
                }
                if (checkQuadrante != 21)
                    return false;
            }
        }
        return true;
    }
    private void emptyStyle(TextField field){
        field.setStyle("-fx-font-size: 30; -fx-font-family: Calibri; -fx-text-fill: TEAL");
    }
    private void wrongStyle(TextField field){
        field.setStyle("-fx-font-size: 30; -fx-font-family: Calibri; -fx-text-fill: black; -fx-background-color: red");
    }
    private int[] getFieldIndex(TextField field){
        for(int i=0; i<6; i++){
            for(int j=0; j<6; j++){
                if(textFields[i][j] == field){
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0,0};
    }
    /**
     * Displays a win alert with options to start a new game or exit.
     */
    private void showAlertWin(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SudokuBoard");
        alert.setHeaderText("Felicidades");
        alert.setContentText("Haz completado todo el tablero de manera correcta");

        ButtonType newGame = new ButtonType("Nueva partida");
        ButtonType exitGame = new ButtonType("Salir del Juego");

        alert.getButtonTypes().setAll(newGame,exitGame);

        alert.setOnCloseRequest(dialogEvent -> {
            if (alert.getResult() == newGame){
                GameStage.deleteInstance();
                try {
                    GameStage.getInstance();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                GameStage.deleteInstance();
            }
        });


        alert.showAndWait();
    }
}
