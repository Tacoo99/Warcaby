package com.checkers.warcaby;

import javafx.fxml.FXML;
import com.checkers.warcaby.NewGame;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController{

    @FXML
    private Button exitBtn;

    @FXML
    private Button loadGameBtn;

    @FXML
    private Button newGameBtn;

    @FXML
    private Button optionsBtn;

    static Color player1Color;
    static Color player2Color;

    static Color king1Color;
    static Color king2Color;


    public void setColors(Color player1, Color player2, Color king1, Color king2){
        player1Color = player1;
        player2Color = player2;
        king1Color = king1;
        king2Color = king2;
        System.out.println("Gracz 1 - kolor: " + player1Color);
        System.out.println("Gracz 2 - kolor: " + player2Color);
        System.out.println("Gracz 1 - król: " + king1Color);
        System.out.println("Gracz 2 - król: " + king2Color);
    }

    public static Color setColor1(){
        return player1Color;
    }

    public static Color setColor2(){
        return player2Color;
    }

    public static Color setKing1(){
        return king1Color;
    }

    public static Color setKing2(){
        return king2Color;
    }

    @FXML
    public void newGame() {
        Stage mainMenu = (Stage) newGameBtn.getScene().getWindow();
        mainMenu.close();

        NewGame newgame = new NewGame();
        newgame.setColors(player1Color, player2Color, king1Color, king2Color);
        newgame.start();
    }

    @FXML
    protected void loadGame(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt") );
        fileChooser.setTitle("Wczytaj plik");
        Stage stage = (Stage) loadGameBtn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file == null){
            setAlert(Alert.AlertType.ERROR,"Wystąpił błąd", "Nie wybrano pliku!" );
        }
        else {
            setAlert(Alert.AlertType.INFORMATION, "Wczytywanie stanu gry", "Wybrano plik: " + file.getName());
        }

    }

    @FXML
    protected void openOptions(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("Options.fxml"));
            Stage stage2 = (Stage) optionsBtn.getScene().getWindow();
            stage2.close();
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:src/main/resources/com/checkers/warcaby/icon.png"));
            stage.setTitle("Warcaby - opcje");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Błąd przy otwieraniu okna: " + e);
        }

    }


    @FXML
    protected void exitGame(){
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Wyjście z gry");
        alert.setContentText("Na pewno chcesz opuścić grę?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Tak");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Nie");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK){
            stage.close();
        }
        else if(result.get() == ButtonType.CANCEL){
            alert.close();
        }

    }

    @FXML
    protected void projectSubject() {
        setAlert(Alert.AlertType.INFORMATION, "Temat projektu", "Temat: Warcaby z funkcją zapisu/odczytu stanu gry");
    }

    @FXML
    protected void projectTeam() {
        setAlert(Alert.AlertType.INFORMATION, "Zespół", "Zespół projektowy: \n\n 1. Wojciech Kozioł \n 2. Dawid Bujak \n 3. Konrad Jaszczyk");
    }

    private void setAlert(Alert.AlertType type, String title, String text){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(text);

        alert.showAndWait();
    }

}