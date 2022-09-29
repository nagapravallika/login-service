package com.datafoundry.loginUserService.model.response;

public class UserResponse {

	private String message;
	private Boolean success;
	private Integer statusCode;
	private Object object;

	public UserResponse(String message, Boolean success, Integer statusCode) {
		this.message = message;
		this.success = success;
		this.statusCode = statusCode;
	}

	public UserResponse(Boolean success, Integer statusCode, Object object) {
		this.success = success;
		this.statusCode = statusCode;
		this.object = object;
	}
	
	public UserResponse(String message, Boolean success, Integer statusCode, Object object) {
		this.message= message;
		this.success = success;
		this.statusCode = statusCode;
		this.object = object;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
