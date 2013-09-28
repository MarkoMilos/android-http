package com.markom.android.http.exceptions;

public class ParsingNotImplementedException extends Exception {

	private static final long serialVersionUID = 1L;
 
	private static final String EXCEPTION_MESSAGE = "Parsing exception";

	public ParsingNotImplementedException() {
		super(EXCEPTION_MESSAGE); 
	}

	public ParsingNotImplementedException(String message) {
		super(message);  
	} 

}