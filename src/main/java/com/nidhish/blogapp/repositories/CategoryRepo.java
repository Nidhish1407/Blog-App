package com.nidhish.blogapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nidhish.blogapp.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {

}
