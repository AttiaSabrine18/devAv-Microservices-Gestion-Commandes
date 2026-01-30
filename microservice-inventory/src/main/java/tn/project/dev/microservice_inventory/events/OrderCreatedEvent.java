package tn.project.dev.microservice_inventory.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderCreatedEvent {

    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private Instant timestamp;

    // CONSTRUCTEUR CRITIQUE : doit accepter un String comme premier paramètre
    // pour que Jackson puisse désérialiser depuis un JSON String
    @JsonCreator
    public static OrderCreatedEvent fromJson(
            @JsonProperty("orderId") String orderId,
            @JsonProperty("customerId") String customerId,
            @JsonProperty("items") List<OrderItem> items,
            @JsonProperty("timestamp") Instant timestamp) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.orderId = orderId;
        event.customerId = customerId;
        event.items = items;
        event.timestamp = timestamp != null ? timestamp : Instant.now();
        return event;
    }

    // Constructeur normal (pas utilisé par Jackson)
    public OrderCreatedEvent(String orderId, String customerId, List<OrderItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.timestamp = Instant.now();
    }

    @Data
    @NoArgsConstructor
    public static class OrderItem {
        private Long productId;
        private Integer quantity;

        @JsonCreator
        public static OrderItem fromJson(
                @JsonProperty("productId") Long productId,
                @JsonProperty("quantity") Integer quantity) {
            OrderItem item = new OrderItem();
            item.productId = productId;
            item.quantity = quantity;
            return item;
        }

        public OrderItem(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}