package com.example.markom.http.schema;

import com.google.gson.annotations.SerializedName;

public class Meta {

	@SerializedName("success")
	private boolean success;

	@SerializedName("error_code")
	private int errorCode;

	@SerializedName("error_type")
	private String errorType;

	@SerializedName("error_message")
	private String errorMessage;

	@SerializedName("user_error_message")
	private String userErrorMessage;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getUserErrorMessage() {
		return userErrorMessage;
	}

	public void setUserErrorMessage(String userErrorMessage) {
		this.userErrorMessage = userErrorMessage;
	}
	
}