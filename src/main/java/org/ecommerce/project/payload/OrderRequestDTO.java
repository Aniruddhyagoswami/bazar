package org.ecommerce.project.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Data Transfer Object representing a request to place an order.")
public class OrderRequestDTO {
    @Schema(description = "Identifier of the address where the order will be delivered", example = "501")
    private Long addressId;
    @Schema(description = "Identifier of the selected payment method", example = "2")
    private Long paymentMethod;
    @Schema(description = "Name of the payment gateway used for the transaction", example = "Razorpay")
    private String pgName;
    @Schema(description = "Unique payment ID provided by the payment gateway", example = "pay_LxY7ab12345MNQ")
    private String pgPaymentId;
    @Schema(description = "Status of the payment transaction", example = "SUCCESS")
    private String pgStatus;
    @Schema(description = "Detailed response message from the payment gateway", example = "Payment processed successfully")
    private String pgResponseMessage;
}
