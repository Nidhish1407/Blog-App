package com.nidhish.blogapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nidhish.blogapp.entities.Category;
import com.nidhish.blogapp.entities.Post;
import com.nidhish.blogapp.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {
	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	List<Post> findByTitleContaining(String title);
}
