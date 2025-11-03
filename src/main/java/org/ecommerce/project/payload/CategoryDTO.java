package org.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object representing a product category.")
public class CategoryDTO {
    @Schema(description = "Unique identifier of the category", example = "101")
    private long categoryId;
    @Schema(description = "Name of the category", example = "Electronics")
    private String categoryName;
}
