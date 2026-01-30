package tn.project.dev.microservice_inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.project.dev.microservice_inventory.model.Product;  // ← IMPORT CORRECT
import tn.project.dev.microservice_inventory.ProductRepository;
import tn.project.dev.microservice_inventory.events.OrderCreatedEvent;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    // Mapping entre productId CommandClient -> productId Inventory
    private final Map<Long, Long> productIdMap = Map.of(
            56L, 1L,  // Ordinateur Portable
            89L, 2L   // Souris Gaming
    );

    // Méthodes pour le contrôleur
    public Product getProduct(String productId) {
        return productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new RuntimeException("Produit non trouvé: " + productId));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(String name, String description, Double price, Integer quantity) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        return productRepository.save(product);
    }

    public Product updateProduct(String productId, Integer quantity) {
        Product product = getProduct(productId);
        product.setQuantity(quantity);
        return productRepository.save(product);
    }

    public void deleteProduct(String productId) {
        productRepository.deleteById(Long.valueOf(productId));
    }

    // Méthode pour Kafka Consumer
    public void processOrder(OrderCreatedEvent event) {
        log.info("Traitement de la commande {}", event.getOrderId());

        for (OrderCreatedEvent.OrderItem item : event.getItems()) {
            try {
                // 🔹 Traduire l'ID CommandClient vers l'ID Inventory
                Long inventoryId = productIdMap.get(item.getProductId());
                if (inventoryId == null) {
                    log.error(" Produit non trouvé dans l'inventaire (mapping manquant): {}", item.getProductId());
                    continue;
                }

                Optional<Product> optionalProduct = productRepository.findById(inventoryId);

                if (optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();

                    if (product.getQuantity() >= item.getQuantity()) {
                        product.setQuantity(product.getQuantity() - item.getQuantity());
                        productRepository.save(product);

                        log.info(" Stock mis à jour: produit {} -> {} unités restantes",
                                product.getId(), product.getQuantity());
                    } else {
                        log.warn("Stock insuffisant: produit {} (demandé: {}, disponible: {})",
                                product.getId(), item.getQuantity(), product.getQuantity());
                    }
                } else {
                    log.error(" Produit non trouvé dans l'inventaire: {}", inventoryId);
                }
            } catch (Exception e) {
                log.error(" Erreur traitement produit {}: {}", item.getProductId(), e.getMessage());
            }
        }

        log.info(" Commande {} traitée avec succès", event.getOrderId());
    }
}
