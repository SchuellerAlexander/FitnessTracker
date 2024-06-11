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

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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

    @FXML
    private void register() {
        ConnectDB db = new ConnectDB();
        Connection connection = db.getConnection();
        if (connection != null) {

            try {

                Statement statement = connection.createStatement();
                String createTable = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY, password Integer," +
                        " name TEXT, weight Double, height Double, caloriesGoal Integer, caloriesEatenToday Integer, " +
                        "stepsGoal Integer, stepsToday Integer)";
                statement.executeUpdate(createTable);

                ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) AS user_count FROM users;");
                int userCount = resultSet.getInt("user_count");
                for (int i = 0; i < userCount; i++) {
                    // überprüfen ob der username an der Stelle i == dem LabelUsername ist
                    if(InputUsername.toString().equals(String.valueOf(statement.executeQuery("Select * from users where id = " + i)))){
                        if(InputPassword.getText().toString().equals(String.valueOf(statement.executeQuery("Select * from users where id = " + i)))){
                            LabelInformation.setText("User already exists");
                        }
                        LabelInformation.setText("Username already exists");
                    }
                }
                // aufrufen von inputHeightWeight




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
                String createTable = "CREATE TABLE IF NOT EXISTS user (id INTEGER PRIMARY KEY, password Integer," +
                        " name TEXT, weight Double, height Double, caloriesGoal Integer, caloriesEatenToday Integer, " +
                        "stepsGoal Integer, stepsToday Integer)";
                statement.executeUpdate(createTable);


                ResultSet resultSet = statement.executeQuery("SELECT COUNT(id) AS user_count FROM users;");
                boolean userFound = false;
                int userCount = resultSet.getInt("user_count");
                for (int i = 0; i < userCount; i++) {
                    // ueberprueft ob der username an der Stelle i == dem LabelUsername ist
                    if(InputUsername.toString().equals(String.valueOf(statement.executeQuery("Select * from users where id = " + i)))){
                        if(InputPassword.getText().toString().equals(String.valueOf(statement.executeQuery("Select * from users where id = " + i)))){
                            openMainPage();
                        }
                        LabelInformation.setText("Password incorrect");
                    }
                }

                LabelInformation.setText("User not found");


            } catch (SQLException e) {

                e.printStackTrace();

            } finally {

                db.closeConnection();

            }

        } else {

            System.out.println("Connection failed.");


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

}
