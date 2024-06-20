package com.example.fitnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Mainpagecontroller {
    @FXML
    private PieChart pieChart;

    @FXML
    private Text zielKalorienText;

    @FXML
    private Text erreichteKalorienText;

    @FXML
    private Text verbrannteKalorienText;

    @FXML
    private TextField schritteTextField;

    @FXML
    private Button trackCaloriesButton;

    private final int zielKalorien = ConectedPerson.INSTANCE.getCaloriesGoal();
    public int erreichteKalorien = ConectedPerson.INSTANCE.getCaloriesEatenToday();
    public int verbrannteKalorien = (int) ((double) ConectedPerson.INSTANCE.getStepsToday() * 0.2);

    @FXML
    public void initialize() throws ParseException {
        try {
            ZonedDateTime now = ZonedDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedNow = now.format(formatter);
            Date lastLoginDate = ConectedPerson.INSTANCE.getLastLogin();
            String formattedLastLogin = new SimpleDateFormat("yyyy-MM-dd").format(lastLoginDate);

            if (!formattedNow.equals(formattedLastLogin)) {
                ConectedPerson.INSTANCE.setCaloriesEatenToday(0);
                ConectedPerson.INSTANCE.setStepsToday(0);
            }
            ConectedPerson.INSTANCE.setLastLogin(new java.sql.Date(Date.from(now.toInstant()).getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println("Initializing Mainpagecontroller...");

        updatePieChart(erreichteKalorien, verbrannteKalorien);

        if (zielKalorienText == null) {
            System.out.println("zielKalorienText is null!");
        } else {
            zielKalorienText.setText("Ziel Kalorien: " + zielKalorien);
        }

        if (erreichteKalorienText == null) {
            System.out.println("erreichteKalorienText is null!");
        } else {
            erreichteKalorienText.setText("Kalorien die man schon erreicht hat: " + erreichteKalorien);
        }

        if (verbrannteKalorienText == null) {
            System.out.println("verbrannteKalorienText is null!");
        } else {
            verbrannteKalorienText.setText("Verbrannte Kalorien: " + verbrannteKalorien);
        }
    }

    public void updatePieChart(int erreichteKalorien, int verbrannteKalorien) {
        if (pieChart == null) {
            System.out.println("pieChart is null in updatePieChart!");
        } else {
            int uebrigeKalorien = (zielKalorien + verbrannteKalorien) - erreichteKalorien;
            pieChart.getData().clear();
            pieChart.getData().addAll(
                    new PieChart.Data("Erreichte Kalorien", erreichteKalorien),
                    new PieChart.Data("Übrige Kalorien", uebrigeKalorien)
            );
        }

        if (erreichteKalorienText != null) {
            erreichteKalorienText.setText("Kalorien die man schon erreicht hat: " + erreichteKalorien);
        }

        if (verbrannteKalorienText != null) {
            verbrannteKalorienText.setText("Verbrannte Kalorien: " + verbrannteKalorien);
        }
    }

    @FXML
    private void acceptSteps() {
        ConectedPerson.INSTANCE.setStepsToday(Integer.parseInt(schritteTextField.getText()));
        verbrannteKalorien = (int) ((double) ConectedPerson.INSTANCE.getStepsToday() * 0.2);
        updatePieChart(erreichteKalorien, verbrannteKalorien);
    }

    @FXML
    private void handleTrackCaloriesButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fitnesstracker/KalorienTrackenPage.fxml"));
            Parent root = loader.load();

            // Pass the main page controller to the calorie tracking controller
            KalorienTrackingController kalorienTrackingController = loader.getController();
            kalorienTrackingController.setMainPageController(this);

            // Show the calorie tracking page in the same window
            Stage stage = (Stage) trackCaloriesButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Calorie Tracking Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
