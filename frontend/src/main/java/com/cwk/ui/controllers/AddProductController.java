package com.cwk.ui.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.cwk.ui.models.ProductModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddProductController {

    @FXML private TextField nameField;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private TextField gstField;
    @FXML private TextField stockField;

    private InventoryController inventoryController;

    private HttpClient client = HttpClient.newHttpClient();
    private ObjectMapper mapper = new ObjectMapper();

    public void setInventoryController(InventoryController controller) {
        this.inventoryController = controller;
    }

    @FXML
    public void saveProduct() {
        try {
            ProductModel product = new ProductModel();
            product.setName(nameField.getText());
            product.setCategory(categoryField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setGstRate(Double.parseDouble(gstField.getText()));
            product.setStock(Integer.parseInt(stockField.getText()));

            // Add to TableView
            inventoryController.getProductTable().getItems().add(product);

            // Optional: send to backend
            String json = mapper.writeValueAsString(product);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/products"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println("Backend response: " + resp.statusCode() + " -> " + resp.body());

            // Close window
            ((Stage) nameField.getScene().getWindow()).close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void cancel() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}
