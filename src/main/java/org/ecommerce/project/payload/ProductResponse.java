package org.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object representing a paginated list of products.")
public class ProductResponse {
    @Schema(description = "List of products returned in the current page")
    private List<ProductDTO> content;
    @Schema(description = "Current page number (0-based index)", example = "0")
    private Integer pageNumber;
    @Schema(description = "Number of products per page", example = "10")
    private Integer pageSize;
    @Schema(description = "Total number of products available", example = "125")
    private Integer totalElements;
    @Schema(description = "Total number of available pages", example = "13")
    private Integer totalPages;
    @Schema(description = "Indicates whether this is the last page of the results", example = "false")
    private boolean lastPage;
}
