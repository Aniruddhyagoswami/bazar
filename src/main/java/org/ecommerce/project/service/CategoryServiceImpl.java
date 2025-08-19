package org.ecommerce.project.service;

import jakarta.validation.Valid;
import org.ecommerce.project.execptions.APIExecption;
import org.ecommerce.project.execptions.ResourceNotFoundException;
import org.ecommerce.project.model.Category;
import org.ecommerce.project.payload.CategoryDTO;
import org.ecommerce.project.payload.CategoryResponse;
import org.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



import java.util.List;



@Service
public class CategoryServiceImpl implements CategoryService{



    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;




    @Override
    public CategoryDTO deleteCategory(long categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));
        categoryRepository.delete(category);
        return modelMapper.map(category,CategoryDTO.class);
    }

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByAndOrder=sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails= PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoriesPage=categoryRepository.findAll(pageDetails);
        List<Category> categories=categoriesPage.getContent();
        if (categories.isEmpty()){
            throw new APIExecption("No categories created yet");
        }
        List<CategoryDTO> categoryDTOS=categories.stream().map(category->modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse categoriesResponse=new CategoryResponse();
        categoriesResponse.setContent(categoryDTOS);
        categoriesResponse.setPageNumber(categoriesPage.getNumber());
        categoriesResponse.setPageSize(categoriesPage.getSize());
        categoriesResponse.setTotalElements(categoriesPage.getTotalElements());
        categoriesResponse.setTotalPages(categoriesPage.getTotalPages());

        categoriesResponse.setLastPage(categoriesPage.isLast());
        return categoriesResponse;
    }

    @Override
    public CategoryDTO createCategory(@Valid CategoryDTO categoryDTO) {
        Category category=modelMapper.map(categoryDTO,Category.class);
        Category categoryFromDB=categoryRepository.findByCategoryName(category.getCategoryName());
        if (categoryFromDB!=null){
            throw new APIExecption("Category with the name: "+category.getCategoryName()+"already exists");
        }
        Category savedCategory= categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, long categoryId) {
        Category savedCategory=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","id",categoryId));
        Category category=modelMapper.map(categoryDTO,Category.class);
        category.setCategoryId(categoryId);
       savedCategory=categoryRepository.save(category);

       return modelMapper.map(savedCategory, CategoryDTO.class);

    }



}
