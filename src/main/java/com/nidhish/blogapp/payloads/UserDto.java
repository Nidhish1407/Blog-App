package com.nidhish.blogapp.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

	private int id;
	@NotEmpty
	@Size(min = 4, message = "Username must have minimum 4 characters.")
	private String name;
	@Email(message = "Your email address is not valid!")
	private String email;	
	@NotEmpty
	@Size(min = 3,max = 10,message = "Password length must be between 3 to 10 characters.")
	private String password;
	@NotNull
	private String about;
}
