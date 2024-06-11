module com.example.fitnesstracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.json;


    opens com.example.fitnesstracker to javafx.fxml;
    exports com.example.fitnesstracker;
}