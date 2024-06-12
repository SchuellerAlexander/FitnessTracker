package com.example.fitnesstracker;

import com.example.fitnesstracker.models.ConnectDB;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.*;

public class InputHeightWeightController{
    @FXML
    private TextField weightTextField;
    @FXML
    private TextField heightTextField;

    private ConnectDB db = new ConnectDB();

    @FXML
    public void save() {
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT, password TEXT, name TEXT, weight Double, height Double, caloriesGoal Integer, caloriesEatenToday Integer, stepsGoal Integer, stepsToday Integer)";
                statement.executeUpdate(createTable);
                String insertSql = "INSERT INTO users(username, password, weight, height) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = connection.prepareStatement(insertSql);
                pstmt.setString(1, RegistrationController.getInputUsername());
                pstmt.setString(2, RegistrationController.getInputPassword());
                pstmt.setDouble(3, Double.parseDouble(weightTextField.getText()));
                pstmt.setDouble(4, Double.parseDouble(heightTextField.getText()));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Database error occurred.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Connection failed.");
        }
    }
}
