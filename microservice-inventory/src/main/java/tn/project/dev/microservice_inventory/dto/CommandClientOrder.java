package tn.project.dev.microservice_inventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class CommandClientOrder {
    @JsonProperty("clientId")
    private String clientId;

    @JsonProperty("lignes")
    private List<Ligne> lignes;

    @Data
    public static class Ligne {
        @JsonProperty("productId")
        private Long productId;

        @JsonProperty("quantity")
        private Integer quantity;

        @JsonProperty("price")
        private Double price;  // Optionnel
    }
}