module com.example.proudctsystemfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.proudctsystemfx to javafx.fxml;
    exports com.example.proudctsystemfx;
}