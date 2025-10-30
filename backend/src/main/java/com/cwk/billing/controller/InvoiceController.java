package com.cwk.billing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.cwk.billing.model.Invoice;
import com.cwk.billing.service.InvoiceService;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public List<Invoice> getAllInvoices() {
        logger.info("Fetching all invoices...");
        List<Invoice> invoices = invoiceService.getAllInvoices();
        logger.info("Fetched {} invoices successfully", invoices.size());
        return invoices;
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        try {
            logger.info("Creating new invoice for recipient: {}", invoice.getRecipientName());

            if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
                logger.error("Invoice must contain at least one item.");
                throw new IllegalArgumentException("Invoice must contain at least one item.");
            }

            Invoice savedInvoice = invoiceService.createInvoice(invoice);
            logger.info("Invoice saved successfully with ID: {}", savedInvoice.getId());
            return savedInvoice;

        } catch (Exception e) {
            logger.error("Error saving invoice: {}", e.getMessage(), e);
            throw new RuntimeException("Error saving invoice: " + e.getMessage());
        }
    }
}
