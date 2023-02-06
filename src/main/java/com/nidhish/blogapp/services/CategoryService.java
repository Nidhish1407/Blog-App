package com.nidhish.blogapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nidhish.blogapp.payloads.CategoryDto;

@Service
public interface CategoryService {

	CategoryDto createCategory(CategoryDto categoryDto);
	CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
	public void deleteCategory(Integer categoryId);
	List<CategoryDto> getCategories();
	CategoryDto getCategory(Integer categoryId);
}
