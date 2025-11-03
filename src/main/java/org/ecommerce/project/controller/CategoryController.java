package org.ecommerce.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.ecommerce.project.config.AppConstants;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;
import org.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;




    @Tag(name = "Category APIs", description = "APIs for managing Categories")
    @Operation(
            summary = "Get all categories (with pagination & sorting)",
            description = "Fetches a paginated and sorted list of all available categories."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched list of categories",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @GetMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = true) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = true) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_CATEGORIES_BY,required = true) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = true) String sortOrder
    ){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }
    @Tag(name = "Category APIs", description = "APIs for managing Categories")
    @Operation(
            summary = "Create category",
            description = "Creates a new category in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })

    @PostMapping("/public/categories")
    //@RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }
    @Tag(name = "Category APIs", description = "APIs for managing Categories")
    @Operation(
            summary = "Delete category",
            description = "Deletes a category by its ID (Admin access required)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){
        CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);
    }

    @Tag(name = "Category APIs", description = "APIs for managing Categories")
    @Operation(
            summary = "Update category",
            description = "Updates an existing category using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryDTO.class))),
            @ApiResponse(responseCode = "404", description = "Category not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                                 @PathVariable Long categoryId){
        CategoryDTO savedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
    }
}
