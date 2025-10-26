
package com.cwk.ui.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class LoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;

    public void handleLogin(ActionEvent event) {
        // Simple local check for POC. For production, call backend auth API and store JWT.
        if (username.getText().equals("admin") && password.getText().equals("1234")) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
                Stage stage = (Stage) username.getScene().getWindow();
                stage.setScene(new Scene(root, 900, 600));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
