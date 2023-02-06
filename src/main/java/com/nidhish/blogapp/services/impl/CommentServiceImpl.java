package com.nidhish.blogapp.services.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nidhish.blogapp.entities.Comment;
import com.nidhish.blogapp.entities.Post;
import com.nidhish.blogapp.exceptions.ResourceNotFoundException;
import com.nidhish.blogapp.payloads.CommentDto;
import com.nidhish.blogapp.repositories.CommentRepo;
import com.nidhish.blogapp.repositories.PostRepo;
import com.nidhish.blogapp.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private ModelMapper modelMapper;

	public CommentDto createComment(CommentDto commentDto, Integer postId) {
		Post post = this.postRepo.findById(postId)
								 .orElseThrow(
								()->new ResourceNotFoundException("Post", "post id",postId)
								);
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		comment.setPost(post);
		Comment saveComment = this.commentRepo.save(comment);
		return this.modelMapper.map(saveComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId)
										 .orElseThrow(
													()->new ResourceNotFoundException("Comment", "comment id",commentId)
													);
		this.commentRepo.delete(comment);
	}

}
