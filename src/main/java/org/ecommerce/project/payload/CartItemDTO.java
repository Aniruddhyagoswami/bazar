package org.ecommerce.project.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing an individual item within a user's cart.")
public class CartItemDTO {
    @Schema(description = "Unique identifier of the cart item", example = "5001")
    private Long cartItemId;
    @Schema(description = "Cart to which this item belongs")
    private CartDTO cart;
    @Schema(description = "Product details associated with this cart item")
    private ProductDTO productDTO;
    @Schema(description = "Quantity of the product added to the cart", example = "2")
    private Integer quantity;
    @Schema(description = "Discount applied to this item, if any", example = "10.0")
    private Double discount;
    @Schema(description = "Final price of the product after discount", example = "899.99")
    private Double productPrice;
}
