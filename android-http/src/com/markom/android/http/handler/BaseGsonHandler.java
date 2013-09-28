package com.markom.android.http.handler;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.exceptions.ParsingNotImplementedException;
import com.markom.android.http.model.ServiceResponse;
import com.markom.android.http.parser.GSONParser;

/**
 * Extends {@link AsyncHttpResponseHandler} and represent base Gson handler class that contains {@link ServiceResponse}
 * object callbacks together with concrete object callbacks.
 * <p>
 * Extending this abstract class requires implementing {@link BaseGsonHandler#parseServiceResponse(String)} method that
 * returns {@link ServiceResponse} or any of it's subclasses. Basic parsing is implemented inside of
 * {@link BaseClassHandler} for simple objects or {@link BaseGenericHandler} for collection's. For custom json schema
 * extend {@link ServiceResponse} class and either {@link BaseClassHandler} or {@link BaseGenericHandler}, and override
 * parsing method.
 * 
 * @param <T> generic type of data encapsulated by {@link ServiceResponse}.
 * 
 * @see {@link BaseClassHandler}, {@link BaseGenericHandler}, {@link ServiceResponse}
 * 
 * @author Marko Milos
 */
public abstract class BaseGsonHandler<T> extends AsyncHttpResponseHandler {

	/**
	 * Create {@link ServiceResponse} or any of it's subclasses from string response data. Use {@link GSONParser}
	 * methods to create objects from json string.
	 * 
	 * @param response raw response data as string
	 * @return {@link ServiceResponse} kind of object.
	 * @throws GsonParsingException thrown when gson parsing fails.
	 */
	protected abstract ServiceResponse<T> parseServiceResponse(String response) throws GsonParsingException;

	/**
	 * Called when request is finished succefully.
	 * 
	 * @param result requested data.
	 */
	public void onSuccess(T result) {
	}

	/**
	 * Called when request is finished succefully.
	 * 
	 * @param statusCode the status code of the response.
	 * @param result requested data.
	 */
	public void onSuccess(int statusCode, T result) {
		onSuccess(result);
	}

	/**
	 * Called when request is finished succefully.
	 * 
	 * @param statusCode the status code of the response.
	 * @param headers the headers of the HTTP response.
	 * @param result requested data.
	 */
	public void onSuccess(int statusCode, Header[] headers, T result) {
		onSuccess(statusCode, result);
	}

	/**
	 * Called when request is finished succefully.
	 * 
	 * @param result {@link ServiceResponse} containing requested data.
	 */
	public void onSuccess(ServiceResponse<T> result) {
	}

	/**
	 * Called when request is finished succefully.
	 * 
	 * @param statusCode the status code of the response.
	 * @param result {@link ServiceResponse} containing requested data.
	 */
	public void onSuccess(int statusCode, ServiceResponse<T> result) {
		onSuccess(result);
	}

	/**
	 * Called when request is finished succefully.
	 * 
	 * @param statusCode the status code of the response.
	 * @param headers the headers of the HTTP response.
	 * @param result {@link ServiceResponse} containing requested data.
	 */
	public void onSuccess(int statusCode, Header[] headers, ServiceResponse<T> result) {
		onSuccess(statusCode, headers, result.getData());
		onSuccess(statusCode, result);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		handleJsonResponse(statusCode, headers, content);
	}

	/**
	 * Handles response string calling parsing method and appropriate callback methods.
	 * 
	 * @param statusCode the status code of the response.
	 * @param headers the headers of the HTTP response.
	 * @param response raw response data.
	 */
	private void handleJsonResponse(int statusCode, Header[] headers, String response) {
		try {
			// Try to parse response.
			ServiceResponse<T> serviceResponse = parseServiceResponse(response);

			// Check if there is existing service response object
			if (serviceResponse != null) {

				// Check if there is existing data enapsulated by service response object
				// data can be null when empty string is retuned as response ""
				if (serviceResponse.getData() != null) {

					// Everything is parsed well, call success calbacks
					super.onSuccess(statusCode, headers, response);
					onSuccess(statusCode, headers, serviceResponse);

				} else {
					// There is no data. Error happened during parsing process trigger error
					onFailure(new GsonParsingException(), response);
				}

			} else {
				// ServiceResponse is null, parsing is not implemented trigger error
				onFailure(new ParsingNotImplementedException(), response);
			}
		} catch (GsonParsingException e) {
			// Error triggered while parsing response
			onFailure(e, response);
		}
	}

}