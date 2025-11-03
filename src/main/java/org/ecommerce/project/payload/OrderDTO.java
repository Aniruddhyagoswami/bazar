package org.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing details of a user's order.")
public class OrderDTO {

    @Schema(description = "Unique identifier of the order", example = "10001")
    private Long orderId;
    @Schema(description = "Email of the user who placed the order", example = "john.doe@example.com")
    private String email;
    @Schema(description = "List of items included in this order")
    private List<OrderItemDTO> orderItems;
    @Schema(description = "Date when the order was placed", example = "2025-11-03")
    private LocalDate orderDate;
    @Schema(description = "Payment information associated with the order")
    private PaymentDTO payment;
    @Schema(description = "Total amount of the order", example = "2599.75")
    private Double totalAmount;
    @Schema(description = "Current status of the order", example = "SHIPPED")
    private String orderStatus;
    @Schema(description = "Address ID where the order will be delivered", example = "501")
    private Long addressId;
}
