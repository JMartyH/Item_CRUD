package com.crud.app1.errorhandling;

public class ItemNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ItemNotFoundException(String message) {
		super(message);
	}

}
