package com.markom.android.http.exceptions;

public class GsonParsingException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String EXCEPTION_MESSAGE = "Failure while pasing JSON";

	public GsonParsingException() {
		super(EXCEPTION_MESSAGE);
	}

	public GsonParsingException(String message) {
		super(message);
	}
	
}