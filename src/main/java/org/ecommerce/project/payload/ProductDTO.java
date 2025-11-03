package org.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a product and its details.")
public class ProductDTO {
    @Schema(description = "Unique identifier of the product", example = "1001")
    private Long productId;
    @Schema(description = "Name of the product", example = "Wireless Bluetooth Headphones")
    private String productName;
    @Schema(description = "Detailed description of the product", example = "High-quality wireless headphones with noise cancellation and 20-hour battery life.")
    private String description;
    @Schema(description = "URL or path of the product image", example = "https://example.com/images/headphones.jpg")
    private String image;
    @Schema(description = "Available stock quantity of the product", example = "50")
    private Integer quantity;
    @Schema(description = "Original price of the product", example = "1999.99")
    private double price;
    @Schema(description = "Discount percentage applied to the product", example = "10.0")
    private double discount;
    @Schema(description = "Final price after applying discount", example = "1799.99")
    private double specialPrice;

}
