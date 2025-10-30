package com.cwk.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class InvoiceController {

    // ==== Invoice Fields ====
    @FXML private TextField recipientNameField;
    @FXML private TextField addressField;
    @FXML private TextField contactNoField;
    @FXML private TextField emailField;
    @FXML private TextField taxField;

    // ==== Product Fields ====
    @FXML private TextField productNameField;
    @FXML private TextField quantityField;
    @FXML private TextField priceField;

    // ==== Product Table ====
    @FXML private TableView<ProductItem> productTable;
    @FXML private TableColumn<ProductItem, String> productColumn;
    @FXML private TableColumn<ProductItem, Integer> quantityColumn;
    @FXML private TableColumn<ProductItem, Double> priceColumn;
    @FXML private TableColumn<ProductItem, Double> totalColumn;

    // ==== Invoice Table (Fetched Data) ====
    @FXML private TableView<InvoiceSummary> invoiceTable;
    @FXML private TableColumn<InvoiceSummary, String> colName;
    @FXML private TableColumn<InvoiceSummary, String> colEmail;
    @FXML private TableColumn<InvoiceSummary, Double> colTotal;
    @FXML private TableColumn<InvoiceSummary, Double> colTax;
    @FXML private TableColumn<InvoiceSummary, Double> colFinal;

    // ==== Labels ====
    @FXML private Label totalLabel;
    @FXML private Label taxLabel;
    @FXML private Label grandTotalLabel;

    private final ObservableList<ProductItem> productList = FXCollections.observableArrayList();
    private final ObservableList<InvoiceSummary> invoiceList = FXCollections.observableArrayList();
    private final DecimalFormat df = new DecimalFormat("0.00");
    private double grandTotalValue = 0.0;

    private static final String API_URL = "http://localhost:8080/api/invoices";

    // ==== Initialize ====
    @FXML
    public void initialize() {
        // Product table setup
        productColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        productTable.setItems(productList);

        // Invoice summary table setup
        colName.setCellValueFactory(new PropertyValueFactory<>("recipientName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("taxCost"));
        colFinal.setCellValueFactory(new PropertyValueFactory<>("finalCost"));
        invoiceTable.setItems(invoiceList);

        fetchInvoicesFromBackend();
    }

    // ==== Add Product ====
    @FXML
    public void addProductToList(ActionEvent event) {
        try {
            String name = productNameField.getText().trim();
            int qty = Integer.parseInt(quantityField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            double total = qty * price;

            if (name.isEmpty() || qty <= 0 || price <= 0) {
                showAlert("Invalid Input", "Please enter valid product details.");
                return;
            }

            productList.add(new ProductItem(name, qty, price, total));

            productNameField.clear();
            quantityField.clear();
            priceField.clear();

            updateTotals();
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter numeric values for quantity and price.");
        }
    }

    // ==== Update Totals ====
    private void updateTotals() {
        double subtotal = productList.stream().mapToDouble(ProductItem::getTotal).sum();
        double taxRate = 0;
        try {
            if (!taxField.getText().isEmpty()) taxRate = Double.parseDouble(taxField.getText());
        } catch (NumberFormatException ignored) {}

        double taxAmount = subtotal * (taxRate / 100);
        grandTotalValue = subtotal + taxAmount;

        totalLabel.setText("Subtotal: ₹" + df.format(subtotal));
        taxLabel.setText("Tax (" + taxRate + "%): ₹" + df.format(taxAmount));
        grandTotalLabel.setText("Grand Total: ₹" + df.format(grandTotalValue));
    }

    // ==== Save Invoice ====
    @FXML
    public void saveInvoice(ActionEvent event) {
        if (recipientNameField.getText().isEmpty()) {
            showAlert("Validation Error", "Recipient name cannot be empty.");
            return;
        }

        if (productList.isEmpty()) {
            showAlert("Validation Error", "Invoice must contain at least one product.");
            return;
        }

        try {
            JSONObject invoiceJson = new JSONObject();
            invoiceJson.put("recipientName", recipientNameField.getText());
            invoiceJson.put("address", addressField.getText());
            invoiceJson.put("contactNo", contactNoField.getText());
            invoiceJson.put("email", emailField.getText());

            JSONArray itemsArray = new JSONArray();
            for (ProductItem item : productList) {
                JSONObject itemJson = new JSONObject();
                itemJson.put("productName", item.getProduct());
                itemJson.put("quantity", item.getQuantity());
                itemJson.put("unitPrice", item.getPrice());
                itemJson.put("totalPrice", item.getTotal());
                itemsArray.put(itemJson);
            }
            invoiceJson.put("items", itemsArray);

            sendInvoiceToBackend(invoiceJson);
            fetchInvoicesFromBackend(); // refresh table

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save invoice: " + e.getMessage());
        }
    }

    // ==== Send to Backend ====
    private void sendInvoiceToBackend(JSONObject invoiceJson) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = invoiceJson.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
            showAlert("Success", "Invoice saved successfully!");
            clearAll();
        } else {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                String response = br.readLine();
                System.err.println("Error Response: " + response);
                showAlert("Error", "Failed to save invoice. Server: " + response);
            }
        }
    }

    // ==== Fetch Existing Invoices ====
    private void fetchInvoicesFromBackend() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                String response = new String(conn.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                JSONArray jsonArray = new JSONArray(response);

                invoiceList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    invoiceList.add(new InvoiceSummary(
                            obj.getString("recipientName"),
                            obj.optString("email", ""),
                            obj.optDouble("totalCost", 0.0),
                            obj.optDouble("taxCost", 0.0),
                            obj.optDouble("finalCost", 0.0)
                    ));
                }
            } else {
                System.err.println("Failed to fetch invoices. HTTP code: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            System.err.println("Error fetching invoices: " + e.getMessage());
        }
    }

    // ==== Helpers ====
    private void clearAll() {
        recipientNameField.clear();
        addressField.clear();
        contactNoField.clear();
        emailField.clear();
        taxField.clear();
        productList.clear();
        updateTotals();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // ==== Inner classes ====
    public static class ProductItem {
        private String product;
        private int quantity;
        private double price;
        private double total;

        public ProductItem(String product, int quantity, double price, double total) {
            this.product = product;
            this.quantity = quantity;
            this.price = price;
            this.total = total;
        }

        public String getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public double getTotal() { return total; }
    }

    public static class InvoiceSummary {
        private String recipientName;
        private String email;
        private double totalCost;
        private double taxCost;
        private double finalCost;

        public InvoiceSummary(String recipientName, String email, double totalCost, double taxCost, double finalCost) {
            this.recipientName = recipientName;
            this.email = email;
            this.totalCost = totalCost;
            this.taxCost = taxCost;
            this.finalCost = finalCost;
        }

        public String getRecipientName() { return recipientName; }
        public String getEmail() { return email; }
        public double getTotalCost() { return totalCost; }
        public double getTaxCost() { return taxCost; }
        public double getFinalCost() { return finalCost; }
    }
}
