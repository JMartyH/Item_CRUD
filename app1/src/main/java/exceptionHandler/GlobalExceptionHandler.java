package exceptionHandler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	// Custom Exception Handling
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleItemNotFoundException(ItemNotFoundException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), LocalDateTime.now());
        LOG.error("Item not found", ex);
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    // General Exception Handling
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", LocalDateTime.now());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}
