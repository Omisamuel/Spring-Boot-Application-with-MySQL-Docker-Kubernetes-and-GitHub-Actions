package com.company.inventory;

import com.company.inventory.controllers.InventoryController;
import com.company.inventory.models.Product;
import com.company.inventory.services.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product(1L, "Product1", "Category1", 10, 100, new BigDecimal("100.0"));
        product2 = new Product(2L, "Product2", "Category2", 5, 50, new BigDecimal("50.0"));
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = Arrays.asList(product1, product2);
        when(inventoryService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<Product>> response = inventoryController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Product1", response.getBody().get(0).getName());
        assertEquals("Product2", response.getBody().get(1).getName());
        verify(inventoryService, times(1)).getAllProducts();
    }

    @Test
    void testGetProductById() {
        when(inventoryService.findById(1L)).thenReturn(Optional.of(product1));

        ResponseEntity<Product> response = inventoryController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product1", response.getBody().getName());
        verify(inventoryService, times(1)).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        when(inventoryService.findById(3L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = inventoryController.getProductById(3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(inventoryService, times(1)).findById(3L);
    }

    @Test
    void testAddProduct() {
        when(inventoryService.addProduct(any(Product.class))).thenReturn(product1);

        ResponseEntity<Product> response = inventoryController.addProduct(product1);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Product1", response.getBody().getName());
        verify(inventoryService, times(1)).addProduct(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product(1L, "UpdatedProduct1", "Category1", 20, 200, new BigDecimal("200.0"));
        when(inventoryService.updateProduct(eq(1L), any(Product.class))).thenReturn(Optional.of(updatedProduct));

        ResponseEntity<Product> response = inventoryController.updateProduct(1L, updatedProduct);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("UpdatedProduct1", response.getBody().getName());
        assertEquals(20, response.getBody().getQuantity());
        assertEquals(new BigDecimal("200.0"), response.getBody().getPrice());
        verify(inventoryService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        when(inventoryService.updateProduct(eq(3L), any(Product.class))).thenReturn(Optional.empty());

        ResponseEntity<Product> response = inventoryController.updateProduct(3L, new Product());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(inventoryService, times(1)).updateProduct(eq(3L), any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        when(inventoryService.deleteProduct(1L)).thenReturn(true);

        ResponseEntity<Void> response = inventoryController.deleteProduct(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(inventoryService, times(1)).deleteProduct(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        when(inventoryService.deleteProduct(3L)).thenReturn(false);

        ResponseEntity<Void> response = inventoryController.deleteProduct(3L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(inventoryService, times(1)).deleteProduct(3L);
    }

    @Test
    void testGetAllProductsWhenEmpty() {
        when(inventoryService.getAllProducts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Product>> response = inventoryController.getAllProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
        verify(inventoryService, times(1)).getAllProducts();
    }
}
