package com.datafoundry.loginUserService.model.response;

public class SecurityQuestionsResponse {

	private String message;
	private Boolean success;
	private Integer statusCode;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public SecurityQuestionsResponse(String message, Boolean success, Integer statusCode) {
		super();
		this.message = message;
		this.success = success;
		this.statusCode = statusCode;
	}

	
}
