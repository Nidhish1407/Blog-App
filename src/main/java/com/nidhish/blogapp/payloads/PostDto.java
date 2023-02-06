package com.nidhish.blogapp.payloads;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import com.nidhish.blogapp.entities.Category;
import com.nidhish.blogapp.entities.Comment;
import com.nidhish.blogapp.entities.User;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class PostDto {
	
	private Integer postId;
	private String title;
	private String content;
	private String imageName;
	private Date addedDate;
	private CategoryDto category;
	private UserDto user;
	private  Set<CommentDto> comments = new HashSet<>();
}
