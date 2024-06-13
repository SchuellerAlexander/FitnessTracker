package com.example.fitnesstracker;

import com.example.fitnesstracker.models.ConnectDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class InputHeightWeightController {
    @FXML
    private TextField weightTextField;
    @FXML
    private TextField heightTextField;

    private final ConnectDB db = new ConnectDB();

    @FXML
    public void save() {
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT, password TEXT, name TEXT, weight Double, height Double, caloriesGoal Integer, caloriesEaten Today Integer, stepsGoal Integer, stepsToday Integer)";
                statement.executeUpdate(createTable);
                String insertSql = "INSERT INTO users(username, password, weight, height) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(insertSql);
                pstmt.setString(1, RegistrationController.getInputUsername());
                pstmt.setString(2, RegistrationController.getInputPassword());
                pstmt.setDouble(3, Double.parseDouble(weightTextField.getText()));
                pstmt.setDouble(4, Double.parseDouble(heightTextField.getText()));
                pstmt.executeUpdate();
                openMainPage();
            } catch (SQLException e) {
                System.out.println("Database error occurred.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection failed.");
        }
    }

    @FXML
    public void openMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Page");
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to load the main page.");
            e.printStackTrace();
        }
    }
}
