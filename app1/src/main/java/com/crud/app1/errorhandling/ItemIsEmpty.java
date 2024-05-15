package com.crud.app1.errorhandling;

public class ItemIsEmpty extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ItemIsEmpty(String message) {
		super(message);
	}
}
