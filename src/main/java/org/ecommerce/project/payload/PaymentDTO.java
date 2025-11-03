package org.ecommerce.project.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object representing payment details associated with an order.")
public class PaymentDTO {
    @Schema(description = "Unique identifier of the payment", example = "7001")
    private Long paymentId;
    @Schema(description = "Payment method used for the transaction", example = "Credit Card")
    private String paymentMethod;
    @Schema(description = "Unique payment ID provided by the payment gateway", example = "pay_LxY7ab12345MNQ")
    private String pgPaymentId;
    @Schema(description = "Status of the payment transaction", example = "SUCCESS")
    private String pgStatus;
    @Schema(description = "Response message returned by the payment gateway", example = "Transaction completed successfully")
    private String pgResponseMessage;
    @Schema(description = "Name of the payment gateway used", example = "Razorpay")
    private String pgName;
}
