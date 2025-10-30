package com.cwk.ui.models;

import java.util.List;

public class Invoice {
    private String recipientName;
    private String address;
    private String contactNo;
    private String email;
    private Double totalCost;
    private Double taxCost;
    private Double finalCost;
    private List<InvoiceItem> items;

    // Getters and setters
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getContactNo() { return contactNo; }
    public void setContactNo(String contactNo) { this.contactNo = contactNo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }
    public Double getTaxCost() { return taxCost; }
    public void setTaxCost(Double taxCost) { this.taxCost = taxCost; }
    public Double getFinalCost() { return finalCost; }
    public void setFinalCost(Double finalCost) { this.finalCost = finalCost; }
    public List<InvoiceItem> getItems() { return items; }
    public void setItems(List<InvoiceItem> items) { this.items = items; }
}
