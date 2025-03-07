package org.example.card_game24;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CardGame24Application extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CardGame24Application.class.getResource("Card-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 275);
        stage.setTitle("Card Game 24!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}