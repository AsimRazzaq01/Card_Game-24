package org.example.card_game24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    /**
     * initializing variables
     */
    private List<Integer> CardValues = new ArrayList<>();
    private final Random random = new Random();
    private final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    /**
     * Initialization on start - want to start with different fresh new deck every time
     */
    public void initialize() {
        shuffleDeck();
    }

    /**
     * method to shuffle the deck
     */
    public void shuffleDeck() {
        // clear values
        CardValues.clear();
        // loop through 4 cards and add random value
        for (int i = 0; i < 4; i++) {
            CardValues.add(random.nextInt(13) + 1);
        }
        // call update image method
        updateCardImage();
        // check for collisions
        if (CardSlot_1.getImage().getUrl().equals(CardSlot_2.getImage().getUrl()) ||
                CardSlot_1.getImage().getUrl().equals(CardSlot_3.getImage().getUrl()) ||
                CardSlot_1.getImage().getUrl().equals(CardSlot_4.getImage().getUrl()) ||
                CardSlot_2.getImage().getUrl().equals(CardSlot_3.getImage().getUrl()) ||
                CardSlot_1.getImage().getUrl().equals(CardSlot_4.getImage().getUrl()) ||
                CardSlot_3.getImage().getUrl().equals(CardSlot_4.getImage().getUrl())
        ) {
            shuffleDeck();
        }
        //System.out.println(CardValues + "\n " + card1Value + "\n" + card2Value + "\n" + card3Value + "\n" + card4Value);
    }

    /**
     * method to match card image/type to actual png file name
     * @param value
     * @return the correct png image based on the value and type
     */
    private Image getCardImage(Integer value) {
        // initialize variables
        String CardName = "";
        String cardType;

        // if statements to set the face with the value
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
        // If no image was found for any card, throw an error
        throw new IllegalArgumentException("Image not found for value: " + value);
    }


    /**
     * Method to update the card image values
     */
    private void updateCardImage() {
        CardSlot_1.setImage(getCardImage(CardValues.get(0)));
        CardSlot_2.setImage(getCardImage(CardValues.get(1)));
        CardSlot_3.setImage(getCardImage(CardValues.get(2)));
        CardSlot_4.setImage(getCardImage(CardValues.get(3)));
    }

    /**
     * Open AI-GPT method
     * @param message
     * @return
     */
    public static String chatGPT(String message) {
        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "AI-KEY-GOES-HERE";
        String model = "gpt-3.5-turbo";

        try {
            // Create the HTTP POST request
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Correct JSON request body
            String body = "{"
                    + "\"model\": \"" + model + "\","
                    + "\"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}],"
                    + "\"max_tokens\": 100"
                    + "}";

            // Send request
            try (OutputStream os = connection.getOutputStream()) {
                byte[] inputBytes = body.getBytes(StandardCharsets.UTF_8);
                os.write(inputBytes, 0, inputBytes.length);
            }

            // Get response
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Error: Received HTTP response code " + responseCode);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Extract response content
            String responseString = response.toString();
            return extractMessage(responseString);

        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * helper method for gpt string extraction
     * @param jsonResponse
     * @return
     */
    private static String extractMessage(String jsonResponse) {
        int start = jsonResponse.indexOf("\"content\":\"");
        if (start == -1) {
            return "Error: Could not extract response.";
        }
        start += 10; // Move past "content":"
        int end = jsonResponse.indexOf("\"", start);
        return jsonResponse.substring(start, end);
    }


    /**
     * Hint button action -> pop out -> gpt answer
     * @param actionEvent
     */
    public void hint_solution(ActionEvent actionEvent) {
        // get the question needed based on the numbers
        String Question = "Based on the numbers :  "+ getCardImage(CardValues.get(0)) +"\n"
                + getCardImage(CardValues.get(1)) +"\n" + getCardImage(CardValues.get(2)) +"\n"
                + getCardImage(CardValues.get(3)) +"\n" + " we want to use the values of the cards to " +
                "get a total value of 24 by adding, subtracting, multiplying, or dividing the the 4 " +
                "above values.";

        // return gpt response
        String response = chatGPT(Question);
        String AI_Solution ;
        AI_Solution = response ;

        // alert feature
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText("Here's a Hint : ");          //
        alert.setContentText(AI_Solution);  // will pass the solution into text
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.out.println("Hint successful");
        }
    }

    public void verify_expression(ActionEvent actionEvent) {
    }


    /**
     * refresh method
     * @param actionEvent
     */
    public void refresh_all(ActionEvent actionEvent) {
        shuffleDeck();
        expression_textfield.clear();
        solution_textfield.clear();
    }


}   // End Controller
















