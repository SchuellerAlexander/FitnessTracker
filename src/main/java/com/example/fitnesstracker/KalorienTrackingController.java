package com.example.fitnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
            int kalorien = Integer.parseInt(kalorienTextField.getText());
            int proteine = Integer.parseInt(proteineTextField.getText());
            int fett = Integer.parseInt(fettTextField.getText());

            mainPageController.updatePieChart(
                    mainPageController.erreichteKalorien + kalorien,
                    mainPageController.verbrannteKalorien
            );
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearchButton(ActionEvent event) {
        String query = essenTextField.getText();
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
                    int calories = food.getInt("calories");
                    int proteins = food.getInt("protein");
                    int fats = food.getInt("fat");

                    kalorienTextField.setText(String.valueOf(calories));
                    proteineTextField.setText(String.valueOf(proteins));
                    fettTextField.setText(String.valueOf(fats));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
