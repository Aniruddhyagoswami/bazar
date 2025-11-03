package org.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object representing a paginated list of product categories.")
public class CategoryResponse {
    @Schema(description = "List of category data returned in the current page")
    private List<CategoryDTO> content;
    @Schema(description = "Current page number (0-based index)", example = "0")
    private  Integer pageNumber;
    @Schema(description = "Number of categories per page", example = "10")
    private  Integer pageSize;
    @Schema(description = "Total number of categories available", example = "56")
    private  Long totalElements;
    @Schema(description = "Total number of available pages", example = "6")
    private  Integer totalPages;
    @Schema(description = "Indicates whether this is the last page of results", example = "false")
    private  boolean lastPage;

}
