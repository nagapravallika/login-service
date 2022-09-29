package com.datafoundry.loginUserService.model.response;

public class LoginResponse {

	private String message;
	private String uuid;
	private Boolean success;
	private Integer statusCode;
	private Object object;


	public LoginResponse(String message, String uuid, Boolean success, Integer statusCode, Object object) {
		super();
		this.message = message;
		this.uuid = uuid;
		this.success = success;
		this.statusCode = statusCode;
		this.object = object;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
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
