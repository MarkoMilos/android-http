package com.markom.android.http.loader;

import com.markom.android.http.model.ServiceResponse;

/**
 * Response object used by {@link BaseGsonLoader} as result type of data.
 * <p>
 * This class encapsulates request informations like: is request succesfull, the request HTTP status code, throwable
 * error object if any, raw string response data and also generic {@link ServiceResponse} object that contains parsed
 * requested data.
 * 
 * @param <T> generic type of data encapsulated by {@link ServiceResponse}.
 * 
 * @author Marko Milos
 */
public class LoaderResponse<T> {

	private boolean isSuccess;
	private int httpStatusCode;
	private Throwable error;
	private String response;
	private ServiceResponse<T> serviceResponse;

	public LoaderResponse() {
		this.isSuccess = false;
		this.setHttpStatusCode(-1);
		this.error = null;
		this.response = null;
		this.serviceResponse = new ServiceResponse<T>();
	}

	public LoaderResponse(boolean isSuccess, int httpStatusCode, Throwable error, String response,
			ServiceResponse<T> serviceResponse) {
		this.isSuccess = isSuccess;
		this.setHttpStatusCode(httpStatusCode);
		this.error = error;
		this.response = response;
		this.serviceResponse = serviceResponse;
	}

	/**
	 * Retrive request status.
	 * 
	 * @return <code>true</code> if request was succesfull, <code>false</code> otherwise.
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * Set request status.
	 * 
	 * @param isSuccess is request succesfull or not.
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * Retrive HTTP status code.
	 * 
	 * @return 1xx, 2xx, 3xx, 4xx, 5xx, 6xx status code.
	 */
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	/**
	 * Set request HTTP status code.
	 * 
	 * @param httpStatusCode to set.
	 */
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	/**
	 * Retrive error if there is any for this request.
	 * 
	 * @return {@link Throwable} error or <code>null</code> if no error occured.
	 */
	public Throwable getError() {
		return error;
	}

	/**
	 * Set error for this request.
	 * 
	 * @param error to set.
	 */
	public void setError(Throwable error) {
		this.error = error;
	}

	/**
	 * @return raw response string data.
	 */
	public String getResponse() {
		return response;
	}

	/**
	 * Sets raw response string data for this request.
	 * 
	 * @param response to set.
	 */
	public void setResponse(String response) {
		this.response = response;
	}

	/**
	 * Retrive {@link ServiceResponse} that encapsulates requested data with loader.
	 * 
	 * @return {@link ServiceResponse} with data or <code>null</code> if error occured during request.
	 */
	public ServiceResponse<T> getServiceResponse() {
		return serviceResponse;
	}

	/**
	 * Set {@link ServiceResponse} for this request.
	 * 
	 * @param serviceResponse to set.
	 */
	public void setServiceResponse(ServiceResponse<T> serviceResponse) {
		this.serviceResponse = serviceResponse;
	}

}