package com.company.inventory.services;

import com.company.inventory.models.Product;
import com.company.inventory.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final ProductRepository productRepository;

    @Autowired
    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Optional<Product> updateProduct(Long id, Product product) {
        return productRepository.findById(id)

                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setCategory(product.getCategory());
                    existingProduct.setQuantity(product.getQuantity());
                    existingProduct.setStock(product.getStock());
                    existingProduct.setPrice(product.getPrice());
                    return productRepository.save(existingProduct);
                });
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> findByStockLessThanEqual(int stock) {
        return productRepository.findByStockLessThanEqual(stock);
    }

    public List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Product> findByNameContainingIgnoreCase(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}
