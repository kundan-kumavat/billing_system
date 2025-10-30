package com.cwk.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cwk.billing.model.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> { }
