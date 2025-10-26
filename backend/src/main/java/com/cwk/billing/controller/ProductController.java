
package com.cwk.billing.controller;

import com.cwk.billing.model.Product;
import com.cwk.billing.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping
    public List<Product> getAll() { return service.getAllProducts(); }

    @PostMapping
    public Product add(@RequestBody Product product) { return service.addProduct(product); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.deleteProduct(id); }
}
