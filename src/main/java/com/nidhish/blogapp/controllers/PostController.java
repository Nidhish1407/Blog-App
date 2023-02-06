package com.nidhish.blogapp.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nidhish.blogapp.payloads.ApiResponse;
import com.nidhish.blogapp.payloads.PostDto;
import com.nidhish.blogapp.payloads.PostResponse;
import com.nidhish.blogapp.services.FileService;
import com.nidhish.blogapp.services.PostService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class PostController {
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${project.image}")
	private String path;
	
	@PostMapping("/users/{userId}/categories/{categoryId}/posts")
	public ResponseEntity<PostDto> createPost(
			@RequestBody PostDto postDto,
			@PathVariable Integer userId,
			@PathVariable Integer categoryId
			){
		PostDto createPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createPost,HttpStatus.CREATED);
	}
	
	@GetMapping("/users/{userId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByUser(
			@PathVariable Integer userId
			){
		List<PostDto> posts = this.postService.getPostsByUser(userId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	}
	
	@GetMapping("/categories/{categoryId}/posts")
	public ResponseEntity<List<PostDto>> getPostsByCategory(
			@PathVariable Integer categoryId
			){
		List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
		return new ResponseEntity<List<PostDto>>(posts,HttpStatus.OK);
	} 
	@GetMapping("/posts")
	public ResponseEntity<PostResponse> getAllPosts(
			
	@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
	@RequestParam(value = "pageSize",defaultValue = "10",required = false) Integer pageSize,
	@RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
	@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			
			){
		PostResponse posts = this.postService.getAllPosts(pageNumber,pageSize,sortBy,sortDir);
		return new ResponseEntity<PostResponse>(posts,HttpStatus.OK);
	} 
	
	@GetMapping("/posts/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId){
		PostDto posts = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(posts,HttpStatus.OK);
	} 
	
	
	@DeleteMapping("/posts/{postId}")
	public ApiResponse DeletePostById(@PathVariable Integer postId){
		this.postService.deletePost(postId);
		return new ApiResponse("Post is successfully deleted!",true);
	} 
	
	@PutMapping("/posts/{postId}")
	public ResponseEntity<PostDto> updatePost(
			@RequestBody PostDto postDto,
			@PathVariable Integer postId){
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	} 
	
	@GetMapping("/posts/search/{keywords}")
	public ResponseEntity<List<PostDto>> searchPostsByTitle(
			@PathVariable("keywords") String keywords
			){
		List<PostDto> searchPost = this.postService.searchPosts(keywords);
		return new ResponseEntity<List<PostDto>>(searchPost,HttpStatus.OK);
	}
	
	//post image upload
	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadPostImage(
			@RequestParam("image") MultipartFile image,
			@PathVariable Integer postId
			) throws IOException{
		String fileName = this.fileService.uploadImage(path, image);
		PostDto postDto = this.postService.getPostById(postId);
		postDto.setImageName(fileName);
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
	}
	
	//Serve files
	@GetMapping(value="/post/image/{imageName}",produces=MediaType.IMAGE_JPEG_VALUE)
	public void downLoadImage(
			@PathVariable("imageName") String imageName,
			HttpServletResponse response
			) throws IOException {
		InputStream resource = this.fileService.getResource(path, imageName);
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
