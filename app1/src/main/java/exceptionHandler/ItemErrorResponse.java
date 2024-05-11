package exceptionHandler;

import java.time.LocalDateTime;

public class ItemErrorResponse {

	private int status;
    private String message;
    private LocalDateTime timestamp;
    
	public ItemErrorResponse(int status, String message, LocalDateTime timestamp) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}
    
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return "ItemErrorResponse [status=" + status + ", message=" + message + ", timestamp=" + timestamp + "]";
	}

	
}