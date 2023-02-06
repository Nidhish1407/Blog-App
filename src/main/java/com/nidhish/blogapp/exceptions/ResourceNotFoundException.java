package com.nidhish.blogapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter        	
public class ResourceNotFoundException extends RuntimeException {
	String resourceName;
	String fieldName;
	long fieldVal;
	
	
	public ResourceNotFoundException(String resourceName, String fieldName, long fieldVal) {
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldVal));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldVal = fieldVal;
	}
	
	
	
}
