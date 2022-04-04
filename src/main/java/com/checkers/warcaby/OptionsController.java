package com.checkers.warcaby;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


public class OptionsController {


    @FXML
    private Button closeBtn;

    @FXML
    private ComboBox<String> dpiCombo;

    @FXML
    private ColorPicker p1Chooser;

    @FXML
    private ColorPicker p1King;

    @FXML
    private ColorPicker p2Chooser;

    @FXML
    private ColorPicker p2King;

    @FXML
    private Label resolutionLabel;

    @FXML
    private Button saveBtn;


    private final ObservableList<String> dpiList = FXCollections.observableArrayList("716x539", "1366x768", "1920x1080");

    public OptionsController() {
    }


    public void initialize() {
        dpiCombo.setItems(dpiList);
        p1Chooser.setValue(Color.GREY);
        p2Chooser.setValue(Color.RED);
        p1King.setValue(Color.CYAN);
        p2King.setValue(Color.BLUEVIOLET);


    }

    @FXML
    protected void close() {
        Stage stageClose = (Stage) closeBtn.getScene().getWindow();

        if ((dpiCombo.getValue() != null) || (p1Chooser.getValue() != Color.GREY) || (p2Chooser.getValue() != Color.RED) ||
                (p1King.getValue() !=Color.CYAN) || (p2King.getValue() != Color.BLUEVIOLET))
        {
            Alert alertClose = new Alert(Alert.AlertType.CONFIRMATION);
            alertClose.setHeaderText(null);
            alertClose.setTitle("Potwierdź niezapisane zmiany");
            alertClose.setContentText("Masz niezapisane zmiany, na pewno chcesz wyjść?");
            alertClose.initModality(Modality.APPLICATION_MODAL);
            alertClose.initOwner(stageClose);
            ((Button) alertClose.getDialogPane().lookupButton(ButtonType.OK)).setText("Tak");
            ((Button) alertClose.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Nie");
            Optional<ButtonType> resultClose = alertClose.showAndWait();

            if (resultClose.isPresent() && resultClose.get() == ButtonType.OK) {
                alertClose.close();
                stageClose.close();
            } else if (resultClose.isPresent() && resultClose.get() == ButtonType.CANCEL) {
                alertClose.close();
            }
        }
        else{
            stageClose.close();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("mainMenu.fxml"));
                Stage stage2 = (Stage) closeBtn.getScene().getWindow();
                stage2.close();
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.getIcons().add(new Image("file:src/main/resources/com/checkers/warcaby/icon.png"));
                stage.setTitle("Warcaby!");
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                System.out.println("Błąd przy otwieraniu okna: " + e);
            }
        }
    }

    @FXML
    protected void setDpiCombo() {
        Stage stage = (Stage) dpiCombo.getScene().getWindow();
        int width = (int) stage.getWidth();
        int height = (int) stage.getHeight();
        String mergedRes = width + "x" + height;
        resolutionLabel.setText("Aktualna rozdzielczość: " + mergedRes);
    }


    @FXML
    protected void saveOptions() {

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        try {
            mainLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainController mainController = mainLoader.getController();

        String dpi = "";
        String color1 = "";
        String color2 = "";
        String king1 = "";
        String king2 = "";

        boolean changedDPI = false;
        boolean changedColor1 = false;
        boolean changedColor2 = false;
        boolean changedKing1 = false;
        boolean changedKing2 = false;



        Stage stage = (Stage) saveBtn.getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Zapisywanie zmian");
        alert.setContentText("Na pewno chcesz zapisać zmiany?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("Tak");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Nie");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {

            if( p1Chooser.getValue() != Color.GREY ){
                changedColor1 = true;
            }

            if( p2Chooser.getValue() != Color.RED ){
                changedColor2 = true;
            }

            if(p1King.getValue() !=Color.CYAN){
                changedKing1 = true;
            }

            if(p2King.getValue() != Color.BLUEVIOLET){
                changedKing2 = true;
            }

            if (dpiCombo.getValue() != null) {
                changedDPI = true;

                String getChosenDpi = dpiCombo.getValue();

                if (Objects.equals(getChosenDpi, "716x539")) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
                        Stage stage3 = (Stage) saveBtn.getScene().getWindow();
                        Scene scene = new Scene(loader.load());
                        stage3.setScene(scene);
                        stage3.setX(0);
                        stage3.setY(0);
                        stage3.setResizable(false);
                        mainController.setColors(p1Chooser.getValue(), p2Chooser.getValue(), p1King.getValue(), p2King.getValue());
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }

                if (Objects.equals(getChosenDpi, "1366x768")) {

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuHD.fxml"));
                        Stage stage2 = (Stage) saveBtn.getScene().getWindow();
                        Scene scene = new Scene(loader.load());
                        stage2.setScene(scene);
                        stage2.setX(1366/2);
                        stage2.setY(768/2);
                        stage2.setResizable(false);
                        mainController.setColors(p1Chooser.getValue(), p2Chooser.getValue(), p1King.getValue(), p2King.getValue());

                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }

                if (Objects.equals(getChosenDpi, "1920x1080")) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenuFHD.fxml"));
                        Stage stage3 = (Stage) saveBtn.getScene().getWindow();
                        Scene scene = new Scene(loader.load());
                        stage3.setScene(scene);
                        stage3.setResizable(false);
                        stage3.setX(1920/2);
                        stage3.setY(1080/2);
                        mainController.setColors(p1Chooser.getValue(), p2Chooser.getValue(), p1King.getValue(), p2King.getValue());
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            }

            if(changedColor1){
                Color color1Hex = p1Chooser.getValue();
                color1 = "\nKolor figury pierwszego gracza: " + color1Hex;
            }

            if(changedColor2){
                Color color2Hex = p2Chooser.getValue();
                color2 = "\nKolor figury drugiego gracza oraz AI: " + color2Hex;
            }

            if(changedKing1){
                Color king1Hex = p1King.getValue();
                king1 = "\nKolor króla pierwszego gracza: " + king1Hex;
            }

            if(changedKing2){
                Color king2Hex = p2King.getValue();
                king2 = "\nKolor króla drugiego gracza: " + king2Hex;
            }

            if(changedDPI){
                dpi = "\nRozdzielczość: " + dpiCombo.getValue();
            }

            if( !(Objects.equals(dpi, "")) || !(Objects.equals(color1,"")) || !(Objects.equals(color2,""))
                    || !(Objects.equals(king1,"")) || !(Objects.equals(king2,""))
            )  {
                setAlert(Alert.AlertType.INFORMATION, "Zatwierdzono zmiany",dpi + color1 + color2 + king1 + king2);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
                    Stage stage3 = (Stage) saveBtn.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
                    stage3.setScene(scene);
                    stage3.setResizable(false);
                    mainController.setColors(p1Chooser.getValue(), p2Chooser.getValue(), p1King.getValue(), p2King.getValue());
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }

            else{
                setAlert(Alert.AlertType.ERROR, "Wystąpił błąd","Nie wybrano żadnych zmian!");
            }

        }
    }

    private void setAlert(Alert.AlertType type, String title, String text){
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.showAndWait();
    }
}

