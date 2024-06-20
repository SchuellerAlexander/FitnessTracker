package com.example.fitnesstracker;

import com.example.fitnesstracker.models.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

public class KalorienTrackingController {

    @FXML
    private Text pageTitle;

    @FXML
    private ComboBox<String> tagesZeitComboBox;

    @FXML
    private TextField essenTextField;

    @FXML
    private TextField kalorienTextField;

    @FXML
    private TextField proteineTextField;

    @FXML
    private TextField fettTextField;

    @FXML
    private Button trackenButton;

    @FXML
    private Button searchButton;

    private Mainpagecontroller mainPageController;

    @FXML
    public void initialize() {
        tagesZeitComboBox.getItems().addAll("Frühstück", "Snacks", "Mittagessen", "Abendessen");
    }

    public void setMainPageController(Mainpagecontroller controller) {
        this.mainPageController = controller;
    }

    @FXML
    private void handleTrackenButton(ActionEvent event) {
        try {
            String tagesZeit = tagesZeitComboBox.getValue();
            String essen = essenTextField.getText();
            if (kalorienTextField.getText().isEmpty() || proteineTextField.getText().isEmpty() || fettTextField.getText().isEmpty()) {
                System.out.println("Bitte füllen Sie alle Felder aus, bevor Sie auf 'Tracken' klicken.");
                return;
            }
            int kalorien = Integer.parseInt(kalorienTextField.getText());
            int proteine = Integer.parseInt(proteineTextField.getText());
            int fett = Integer.parseInt(fettTextField.getText());

            mainPageController.updatePieChart(
                    mainPageController.erreichteKalorien + kalorien,
                    mainPageController.verbrannteKalorien
            );

            switchToMainPage();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchButton(ActionEvent event) {
        String query = essenTextField.getText();
        if (query.isEmpty()) {
            System.out.println("Bitte geben Sie ein Nahrungsmittel ein, bevor Sie auf 'Suchen' klicken.");
            return;
        }
        String apiKey = "bveGGB5WHVxfqjQrY7qh1Dn126Itm9UEffqcKeG6";
        String urlString = "https://api.nal.usda.gov/fdc/v1/foods/search?query=" + query + "&api_key=" + apiKey;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();

                JSONObject jsonResponse = new JSONObject(inline);
                JSONArray foods = jsonResponse.getJSONArray("foods");

                if (foods.length() > 0) {
                    JSONObject food = foods.getJSONObject(0);
                    JSONArray foodNutrients = food.getJSONArray("foodNutrients");

                    int calories = getNutrientValue(foodNutrients, 1008); // 1008 is the nutrient ID for calories
                    int proteins = getNutrientValue(foodNutrients, 1003); // 1003 is the nutrient ID for proteins
                    int fats = getNutrientValue(foodNutrients, 1004); // 1004 is the nutrient ID for fats
                    ConectedPerson.INSTANCE.setCaloriesEatenToday(ConectedPerson.INSTANCE.getCaloriesEatenToday() + calories);
                    kalorienTextField.setText(String.valueOf(calories));
                    proteineTextField.setText(String.valueOf(proteins));
                    fettTextField.setText(String.valueOf(fats));
                } else {
                    System.out.println("Keine Nährwertinformationen für das eingegebene Nahrungsmittel gefunden.");
                }
            } else {
                System.out.println("Fehler bei der Verbindung zur API: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNutrientValue(JSONArray foodNutrients, int nutrientId) {
        for (int i = 0; i < foodNutrients.length(); i++) {
            JSONObject nutrient = foodNutrients.getJSONObject(i);
            if (nutrient.getInt("nutrientId") == nutrientId) {
                return nutrient.getInt("value");
            }
        }
        return 0; // default value if the nutrient is not found
    }

    private void switchToMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/fitnesstracker/mainpage.fxml"));
            Parent root = loader.load();

            // Ensure the main page controller is passed correctly
            Mainpagecontroller mainPageController = loader.getController();
            mainPageController.initialize();

            // Show the main page in the same window
            Stage stage = (Stage) trackenButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Page");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
