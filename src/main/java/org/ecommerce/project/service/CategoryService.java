package org.ecommerce.project.service;

import jakarta.validation.Valid;

import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;



public interface CategoryService {
    CategoryDTO deleteCategory(long categoryId);

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder);
    CategoryDTO createCategory(@Valid CategoryDTO categoryDTO);



    CategoryDTO updateCategory(CategoryDTO categoryDTO, long categoryId);
}
