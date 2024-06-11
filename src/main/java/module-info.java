module com.example.fitnesstracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.example.fitnesstracker to javafx.fxml;
    exports com.example.fitnesstracker;
}