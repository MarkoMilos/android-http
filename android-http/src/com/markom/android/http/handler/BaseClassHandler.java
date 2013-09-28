package com.markom.android.http.handler;

import com.google.gson.Gson;
import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.model.ServiceResponse;
import com.markom.android.http.parser.GSONParser;

/**
 * Extends {@link BaseGsonHandler} and enables parsing plain object data from json by providing {@link Class} of parsing
 * element. By default {@link BaseGsonHandler#parseServiceResponse(String)} methods try's to parse received response
 * with {@link GSONParser} immediately. In order to parse objects from custom json schema, create custom
 * {@link ServiceResponse} class and override this class to implement your own parsing logic.
 * <p>
 * Example:
 * 
 * <pre>
 * client.get(&quot;www.example.com&quot;, new BaseClassHandler&lt;Person&gt;(Person.class) {
 * 
 * 	&#064;Override
 * 	public void onStart() {
 * 		// started
 * 	}
 * 
 * 	&#064;Override
 * 	public void onSuccess(int statusCode, Header[] headers, ServiceResponse&lt;Person&gt; result) {
 * 		super.onSuccess(statusCode, headers, result);
 * 		Person person = result.getData();
 * 		// handle success
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFailure(Throwable error, String content) {
 * 		super.onFailure(error, content);
 * 		// handle failure
 * 	}
 * 
 * 	&#064;Override
 * 	public void onFinish() {
 * 		// finished
 * 	}
 * 
 * });
 * </pre>
 * 
 * @param <T> generic type of data encapsulated by {@link ServiceResponse}.
 * 
 * @author Marko Milos
 */
public class BaseClassHandler<T> extends BaseGsonHandler<T> {

	/**
	 * {@link Class} instance of object to be parsed. Needed by {@link Gson} in oreder to do parse.
	 */
	protected Class<T> clazz;

	/**
	 * Creates an new instance of {@link BaseClassHandler}.
	 * 
	 * @param clazz the {@link Class} instance of object requested.
	 */
	public BaseClassHandler(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	protected ServiceResponse<T> parseServiceResponse(String response) throws GsonParsingException {
		// Create object using GSONParser
		T data = GSONParser.createObjectFromResponse(clazz, response);

		// Create new ServiceResponse object and set parsed data
		ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
		serviceResponse.setData(data);
		return serviceResponse;
	}

}