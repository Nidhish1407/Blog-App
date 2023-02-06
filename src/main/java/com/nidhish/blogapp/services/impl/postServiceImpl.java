package com.nidhish.blogapp.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nidhish.blogapp.entities.Category;
import com.nidhish.blogapp.entities.Post;
import com.nidhish.blogapp.entities.User;
import com.nidhish.blogapp.exceptions.ResourceNotFoundException;
import com.nidhish.blogapp.payloads.PostDto;
import com.nidhish.blogapp.payloads.PostResponse;
import com.nidhish.blogapp.repositories.CategoryRepo;
import com.nidhish.blogapp.repositories.PostRepo;
import com.nidhish.blogapp.services.PostService;
import com.nidhish.blogapp.repositories.UserRepo;

@Service
public class postServiceImpl implements PostService {
	
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
		
		User user = this.userRepo
						.findById(userId)
						.orElseThrow(()->new ResourceNotFoundException("User", "User id", userId));
		
		Category category = this.categoryRepo
								.findById(categoryId)
								.orElseThrow(()->new ResourceNotFoundException("Category", "CategoryId", categoryId));
		
		Post post = this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost = this.postRepo.save(post);
		
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post = this.postRepo.findById(postId)
				 .orElseThrow(
				    () -> new ResourceNotFoundException("Post", "Post Id", postId)
				   );
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		
		Post updatedPost = this.postRepo.save(post);
		
		return this.modelMapper.map(updatedPost	,PostDto.class);
	}

	@Override
	public void deletePost(Integer postId) {
		Post post = this.postRepo.findById(postId)
					 .orElseThrow(
					    () -> new ResourceNotFoundException("Post", "Post Id", postId)
					   );
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		
//		int pageSize = 5;
//		int pageNumber = 1;
		Sort sort = null;
		if(sortDir.equalsIgnoreCase("asc")) {
			sort = Sort.by(sortBy).ascending();
		}
		else{
			sort = Sort.by(sortBy).descending();
		}
		
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Post> pagePost = this.postRepo.findAll(p);
		List<Post> posts = pagePost.getContent();
		
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse = new PostResponse();
		
		postResponse.setContent(postDtos);
		postResponse.setPageNumber(pagePost.getNumber());
		postResponse.setPageSize(pagePost.getSize());
		postResponse.setTotalElements(pagePost.getTotalElements());
		postResponse.setTotalPages(pagePost.getTotalPages());
		postResponse.setLastPage(pagePost.isLast());
		
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
					.orElseThrow(
							()-> new ResourceNotFoundException("Post","post id",postId)
						);
		return this.modelMapper.map(post,PostDto.class);
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		Category category = this.categoryRepo
								.findById(categoryId)
								.orElseThrow(
										()-> new ResourceNotFoundException("Category","category Id",categoryId)
								);
		List<Post> posts = this.postRepo.findByCategory(category);
		
		List<PostDto> postDtos = posts.stream()
									  .map(post -> this.modelMapper.map(post,PostDto.class))
									  .collect(Collectors.toList()); 
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
		User user = this.userRepo
						.findById(userId)
						.orElseThrow(
								()-> new ResourceNotFoundException("User", "user Id", userId)
								);
		List<Post> posts  = this.postRepo.findByUser(user);
		List<PostDto> postDtos = posts.stream()
									  .map(post -> this.modelMapper.map(post, PostDto.class))
									  .collect(Collectors.toList());
//		for (Post post : posts) {
//			System.out.println(post.getUser());
//		}
		return postDtos;
	}
	
	@Override
	public List<PostDto> searchPosts(String keyword){
		List<Post> posts = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> postDtos = posts.stream()
				  .map(post -> this.modelMapper.map(post, PostDto.class))
				  .collect(Collectors.toList());
		return postDtos;
	}

}
