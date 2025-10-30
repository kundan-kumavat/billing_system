package com.cwk.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cwk.billing.model.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> { }
