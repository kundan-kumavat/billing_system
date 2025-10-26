
package com.cwk.ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ReportsController {
    public void goBack(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
    }
}
