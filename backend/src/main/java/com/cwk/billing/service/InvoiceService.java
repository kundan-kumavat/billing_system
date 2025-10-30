package com.cwk.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cwk.billing.repository.InvoiceRepository;
import com.cwk.billing.model.Invoice;
import com.cwk.billing.model.InvoiceItem;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice createInvoice(Invoice invoice) {
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            throw new IllegalArgumentException("Invoice must contain at least one item.");
        }

        double totalCost = 0.0;
        for (InvoiceItem item : invoice.getItems()) {
            double itemTotal = item.getQuantity() * item.getUnitPrice();
            item.setTotalPrice(itemTotal);
            item.setInvoice(invoice); // establish relationship
            totalCost += itemTotal;
        }

        double taxCost = totalCost * 0.18; // 18% GST
        double finalCost = totalCost + taxCost;

        invoice.setTotalCost(totalCost);
        invoice.setTaxCost(taxCost);
        invoice.setFinalCost(finalCost);

        return invoiceRepository.save(invoice);
    }
}
