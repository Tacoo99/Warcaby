package com.checkers.warcaby;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


public class NewGame {

    List<Circle> circles = new ArrayList<>();
    boolean oneHuman = true, twoHuman = true;
    int depth = 6;
    Stack stack1 = new Stack();
    Stack stack2 = new Stack();

    ArrayList Xstack = new ArrayList();

    ArrayList Ystack = new ArrayList();

    ArrayList Xstack2 = new ArrayList();

    ArrayList Ystack2 = new ArrayList();
    String coordinations;
    String coordinations2;

    boolean loadSave = false;

    String fromCoordinations;

    String fromCoordinations2;

    String encodedX;
    String encodedY;

    String encodedX2;
    String encodedY2;

    TextArea player1TextArea = new TextArea();
    TextArea player2TextArea = new TextArea();

    Color player1Color = MainController.setColor1();
    Color player2Color = MainController.setColor2();
    Color king1Color = MainController.setKing1();
    Color king2Color = MainController.setKing2();


    public void setColors(Color player1, Color player2, Color king1, Color king2) {

        //Króle--------------------------------------------------------------

        if (king1Color != null) {
            king1Color = king1;
            System.out.println("Kolor króla gracza 1:" + king1Color);
        } else {
            System.out.println("Kolor króla gracza 1 ustawiono na domyślny");
            king1Color = Color.BLUEVIOLET;
        }
        if (king2Color != null) {
            king2Color = king2;
            System.out.println("Kolor króla gracza 2:" + king2Color);
        } else {
            System.out.println("Kolor króla gracza 1 ustawiono na domyślny");
            king2Color = Color.CYAN;
        }

        //Kolory---------------------------------------------------------------------------

        if (player1 != null) {
            player1Color = player1;
            System.out.println("Kolor gracza 1:" + player1Color);
        } else {
            System.out.println("Kolor gracza 1 ustawiono na domyślny");
            player1Color = Color.GREY;
        }
        if (player2 != null) {
            player2Color = player2;
            System.out.println("Kolor gracza 2:" + player2Color);
        } else {
            System.out.println("Kolor gracza 2 ustawiono na domyślny");
            player2Color = Color.RED;
        }

    }

    private void writeToFile(String filename, String text) {
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

    void updateScene(Group root, Game game, Label chanceLabel, Label maxScoreLabel, Label minScoreLabel, TextArea player1TextArea, TextArea player2TextArea) {

        State state = game.getState();

        int sizeX, sizeY, sizeX2, sizeY2;
        sizeX = Xstack.size();
        sizeY = Ystack.size();
        sizeX2 = Xstack2.size();
        sizeY2 = Ystack2.size();
        int a = 0;

        for (int i = 0; i < state.getMinPieceList().size(); i++) {
            Circle circle = new Circle();

            if(loadSave){

                    String X = (String) Xstack.get(a);
                    String Y = (String) Ystack.get(a);


                    if (X != null) {

                        System.out.println(sizeX);
                        System.out.println(sizeY);
                        int intX = Integer.parseInt(X);
                        int intY = Integer.parseInt(Y);

                        circle.setCenterY(165 + intX * 90);
                        circle.setCenterX(165 + intY * 90);


                    }

            }

            else {

                circle.setCenterY(165 + state.getMinPieceList().get(i).getPosition().getxCoordinate() * 90);
                circle.setCenterX(165 + state.getMinPieceList().get(i).getPosition().getyCoordinate() * 90);
            }

            if (state.getMinPieceList().get(i).isKing()) {

                circle.setFill(king1Color);

            } else {
                circle.setFill(player1Color);
            }
            circle.setRadius(40);
            final Alert[] alert = new Alert[1];

            if (twoHuman) {
                circle.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                    clearScene(root);
                    updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel, player1TextArea, player2TextArea);
                    if (!game.getState().isMaxChance()) {
                        double dragBaseX = circle.getCenterX();
                        double dragBaseY = circle.getCenterY();
                        int y = (int) (dragBaseX - 140) / 90;
                        int x = (int) (dragBaseY - 140) / 90;

                        String X = String.valueOf(circle.getCenterX());
                        String Y = String.valueOf(circle.getCenterY());
                        fromCoordinations = cutXY(X,Y);

                        Piece p = state.getBoard().get(new Coordinate(x, y));

                        if (state.getStateActions().isEmpty()) System.out.println("Gracz2 wygrał");

                        List<Action> actions = new ArrayList<>();
                        if (state.getStateActions().containsKey(p.getPosition()))
                            actions = state.getStateActions().get(p.getPosition());
                        else {
                            alert[0] = new Alert(Alert.AlertType.INFORMATION);
                            alert[0].setTitle("Informacja");
                            alert[0].setHeaderText(null);
                            alert[0].setContentText("Figura nie moze wykonac tego ruchu \nlub:\n1. Jest zablokowana. albo\n2. Mozesz zbic innego gracza");
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
                                coordinations = cutXY(player1X, player1Y);
                                encodedX = encodeX(player1X);
                                encodedY = encodeY(player1Y);

                                System.out.println("Wykonano ruch " + coordinations);
                                writeToFile("player2.save", fromCoordinations + " --> " + coordinations);
                                writeToFile("save/player2X.save", encodedX);
                                writeToFile("save/player2Y.save", encodedY);



                                if (s.getStateActions().isEmpty()) {
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
                                updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel, player1TextArea, player2TextArea);
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
                    } else System.out.println("Tura gracza nr1");
                    mouseEvent.consume();
                });
            }
            circles.add(circle);
            root.getChildren().add(circle);
        }

        for (int i = 0; i < state.getMaxPieceList().size(); i++) {

            Circle circle = new Circle();

            if(loadSave){

                String X2 = (String) Xstack2.get(a);
                String Y2 = (String) Ystack2.get(a);


                if (X2 != null) {

                    System.out.println(sizeX);
                    System.out.println(sizeY);
                    int intX2 = Integer.parseInt(X2);
                    int intY2 = Integer.parseInt(Y2);

                    circle.setCenterY(165 + intX2 * 90);
                    circle.setCenterX(165 + intY2 * 90);


                }

            }

            else {
                circle.setCenterY(165 + state.getMaxPieceList().get(i).getPosition().getxCoordinate() * 90);
                circle.setCenterX(165 + state.getMaxPieceList().get(i).getPosition().getyCoordinate() * 90);
            }


            if (state.getMaxPieceList().get(i).isKing()) {
                circle.setFill(king2Color);

            } else {
                circle.setFill(player2Color);
            }
            circle.setRadius(40);
            circle.setFocusTraversable(true);

            final Alert[] alert = new Alert[1];
            if (oneHuman) {
                circle.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> {
                    clearScene(root);
                    updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel, player1TextArea, player2TextArea);
                    if (game.getState().isMaxChance()) {
                        double dragBaseX = circle.getCenterX();
                        double dragBaseY = circle.getCenterY();
                        int y = (int) (dragBaseX - 140) / 90;
                        int x = (int) (dragBaseY - 140) / 90;

                        String X2 = String.valueOf(circle.getCenterX());
                        String Y2 = String.valueOf(circle.getCenterY());
                        fromCoordinations2 = cutXY(X2,Y2);

                        Piece p = state.getBoard().get(new Coordinate(x, y));

                        if (p.isKing()) System.out.println("KRÓL");

                        List<Action> actions = new ArrayList<>();
                        if (state.getStateActions().containsKey(p.getPosition()))
                            actions = state.getStateActions().get(p.getPosition());
                        else {
                            alert[0] = new Alert(Alert.AlertType.INFORMATION);
                            alert[0].setTitle("Informacja");
                            alert[0].setHeaderText(null);
                            alert[0].setContentText("Pionek nie moze wykonac tego ruchu \nlub:\n1. Jest zablokowany albo\n2 mozesz zbic inny pionek");
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
                                coordinations2 = cutXY(player2X, player2Y);
                                encodedX2 = encodeX(player2X);
                                encodedY2 = encodeY(player2Y);


                                System.out.println("Wykonano ruch " + coordinations2);
                                writeToFile("player1.save", fromCoordinations2 + " --> " + coordinations2);
                                writeToFile("save/player1X.save", encodedX2);
                                writeToFile("save/player1Y.save", encodedY2);



                                if (s.getStateActions().isEmpty()) {
                                    Alert alert12 = new Alert(Alert.AlertType.INFORMATION);
                                    alert12.setTitle("Informacja");
                                    alert12.setHeaderText(null);
                                    alert12.setContentText("Gracz2 wygral!");
                                    alert12.showAndWait();
                                    Platform.exit();
                                    System.exit(0);
                                }

                                clearScene(root);
                                updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel, player1TextArea, player2TextArea);
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
                    } else System.out.println("Tura gracza nr2");
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

        readFile("player1.save", player1TextArea);
        readFile("player2.save", player2TextArea);
    }

    void readCoordinations(String filename, ArrayList stackname) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            stackname.add(line);

            while (line != null) {
                line = reader.readLine();
                stackname.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String cutXY(String X, String Y){
        String cutX = X.substring(0, X.length() - 4);
        String cutY = Y.substring(0, Y.length() - 4);

        String Xstr = String.valueOf(stack2.get(Integer.parseInt(cutY) - 1));
        String Ystr = String.valueOf(stack1.get(Integer.parseInt(cutX) - 1));

        String coordination = Xstr + " " + Ystr;

        return coordination;
    }

    String encodeX(String X){
        String cutX = X.substring(0, X.length() - 4);


        String coordination = cutX;

        return coordination;
    }

    String encodeY(String Y){
        String cutY = Y.substring(0, Y.length() - 4);

        String coordination = cutY;

        return coordination;
    }

    void readFile(String filename, TextArea textArea){
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Path.of(filename);
        try (Stream<String> stream
                     = Files.lines(Paths.get(String.valueOf(filePath)), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        textArea.setText(contentBuilder.toString());
    }


    boolean checkFileExists(String filename){
        boolean exists;
        File f = new File(filename);
        if (f.length() == 0) {
            System.out.println("Plik " + filename + " jest pusty, nie wyświetlam okna");
            exists = false;
        } else {
            System.out.println("Plik " + filename + " nie jest pusty, wyświetlam okno");
            exists = true;
        }
        return exists;
    }

    void clearFile(String filename) {
        try {
            new FileWriter(filename, false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        } else {
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
        final String[] arrayData = {"Człowiek vs AI", "Człowiek vs Człowiek", "AI vs AI",};
        List<String> dialogData = Arrays.asList(arrayData);
        ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("Wybierz tryb");
        dialog.setHeaderText("Wybierz tryb gry");
        Optional result = dialog.showAndWait();
        String selected = "anulowano.";
        if (result.isPresent()) {
            selected = (String) result.get();
        } else {
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


    private void closeWindowEvent(WindowEvent event) {
        System.out.println("Wykryto chęc zamknięcia okna");

        if( !(player1TextArea.getText().isEmpty() ) || !(player2TextArea.getText().isEmpty()) ){

            Alert alertClose = new Alert(Alert.AlertType.CONFIRMATION);
            alertClose.setHeaderText(null);
            alertClose.setTitle("Gra w trakcie");
            alertClose.setContentText("Czy chcesz zapisać stan gry?");
            alertClose.initModality(Modality.APPLICATION_MODAL);
            ((Button) alertClose.getDialogPane().lookupButton(ButtonType.OK)).setText("Tak");
            ((Button) alertClose.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Nie");
            Optional<ButtonType> resultClose = alertClose.showAndWait();

            if (resultClose.isPresent() && resultClose.get() == ButtonType.OK) {
                alertClose.close();
                Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
                confirmation.setHeaderText(null);
                confirmation.setTitle("Zapis stanu gry");
                confirmation.setContentText("Stan gry został zapisany");
                confirmation.show();


            } else if (resultClose.isPresent() && resultClose.get() == ButtonType.CANCEL) {

                clearFile("player1.save");
                clearFile("player2.save");

                clearFile("save/player1X.save");
                clearFile("save/player1Y.save");
                clearFile("save/player2X.save");
                clearFile("save/player2Y.save");
            }
        }

        else{
            System.out.println("Pola są puste");
        }
    }

    public void start() {

        Canvas canvas = new Canvas();
        Group root = new Group(canvas);
        Scene scene = new Scene(root, 1400, 1000);

        for (String s : Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H")) { //OŚ X
            stack1.push(s);
        }
        for (String s : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8")) { //OŚ Y
            stack2.push(s);
        }

        boolean file1Exists = checkFileExists("player1.save");
        boolean file2Exists = checkFileExists("player2.save");

        if(file1Exists && file2Exists){

            Alert alertClose = new Alert(Alert.AlertType.CONFIRMATION);
            alertClose.setHeaderText(null);
            alertClose.setTitle("Wczytywanie stanu gry");
            alertClose.setContentText("Istnieje zapis gry. chcesz go wczytać?");
            alertClose.initModality(Modality.APPLICATION_MODAL);
            ((Button) alertClose.getDialogPane().lookupButton(ButtonType.OK)).setText("Tak");
            ((Button) alertClose.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Nie");
            Optional<ButtonType> resultClose = alertClose.showAndWait();

            if (resultClose.isPresent() && resultClose.get() == ButtonType.OK) {
                alertClose.close();
                loadSave = true;
                readCoordinations("save/player1X.save", Xstack);
                readCoordinations("save/player1y.save", Ystack);
                readCoordinations("save/player2X.save", Xstack2);
                readCoordinations("save/player2Y.save", Ystack2);

            } else if (resultClose.isPresent() && resultClose.get() == ButtonType.CANCEL) {
                clearFile("player1.save");
                clearFile("player2.save");

                clearFile("save/player1X.save");
                clearFile("save/player1Y.save");
                clearFile("save/player2X.save");
                clearFile("save/player2Y.save");

            }

        }

        drawSquares(scene, root);

    }

    void drawSquares(Scene scene, Group root){
        //Making the board. Stacking the squares.
        boolean red = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rectangle = new Rectangle(120 + 90 * i, 120 + 90 * j, 90, 90);
                if (red) {
                    rectangle.setFill(Color.web("#ECDBD3"));
                } else {
                    rectangle.setFill(Color.web("#43251A"));
                }
                red = !red;
                root.getChildren().add(rectangle);
            }
            red = !red;
        }
        int x = 65;
        Text text;
        for (int t = 0; t < 8; t++) {
            x = x + 90;
            text = new Text((String) stack1.get(t));
            text.setFont(Font.font(45));
            text.setX(x);
            text.setY(90);
            root.getChildren().add(text);
        }

        int y = 90;
        Text text2;
        for (int t = 0; t < 8; t++) {
            y = y + 90;
            text2 = new Text((String) stack2.get(t));
            text2.setFont(Font.font(45));
            text2.setX(50);
            text2.setY(y);
            root.getChildren().add(text2);
        }

        Label player1Moves = new Label("Ruchy gracza 1");
        player1Moves.setFont(Font.font(15));
        player1Moves.setLayoutX(920);
        player1Moves.setLayoutY(120);
        root.getChildren().add(player1Moves);


        player1TextArea.setPromptText("Tutaj pojawią się ruchy gracza 1");
        player1TextArea.setEditable(false);
        player1TextArea.setMinWidth(200);
        player1TextArea.setMaxWidth(200);
        player1TextArea.setMinHeight(600);
        player1TextArea.setLayoutX(870);
        player1TextArea.setLayoutY(150);
        player1TextArea.setEditable(false);
        root.getChildren().add(player1TextArea);


        Label player2Moves = new Label("Ruchy gracza 2");
        player2Moves.setFont(Font.font(15));
        player2Moves.setLayoutX(1200);
        player2Moves.setLayoutY(120);
        root.getChildren().add(player2Moves);


        player2TextArea.setPromptText("Tutaj pojawią się ruchy gracza 2");
        player1TextArea.setEditable(false);
        player2TextArea.setMinWidth(200);
        player2TextArea.setMaxWidth(200);
        player2TextArea.setMinHeight(600);
        player2TextArea.setLayoutX(1150);
        player2TextArea.setLayoutY(150);
        player2TextArea.setEditable(false);
        root.getChildren().add(player2TextArea);

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
        updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel, player1TextArea, player2TextArea);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Warcaby!");
        primaryStage.getIcons().add(new Image("file:src/main/resources/com/checkers/warcaby/icon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        scene.setOnMousePressed(mouseEvent -> {
            if (!game.hasFinished()) {
                game.playNextMove(oneHuman, twoHuman, depth);
                clearScene(root);
                updateScene(root, game, chanceLabel, maxScoreLabel, minScoreLabel, player1TextArea, player2TextArea);
            }
        });

        String selected = chooseGameMode();

        switch (selected) {
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
