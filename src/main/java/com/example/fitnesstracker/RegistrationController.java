package com.example.fitnesstracker;

import com.example.fitnesstracker.models.ConnectDB;
import javafx.fxml.FXML;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.*;


public class RegistrationController {
    @FXML
    private Label LabelUsername;
    @FXML
    private Label LabelEMail;
    @FXML
    private Label LabelInformation;
    @FXML
    private TextField InputUsername;
    @FXML
    private TextField InputPassword;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button LoginButton;

    private static String username;
    private static String password;

    @FXML
    private void register() {
        ConnectDB db = new ConnectDB();
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                // Ensure the table name is consistent
                String createTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT, password TEXT," +
                        " name TEXT, weight Double, height Double, caloriesGoal Integer, caloriesEatenToday Integer, " +
                        "stepsGoal Integer, stepsToday Integer)";
                statement.executeUpdate(createTable);

                // Efficiently check if user exists
                String query = "SELECT username FROM users WHERE username = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, InputUsername.getText());
                ResultSet resultSet = pstmt.executeQuery();
                if (resultSet.next()) {
                    LabelInformation.setText("Username already exists");
                } else {
                    // Insert new user logic here
                    openInputHeightWeight();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                db.closeConnection();
            }
        } else {
            System.out.println("Connection failed.");
        }
    }

    @FXML
    private void login() {
        ConnectDB db = new ConnectDB();
        Connection connection = db.getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                // Ensure the table name is consistent and the table exists
                String createTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT, password TEXT," +
                        " name TEXT, weight Double, height Double, caloriesGoal Integer, caloriesEatenToday Integer, " +
                        "stepsGoal Integer, stepsToday Integer)";
                statement.executeUpdate(createTable);

                // Use a PreparedStatement to securely check user credentials
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, InputUsername.getText());
                pstmt.setString(2, InputPassword.getText());
                ResultSet resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    // Credentials are correct, open main page
                    username = InputUsername.toString();
                    password = InputPassword.toString();
                    openMainPage();
                } else {
                    LabelInformation.setStyle("-fx-text-fill: red;");
                    LabelInformation.setText("User not found or password incorrect");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LabelInformation.setText("Database error occurred.");
            } finally {
                db.closeConnection();
            }
        } else {
            System.out.println("Connection failed.");
            LabelInformation.setText("Failed to connect to the database.");
        }
    }


    private void openMainPage() {
        try {
            // Laden der FXML-Datei für die Hauptseite
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent root = loader.load();

            // Holen der aktuellen Stage über ein beliebiges Steuerelement (z.B. einen Button)
            Stage stage = (Stage) RegisterButton.getScene().getWindow();

            // Festlegen der Szene für die Stage
            stage.setScene(new Scene(root, 800, 600));  // Breite und Höhe der neuen Szene anpassen
            stage.setTitle("MainPage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            LabelInformation.setText("Fehler beim Laden der Hauptseite.");
        }
    }

    private void openInputHeightWeight() {
        try {
            // Laden der FXML-Datei für die Hauptseite
            FXMLLoader loader = new FXMLLoader(getClass().getResource("inputHeightWeight.fxml"));
            Parent root = loader.load();

            // Holen der aktuellen Stage über ein beliebiges Steuerelement (z.B. einen Button)
            Stage stage = (Stage) RegisterButton.getScene().getWindow();

            // Festlegen der Szene für die Stage
            stage.setScene(new Scene(root, 800, 600));  // Breite und Höhe der neuen Szene anpassen
            stage.setTitle("Data");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            LabelInformation.setText("Fehler beim Laden der InputPage .");
        }
    }

    public static String getInputUsername() {
        return username;
    }

    public static String getInputPassword() {
        return password;
    }
}
