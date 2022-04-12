package com.checkers.warcaby;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class NewGame {

    List<Circle> circles = new ArrayList<>();
    boolean oneHuman = true, twoHuman = true;
    int depth = 6;

    Color player1Color = MainController.setColor1();
    Color player2Color = MainController.setColor2();
    Color king1Color = MainController.setKing1();
    Color king2Color = MainController.setKing2();


    public void setColors(Color player1, Color player2, Color king1, Color king2){

        //Króle-------------------------------------------------------------------------

        if(king1Color != null) {
            king1Color = king1;
            System.out.println("Kolor króla gracza 1:" + king1Color);
        }
        else{
            System.out.println("Kolor króla gracza 1 ustawiono na domyślny");
            king1Color = Color.BLUEVIOLET;
        }
        if(king2Color != null) {
            king2Color = king2;
            System.out.println("Kolor króla gracza 2:" + king2Color);
        }
        else{
            System.out.println("Kolor króla gracza 1 ustawiono na domyślny");
            king2Color = Color.CYAN;
        }

        //Kolory---------------------------------------------------------------------------

        if(player1 != null) {
            player1Color = player1;
            System.out.println("Kolor gracza 1:" + player1Color);
        }
        else{
            System.out.println("Kolor gracza 1 ustawiono na domyślny");
            player1Color = Color.GREY;
        }
        if(player2 != null) {
            player2Color = player2;
            System.out.println("Kolor gracza 2:" + player2Color);
        }
        else {
            System.out.println("Kolor gracza 2 ustawiono na domyślny");
            player2Color = Color.RED;
        }

    }

    private void writeToFile(String filename, String text){
        try {
            File file = new File(filename);
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(text);
            br.newLine();
            br.close();
            fr.close();
            System.out.println("Współrzędne wpisano do pliku: " + filename);
        } catch (IOException e) {
            System.out.println("Wystąpił błąd");
            e.printStackTrace();
        }
    }

    void updateScene(Group root, Game game, Label chanceLabel, Label maxScoreLabel, Label minScoreLabel) {

        State state = game.getState();
        for (int i = 0; i < state.getMinPieceList().size(); i++) {
            Circle circle = new Circle();
            circle.setCenterY(165 + state.getMinPieceList().get(i).getPosition().getxCoordinate() * 90);
            circle.setCenterX(165 + state.getMinPieceList().get(i).getPosition().getyCoordinate() * 90);
            if(state.getMinPieceList().get(i).isKing()) {
                circle.setFill(king1Color);
            }
            else{
               circle.setFill(player1Color);
            }
            circle.setRadius(40);
            circle.setFocusTraversable(true);
            final Alert[] alert = new Alert[1];
            if (twoHuman) {
                circle.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                    clearScene(root);
                    updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel);
                    if (!game.getState().isMaxChance()){
                        double dragBaseX = circle.getCenterX();
                        double dragBaseY = circle.getCenterY();
                        int y = (int) (dragBaseX - 140) / 90;
                        int x = (int) (dragBaseY - 140) / 90;

                        Piece p = state.getBoard().get(new Coordinate(x, y));

                        if (state.getStateActions().isEmpty()) System.out.println("Gracz2 wygrał");

                        List<Action> actions = new ArrayList<>();
                        if(state.getStateActions().containsKey(p.getPosition())) actions = state.getStateActions().get(p.getPosition());
                        else {
                            alert[0] = new Alert(Alert.AlertType.INFORMATION);
                            alert[0].setTitle("Informacja");
                            alert[0].setHeaderText(null);
                            alert[0].setContentText("Pionek nie moze wykonac tego ruchu \nlub:\n1. Jest zablokowany. albo\n2. Mozesz zbic inny pionek");
                            alert[0].showAndWait();
                        }

                        for (Action action : actions) {
                            Circle c = new Circle();
                            c.setCenterY(165 + action.getNewCoordinate().getxCoordinate() * 90);
                            c.setCenterX(165 + action.getNewCoordinate().getyCoordinate() * 90);
                            c.setFill(Color.PINK);
                            c.setRadius(40);
                            c.setFocusTraversable(true);
                            circles.add(c);

                            c.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent1 -> {
                                State s = state.getNextState(action);

                                String player1X = String.valueOf(c.getCenterX());
                                String player1Y = String.valueOf(c.getCenterY());
                                String coordinations1 = "X: " + player1X.substring(0, player1X.length() - 4) + " Y: " + player1Y.substring(0, player1Y.length() - 4);
                                //TODO
                                // Dodać pobieranie cyfry oraz litery z tablicy, pewnie zrobię to za pomocą numeru indeksu przekazywanym w zmiennej, np. tablica.get(indeks)
                                writeToFile("player1.save", coordinations1);



                                if(s.getStateActions().isEmpty()) {
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                    alert1.setTitle("Informacja");
                                    alert1.setHeaderText(null);
                                    alert1.setContentText("Gracz2 wygral!");
                                    alert1.showAndWait();
                                    Platform.exit();
                                    System.exit(0);
                                }

                                game.setState(s);
                                clearScene(root);
                                updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel);
                                if (!game.getState().isContinuedState()) game.getState().setMaxChance(true);
                                else {
                                    alert[0] = new Alert(Alert.AlertType.INFORMATION);
                                    alert[0].setTitle("Informacja");
                                    alert[0].setHeaderText(null);
                                    alert[0].setContentText("Powtorz ruch.");
                                    alert[0].showAndWait();
                                }
                            });
                            root.getChildren().add(c);
                        }
                    }
                    else System.out.println("Tura gracza nr1");
                    mouseEvent.consume();
                });
            }
            circles.add(circle);
            root.getChildren().add(circle);
        }
        for (int i = 0; i < state.getMaxPieceList().size(); i++){
            Circle circle = new Circle();
            circle.setCenterY(165 + state.getMaxPieceList().get(i).getPosition().getxCoordinate() * 90);
            circle.setCenterX(165 + state.getMaxPieceList().get(i).getPosition().getyCoordinate() * 90);
            if(state.getMaxPieceList().get(i).isKing()) {
                circle.setFill(king2Color);
            }
            else{
                circle.setFill(player2Color);
            }
            circle.setRadius(40);
            circle.setFocusTraversable(true);

            final Alert[] alert = new Alert[1];
            if (oneHuman) {
                circle.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                    clearScene(root);
                    updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel);
                    if (game.getState().isMaxChance()) {
                        double dragBaseX = circle.getCenterX();
                        double dragBaseY = circle.getCenterY();
                        int y = (int) (dragBaseX - 140) / 90;
                        int x = (int) (dragBaseY - 140) / 90;

                        Piece p = state.getBoard().get(new Coordinate(x, y));

                        if (p.isKing()) System.out.println("KRÓL");

                        List<Action> actions = new ArrayList<>();
                        if(state.getStateActions().containsKey(p.getPosition())) actions = state.getStateActions().get(p.getPosition());
                        else {
                            alert[0] = new Alert(Alert.AlertType.INFORMATION);
                            alert[0].setTitle("Informacja");
                            alert[0].setHeaderText(null);
                            alert[0].setContentText("Pionek nie moze wykonac tego ruchu \nlub:\n1. Jest zablokowany. albo\n2. Mozesz zbic inny pionek");
                            alert[0].showAndWait();
                        }

                        for (Action action : actions) {
                            Circle c = new Circle();
                            c.setCenterY(165 + action.getNewCoordinate().getxCoordinate() * 90);
                            c.setCenterX(165 + action.getNewCoordinate().getyCoordinate() * 90);
                            c.setFill(Color.PINK);
                            c.setRadius(40);
                            c.setFocusTraversable(true);
                            circles.add(c);

                            c.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent12 -> {
                                State s = state.getNextState(action);
                                game.setState(s);

                                String player2X = String.valueOf(c.getCenterX());
                                String player2Y = String.valueOf(c.getCenterY());
                                String coordinations2 = "X: " + player2X.substring(0, player2X.length() - 4) + " Y: " + player2Y.substring(0, player2Y.length() - 4);
                                //TODO
                                // Dodać pobieranie cyfry oraz litery z tablicy, pewnie zrobię to za pomocą numeru indeksu przekazywanym w zmiennej, np. tablica.get(indeks)
                                writeToFile("player2.save", coordinations2);


                                if(s.getStateActions().isEmpty()) {
                                    Alert alert12 = new Alert(Alert.AlertType.INFORMATION);
                                    alert12.setTitle("Informacja");
                                    alert12.setHeaderText(null);
                                    alert12.setContentText("Gracz2 wygral!");
                                    alert12.showAndWait();
                                    Platform.exit();
                                    System.exit(0);
                                }

                                clearScene(root);
                                updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel);
                                System.out.println("Koniec tury gracza nr1");
                                if (!game.getState().isContinuedState()) game.getState().setMaxChance(false);
                                else {
                                    alert[0] = new Alert(Alert.AlertType.INFORMATION);
                                    alert[0].setTitle("Informacja");
                                    alert[0].setHeaderText(null);
                                    alert[0].setContentText("Powtorz ruch.");
                                    alert[0].showAndWait();
                                }
                                System.out.println("Ruch " + game.getState().isMaxChance());
                            });
                            root.getChildren().add(c);
                        }
                    }
                    else System.out.println("Tura gracza nr2");
                    mouseEvent.consume();
                });
            }
            circles.add(circle);
            root.getChildren().add(circle);
        }
        String chanceLabelText = state.isMaxChance() ? "Tura Gracza 1." : "Tura Gracza 2.";
        chanceLabel.setText(chanceLabelText);
        maxScoreLabel.setText("Punkty Gracza nr1 : " + state.getMaxScore());
        minScoreLabel.setText("Punkty Gracza nr2: " + state.getMinScore());
    }

    void clearScene(Group root) {
        for (Circle circle : circles) {
            root.getChildren().remove(circle);
        }
        circles.clear();
    }



    int chooseDifficulty() {
        String[] arrayData = new String[]{"Ławy", "Normalny", "Trudny", "Zaawansownany", "Ekspert"};
        List<String> dialogData = Arrays.asList(arrayData);

        ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("Trudność");
        dialog.setHeaderText("Wybierz trudność gry");

        Optional<String> result = dialog.showAndWait();
        String selected = "Anulowano.";
        if (result.isPresent()) {
            selected = result.get();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wybieranie trudności gry");
            alert.setHeaderText(null);
            alert.setContentText("Nie można wystartować dopóki nie wybierzesz trudności gry.\nWychodzenie!");
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        }
        System.out.println(selected);

        switch (selected) {
            case "Ławy" -> {
                return 1;
            }
            case "Trudny" -> {
                return 3;
            }
            case "Zaawansownany" -> {
                return 4;
            }
            case "Ekspert" -> {
                return 5;
            }
            default -> {
                return 2;
            }
        }
    }

    String chooseGameMode() {
        final String [] arrayData = {"Człowiek vs AI", "Człowiek vs Człowiek", "AI vs AI", };
        List<String> dialogData = Arrays.asList(arrayData);
        ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("Wybierz tryb");
        dialog.setHeaderText("Wybierz tryb gry");
        Optional result = dialog.showAndWait();
        String selected = "anulowano.";
        if (result.isPresent()) {
            selected = (String) result.get();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wychodzenie");
            alert.setHeaderText(null);
            alert.setContentText("Musisz wybrac poziom trudnosci.\nWychodzenie!");
            alert.showAndWait();
            Platform.exit();
            System.exit(0);
        }
        return selected;
    }

    void newWindow(String filename, int y, int x){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(filename));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image("file:src/main/resources/com/checkers/warcaby/icon.png"));
            stage.setResizable(false);
            stage.setX(x);
            stage.setY(y);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    Player1Info player1Info = new Player1Info();
    Player2Info player2Info = new Player2Info();

    //This is the first function that starts.
    public void start() {

        Canvas canvas = new Canvas();
        Group root = new Group(canvas);
        Scene scene= new Scene(root, 1000, 1000);

        newWindow("player1Info.fxml", 100, 200);
        newWindow("player2Info.fxml", 100, 1500);
        Stack stack1 = new Stack();
        for (String s : Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H")) {
            stack1.push(s);
        }
        Stack stack2 = new Stack();
        for (String s : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) {
            stack2.push(s);
        }


        //Making the board. Stacking the squares.
        boolean red = true;
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rectangle = new Rectangle(120 + 90 * i, 120 + 90 * j, 90, 90);
                if (red) {
                    rectangle.setFill(Color.web("#ECDBD3"));
                }
                else {
                    rectangle.setFill(Color.web("#43251A"));
                }
                red = !red;
                root.getChildren().add(rectangle);
            }
            red = !red;
        }
        int x = 65;
        Text text;
        for(int t = 0; t < 8; t++){
            x = x + 90;
            text = new Text((String) stack1.get(t));
            text.setFont(Font.font(45));
            text.setX(x);
            text.setY(90);
            root.getChildren().add(text);
        }

        int y = 90;
        Text text2;
        for(int t = 0; t < 8; t++){
            y = y + 90;
            text2 = new Text((String) stack2.get(t));
            text2.setFont(Font.font(45));
            text2.setX(50);
            text2.setY(y);
            root.getChildren().add(text2);
        }

        Label chanceLabel = new Label("GRACZ 1: Twój ruch");
        chanceLabel.setFont(Font.font(15));
        chanceLabel.setLayoutX(760);
        chanceLabel.setLayoutY(900);
        root.getChildren().add(chanceLabel);

        Label maxScoreLabel = new Label("Punkty Gracza 1: 0");
        maxScoreLabel.setFont(Font.font(15));
        maxScoreLabel.setLayoutX(100);
        maxScoreLabel.setLayoutY(900);
        root.getChildren().add(maxScoreLabel);

        Label minScoreLabel = new Label("Punkty Gracza 2 : 0");
        minScoreLabel.setFont(Font.font(15));
        minScoreLabel.setLayoutX(360);
        minScoreLabel.setLayoutY(900);
        root.getChildren().add(minScoreLabel);

        Game game = new Game();
        updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Warcaby!");
        primaryStage.getIcons().add(new Image("file:src/main/resources/com/checkers/warcaby/icon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnMousePressed(mouseEvent -> {
            if (!game.hasFinished()) {
                game.playNextMove(oneHuman, twoHuman, depth);
                clearScene(root);
                updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel);
            }
        });

        String selected = chooseGameMode();

        switch (selected) {
            case "AI vs AI" -> {
                oneHuman = false;
                twoHuman = false;
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informacja");
                alert.setHeaderText(null);
                alert.setContentText("Kliknij myszą żeby AI wykonało kolejny ruch!.");
                alert.showAndWait();
            }
            case "Człowiek vs AI" -> {
                oneHuman = true;
                twoHuman = false;
                depth = chooseDifficulty();
            }
            case "Człowiek vs Człowiek" -> {
                oneHuman = true;
                twoHuman = true;
            }
        }
    }

}
