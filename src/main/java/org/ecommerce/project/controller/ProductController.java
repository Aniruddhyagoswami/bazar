package org.ecommerce.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ecommerce.project.config.AppConstants;
import org.ecommerce.project.payload.ProductDTO;
import org.ecommerce.project.payload.ProductResponse;
import org.ecommerce.project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    ProductService productService;

    @Tag(name = "Products APIs", description = "APIs for managing products")
    @Operation(
            summary = "Add a new product",
            description = "Creates and saves a new product under a specific category."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data or category ID"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long categoryId) {
      ProductDTO savedProductDTO=productService.addProduct(categoryId,productDTO);
      return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }
    @Tag(name = "Products APIs", description = "APIs for managing products")
    @Operation(
            summary = "Get all products",
            description = "Retrieves a paginated and sorted list of all available products."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder
    ){
        ProductResponse productResponse=productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


    @Tag(name = "Products APIs", description = "APIs for managing products")
    @Operation(
            summary = "Get products by category",
            description = "Fetches all products belonging to a specific category with pagination and sorting."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId,
                                                                 @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                 @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                 @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sortBy,
                                                                 @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        ProductResponse productResponse=productService.searchByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @Tag(name = "Products APIs", description = "APIs for managing products")
    @Operation(
            summary = "Search products by keyword",
            description = "Retrieves a list of products that match the given search keyword."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found successfully"),
            @ApiResponse(responseCode = "404", description = "No products found for the keyword"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword,
                                                                @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
                                                                @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
                                                                @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_PRODUCTS_BY,required = false) String sortBy,
                                                                @RequestParam(name = "sortOrder",defaultValue = AppConstants.SORT_DIR,required = false) String sortOrder){
        ProductResponse productResponse=productService.searchProductByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);

    }
    @Tag(name = "Products APIs", description = "APIs for managing products")
    @Operation(
            summary = "Update product details",
            description = "Updates the information of an existing product by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid product data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO updatedProductDTO=productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
    }
    @Tag(name = "Products APIs", description = "APIs for managing products")
    @Operation(
            summary = "Delete a product",
            description = "Deletes a product from the system using its product ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable long productId){
        ProductDTO deleteProduct=productService.deleteProduct(productId);
        return new ResponseEntity<>(deleteProduct,HttpStatus.OK);
    }

    @Tag(name = "Products APIs", description = "APIs for managing products")
    @Operation(
            summary = "Update product image",
            description = "Uploads or replaces the image of an existing product."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product image updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid image file"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updatedProduct=productService.updateProductImage(productId,image);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }
}
