package org.ecommerce.project.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Schema(description = "Data Transfer Object representing an individual product item within an order.")
public class OrderItemDTO {
    @Schema(description = "Unique identifier of the order item", example = "3001")
    private Long orderItemId;
    @Schema(description = "Product details for this specific order item")
    private ProductDTO productDTO;
    @Schema(description = "Quantity of the product ordered", example = "2")
    private Integer quantity;
    @Schema(description = "Discount applied to this product item, if any", example = "5.0")
    private Double discount;
    @Schema(description = "Final price of the product after discount", example = "1499.99")
    private Double orderedProductPrice;

}
