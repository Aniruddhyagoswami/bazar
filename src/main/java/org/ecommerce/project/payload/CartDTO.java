package org.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing the shopping cart details of a user.")
public class CartDTO {
    @Schema(description = "Unique identifier of the cart", example = "2001")
    private Long cartId;
    @Schema(description = "Total price of all products in the cart", example = "1499.99")
    private Double totalPrice=0.0;
    @Schema(description = "List of products currently in the cart")
    private List<ProductDTO> products=new ArrayList<>();

}
