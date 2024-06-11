package com.example.fitnesstracker;

import com.example.fitnesstracker.models.ConnectDB;
import javafx.fxml.FXML;

import java.awt.*;
import java.lang.classfile.Label;
import java.sql.Connection;
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

    }

    @FXML
    private void login() {
        ConnectDB db = new ConnectDB();
        Connection connection = db.getConnection();
        Statement statement = connection.createStatement();


    }
}
