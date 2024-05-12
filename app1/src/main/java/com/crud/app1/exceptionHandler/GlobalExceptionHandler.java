package com.crud.app1.exceptionHandler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.annotation.PostConstruct;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@PostConstruct
	public void init() {
		logger.info("GlobalExceptionHandler bean initialized");
	}

	@ExceptionHandler(ItemNotFoundException.class)
	public ResponseEntity<ApiError> handleItemNotFoundException(ItemNotFoundException ex) {
		init();
		System.out.println("Exception caught in GlobalExceptionHandler");
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), LocalDateTime.now());
		logger.error("Item not found", ex);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(ItemIsEmpty.class)
	public ResponseEntity<ApiError> handleItemIsEmpty(ItemIsEmpty ex) {
		init();
		System.out.println("Exception caught in GlobalExceptionHandler");
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), LocalDateTime.now());
		logger.error("There are currently no Items", ex);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	// General Exception Handling
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiError> handleGenericException(Exception ex) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
				LocalDateTime.now());
		logger.error("General Exception", ex);
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

}
