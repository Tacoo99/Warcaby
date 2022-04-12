package com.checkers.warcaby;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Player2Info {

    @FXML
    private TextArea player2TextField;

    public void readFromFile() {
        try {
            File myObj = new File("player2.save");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                player2TextField.appendText(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Wystąpił błąd");
            e.printStackTrace();
        }
    }
}
