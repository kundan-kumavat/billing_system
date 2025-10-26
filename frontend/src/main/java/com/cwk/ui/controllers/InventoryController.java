package com.cwk.ui.controllers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.cwk.ui.models.ProductModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class InventoryController {

    @FXML private TableView<ProductModel> productTable;
    @FXML private TableColumn<ProductModel, Long> idCol;
    @FXML private TableColumn<ProductModel, String> nameCol;
    @FXML private TableColumn<ProductModel, String> categoryCol;
    @FXML private TableColumn<ProductModel, Double> priceCol;
    @FXML private TableColumn<ProductModel, Double> gstCol;
    @FXML private TableColumn<ProductModel, Integer> stockCol;

    private ObjectMapper mapper = new ObjectMapper();
    private HttpClient client = HttpClient.newHttpClient();


    public void initialize() {
        loadProducts();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        gstCol.setCellValueFactory(new PropertyValueFactory<>("gstRate"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void loadProducts() {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/products"))
                    .GET()
                    .build();
            HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            if(resp.statusCode() == 200) {
                List<ProductModel> products = mapper.readValue(resp.body(), new TypeReference<List<ProductModel>>(){});
                productTable.setItems(FXCollections.observableArrayList(products));
            } else {
                System.out.println("Failed to fetch products: " + resp.statusCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void addSampleProduct(ActionEvent e) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_product.fxml"));
        Parent root = loader.load();

        // Pass reference of InventoryController to AddProductController
        AddProductController controller = loader.getController();
        controller.setInventoryController(this);

        Stage stage = new Stage();
        stage.setTitle("Add Product");
        stage.setScene(new Scene(root, 300, 350));
        stage.show();
    }

    public TableView<ProductModel> getProductTable() {
        return productTable;
    }

    @FXML
    public void goBack(ActionEvent e) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/dashboard.fxml"));
        Stage stage = (Stage) productTable.getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
    }
}
