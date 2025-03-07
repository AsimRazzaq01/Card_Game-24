module org.example.card_game24 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.scripting;


    opens org.example.card_game24 to javafx.fxml;
    exports org.example.card_game24;
}