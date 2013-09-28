package com.markom.android.http.model;

/**
 * Generic service response class that encapsulates core response data.
 * 
 * @param <T> type of data.
 * 
 * @author Marko Milos
 */
public class ServiceResponse<T> {

	private T data;

	/**
	 * Retrives encapsulated requested data.
	 * 
	 * @return request data.
	 */
	public T getData() {
		return data;
	}

	/**
	 * Set service response data.
	 * 
	 * @param data to set.
	 */
	public void setData(T data) {
		this.data = data;
	}

}