package com.example.fitnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.chart.PieChart.Data;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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

    private int zielKalorien = ConectedPerson.INSTANCE.getCaloriesGoal(); // das kommt dann vom alex / bzw. aus der rechnung die am angang gemacht wird
    public int erreichteKalorien;
    public int verbrannteKalorien;

    @FXML
    public void initialize() {
        // auf standard Werte setzen
        pieChart.getData().addAll(
                new Data("Erreichte Kalorien", erreichteKalorien),
                new Data("Verbrannte Kalorien", verbrannteKalorien),
                new Data("Übrige Kalorien", zielKalorien - erreichteKalorien - verbrannteKalorien)
        );

        zielKalorienText.setText("Ziel Kalorien: " + zielKalorien);
        erreichteKalorienText.setText("Kalorien die man schon erreicht hat: " + erreichteKalorien);
        verbrannteKalorienText.setText("Verbrannte Kalorien: " + verbrannteKalorien);
    }

    @FXML
    private void handleTrackCaloriesButton(ActionEvent event) {
        try {
            // Load the Calorie Tracker Page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fitnesstracker/KalorienTrackenPage.fxml"));
            Parent root = loader.load();

            // Show the Calorie Tracker Page in the same window
            Stage stage = (Stage) trackCaloriesButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Calorie Tracker Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Pie chart updaten
    public void updatePieChart(int erreichteKalorien, int verbrannteKalorien) {
        pieChart.getData().clear();
        pieChart.getData().addAll(
                new Data("Erreichte Kalorien", erreichteKalorien),
                new Data("Verbrannte Kalorien", verbrannteKalorien),
                new Data("Übrige Kalorien", zielKalorien - erreichteKalorien - verbrannteKalorien)
        );

        erreichteKalorienText.setText("Kalorien die man schon erreicht hat: " + erreichteKalorien);
        verbrannteKalorienText.setText("Verbrannte Kalorien: " + verbrannteKalorien);
    }
}
