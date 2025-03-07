package org.example.card_game24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.net.URL;
import java.util.*;

public class CardGame24Controller {
    @FXML
    private TextField expression_textfield;
    @FXML
    private Button refresh_button;
    @FXML
    private Button hint_button;
    @FXML
    private TextField solution_textfield;
    @FXML
    private Button verify_button;
    @FXML
    private ImageView CardSlot_1;
    @FXML
    private ImageView CardSlot_2;
    @FXML
    private ImageView CardSlot_3;
    @FXML
    private ImageView CardSlot_4;

    // initializing variables
    private List<Integer> CardValues = new ArrayList<>();
    private final Random random = new Random();
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    // Initialization on start - want to start with different fresh new deck every time
    public void initialize() {
        shuffleDeck();
    }

    // method to shuffle the deck
    public void shuffleDeck() {
        CardValues.clear();
        for (int i = 0; i < 4; i++) {
            CardValues.add(random.nextInt(13) + 1);
        }
        updateCardImage();
        if ((CardSlot_1.getImage().equals(CardSlot_2.getImage()) ||
                CardSlot_1.getImage().equals(CardSlot_3.getImage()) ||
                CardSlot_1.getImage().equals(CardSlot_4.getImage())  ||
                CardSlot_2.getImage().equals(CardSlot_3.getImage()) ||
                CardSlot_1.getImage().equals(CardSlot_4.getImage())) ||
                CardSlot_3.getImage().equals(CardSlot_4.getImage())
        ) {
            shuffleDeck();
        }
    }

    // method to match card image/type to actual png file name
    private Image getCardImage(Integer value) {
        String CardName = "";
        String cardType ;

        if (value == 1) {
            cardType = "ace";
        } else if (value >= 2 && value <= 10) {
            cardType = String.valueOf(value); // Convert number to string for cards
        } else if (value == 11) {
            cardType = "jack";
        } else if (value == 12) {
            cardType = "queen";
        } else if (value == 13) {
            cardType = "king";
        } else {
            throw new IllegalArgumentException("Invalid card value: " + value);
        }

        // Assign (clubs, diamonds, hearts, spades)
        String[] types = {"clubs", "diamonds", "hearts", "spades"};
        for (String type : types) {
            CardName = cardType + "_of_" + type;
            URL imageURL = getClass().getResource("/png/" + CardName + ".png");

            if (imageURL != null) { // If the image exists, return it
                return new Image(imageURL.toExternalForm());
            }
        }

        // If no image was found for any suit, throw an error
        throw new IllegalArgumentException("Image not found for value: " + value);
    }


    private void updateCardImage() {
        CardSlot_1.setImage(getCardImage(CardValues.get(0)));
        CardSlot_2.setImage(getCardImage(CardValues.get(1)));
        CardSlot_3.setImage(getCardImage(CardValues.get(2)));
        CardSlot_4.setImage(getCardImage(CardValues.get(3)));
    }





    public void hint_solution(ActionEvent actionEvent) {
    }

    public void verify_expression(ActionEvent actionEvent) {
    }




    public void refresh_all(ActionEvent actionEvent) {
        shuffleDeck();
        expression_textfield.clear();
        solution_textfield.clear();
    }


}   // End Controller
















