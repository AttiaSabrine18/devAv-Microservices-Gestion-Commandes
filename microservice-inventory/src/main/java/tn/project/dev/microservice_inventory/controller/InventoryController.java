package tn.project.dev.microservice_inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.project.dev.microservice_inventory.model.Product;
import tn.project.dev.microservice_inventory.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getProduct(id.toString()));
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam Double price,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
                inventoryService.createProduct(name, description, price, quantity)
        );
    }

    @PutMapping("/products/{id}/quantity")
    public ResponseEntity<Product> updateProductQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(
                inventoryService.updateProduct(id.toString(), quantity)
        );
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        inventoryService.deleteProduct(id.toString());
        return ResponseEntity.noContent().build();
    }
}