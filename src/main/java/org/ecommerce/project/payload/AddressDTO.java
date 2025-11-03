package org.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object representing a user's address details.")
public class AddressDTO {
    @Schema(description = "Unique identifier of the address", example = "101")
   private long addressId;
    @Schema(description = "Street name or area details", example = "MG Road")
    private String street;
    @Schema(description = "Name or number of the building", example = "Sunshine Apartments")
    private String buildingName;
    @Schema(description = "City name", example = "Pune")
    private String city;
    @Schema(description = "State name", example = "Maharashtra")
    private String state;
    @Schema(description = "Postal code of the area", example = "411001")
    private String pincode;


    @Schema(description = "Country name", example = "India")
    private String country;

}
