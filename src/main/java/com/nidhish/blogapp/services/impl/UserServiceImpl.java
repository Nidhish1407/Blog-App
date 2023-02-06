package com.nidhish.blogapp.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nidhish.blogapp.exceptions.*;
import com.nidhish.blogapp.entities.User;
import com.nidhish.blogapp.payloads.UserDto;
import com.nidhish.blogapp.repositories.UserRepo;
import com.nidhish.blogapp.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.userDtoTouser(userDto);
		User savedUser = this.userRepo.save(user);
		return this.userTouserDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User","User Id", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setAbout(userDto.getAbout());
		
		User userUpdated = this.userRepo.save(user);
		
		UserDto userDto1 =  this.userTouserDto(userUpdated);
		return userDto1;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo
				.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","User Id", userId));
		UserDto userDto =  this.userTouserDto(user);
		return userDto;
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = this.userRepo.findAll();
		List<UserDto> userDtos = users.stream()
									.map(user -> this.userTouserDto(user))
									.collect(Collectors.toList());		
		return userDtos;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo
				.findById(userId)
				.orElseThrow(()-> new ResourceNotFoundException("User","User Id", userId));
		this.userRepo.delete(user);
	}
	
	private User userDtoTouser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
//		User user = new User();
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setPassword(userDto.getPassword());
//		user.setAbout(userDto.getAbout());
		return user;
	}
	
	public UserDto userTouserDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
//		UserDto userDto = new UserDto();
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setPassword(user.getPassword());
//		userDto.setAbout(user.getAbout());
		return userDto;
	}
}
