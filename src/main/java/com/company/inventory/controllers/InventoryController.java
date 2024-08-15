package com.company.inventory.controllers;

import com.company.inventory.models.Product;
import com.company.inventory.services.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = inventoryService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product addedProduct = inventoryService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Optional<Product> updatedProduct = inventoryService.updateProduct(id, product);
        return updatedProduct.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = inventoryService.deleteProduct(id);
        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Optional<Product> product = inventoryService.findByName(name);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = inventoryService.findByCategory(category);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stock/{stock}")
    public ResponseEntity<List<Product>> getProductsByStock(@PathVariable int stock) {
        List<Product> products = inventoryService.findByStockLessThanEqual(stock);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/price")
    public ResponseEntity<List<Product>> getProductsByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
        List<Product> products = inventoryService.findByPriceBetween(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String keyword) {
        logger.debug("Searching products with keyword: {}", keyword);
        List<Product> products = inventoryService.findByNameContainingIgnoreCase(keyword);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test successful");
    }

    @GetMapping("/sample")
    public ResponseEntity<List<String>> getSampleData() {
        List<String> sample = Arrays.asList("Item1", "Item2", "Item3");
        return ResponseEntity.ok(sample);
    }
}
