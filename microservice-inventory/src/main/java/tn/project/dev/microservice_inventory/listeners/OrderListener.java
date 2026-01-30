package tn.project.dev.microservice_inventory.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tn.project.dev.microservice_inventory.events.OrderCreatedEvent;
import tn.project.dev.microservice_inventory.service.InventoryService;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderListener {

    private final ObjectMapper objectMapper;
    private final InventoryService inventoryService;

    @KafkaListener(topics = "orders.created", groupId = "inventory-group")
    public void handleOrderCreated(String rawMessage) {
        try {
            log.info("📦 Message Kafka brut: {}", rawMessage);

            String jsonToParse = rawMessage;

            // CORRECTION CRITIQUE : Si le message commence et finit par des guillemets
            if (rawMessage.startsWith("\"") && rawMessage.endsWith("\"")) {
                log.info("🔧 Détection: Message encapsulé dans des guillemets");

                // 1. Enlever les guillemets extérieurs
                jsonToParse = rawMessage.substring(1, rawMessage.length() - 1);

                // 2. Remplacer les \" par " (échappement JSON)
                jsonToParse = jsonToParse.replace("\\\"", "\"");

                log.info("🔧 JSON nettoyé: {}", jsonToParse);
            }

            // Vérifier que c'est du JSON valide
            if (!jsonToParse.startsWith("{") || !jsonToParse.endsWith("}")) {
                log.error("❌ ERREUR: Ce n'est pas du JSON valide !");
                log.error("❌ Premier char: '{}' (attendu: '{{')", jsonToParse.charAt(0));
                log.error("❌ Dernier char: '{}' (attendu: '}}')",
                        jsonToParse.charAt(jsonToParse.length() - 1));
                log.error("❌ Taille: {} caractères", jsonToParse.length());
                return;
            }

            // Désérialiser
            OrderCreatedEvent event = objectMapper.readValue(jsonToParse, OrderCreatedEvent.class);

            log.info("✅ SUCCÈS ! Commande: {}, Client: {}",
                    event.getOrderId(), event.getCustomerId());
            log.info("📦 Nombre d'items: {}", event.getItems().size());

            // Traiter la commande
            inventoryService.processOrder(event);

            log.info("🏁 Commande {} traitée avec succès", event.getOrderId());

        } catch (Exception e) {
            log.error("❌ ERREUR de traitement: {}", e.getMessage());
            log.error("❌ Message problématique: {}", rawMessage);

            // Debug avancé
            try {
                // Essayer de voir ce que contient le message
                log.error("❌ Longueur message: {}", rawMessage.length());
                log.error("❌ Premier caractère: '{}' (code: {})",
                        rawMessage.charAt(0), (int) rawMessage.charAt(0));
                log.error("❌ Dernier caractère: '{}' (code: {})",
                        rawMessage.charAt(rawMessage.length() - 1),
                        (int) rawMessage.charAt(rawMessage.length() - 1));
            } catch (Exception e2) {
                // Ignore
            }
        }
    }
}