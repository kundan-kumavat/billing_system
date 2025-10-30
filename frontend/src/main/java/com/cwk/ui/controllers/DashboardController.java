package com.cwk.ui.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class DashboardController {

    @FXML
    private AnchorPane mainContent; // This is your placeholder in the FXML

    @FXML
    private void openBilling(ActionEvent event) {
        loadModule("/fxml/invoice-view.fxml");
    }

    @FXML
    private void openInventory(ActionEvent event) {
        loadModule("/fxml/inventory.fxml");
    }

    @FXML
    private void openReports(ActionEvent event) {
        loadModule("/fxml/reports.fxml");
    }

    // Generic loader method
    private void loadModule(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            mainContent.getChildren().clear();  // Clear previous module
            mainContent.getChildren().add(root); // Add new module

            // Optional: make the loaded module fill the AnchorPane
            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
