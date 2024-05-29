package com.example.fitnesstracker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

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

    private Mainpagecontroller mainPageController;

    @FXML
    public void initialize() {
        tagesZeitComboBox.getItems().addAll("Frühstück", "Snacks", "Mittagessen", "Abendessen");
    }

    public void setMainPageController(Mainpagecontroller controller) {
        this.mainPageController = controller;
    }

    @FXML
    private void handleTrackenButton(ActionEvent event) { // trackt die ganzen sachen / füllt die daten dann in den main page controller
        try {
            // Extract the entered data
            String tagesZeit = tagesZeitComboBox.getValue();
            String essen = essenTextField.getText();
            int kalorien = Integer.parseInt(kalorienTextField.getText());
            int proteine = Integer.parseInt(proteineTextField.getText());
            int fett = Integer.parseInt(fettTextField.getText());

            // Update the main page pie chart
            mainPageController.updatePieChart(
                    mainPageController.erreichteKalorien + kalorien,
                    mainPageController.verbrannteKalorien
            );
        } catch (NumberFormatException e) {
            // theoretisches fehler handeling
            e.printStackTrace();
        }
    }
}

