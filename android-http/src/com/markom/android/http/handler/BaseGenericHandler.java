package com.markom.android.http.handler;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.Gson;
import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.model.ServiceResponse;
import com.markom.android.http.parser.GSONParser;

/**
 * Extends {@link BaseGsonHandler} and enables parsing {@link Collection} data from json by providing {@link Type} of
 * parsing element. By default {@link BaseGsonHandler#parseServiceResponse(String)} methods try's to parse received
 * response with {@link GSONParser} immediately. In order to parse collection from custom json schema, create custom
 * {@link ServiceResponse} class and override this class to implement your own parsing logic.
 * <p>
 * Example:
 * 
 * <pre>
 * Type type = new TypeToken&lt;List&lt;Person&gt;&gt;() {
 * }.getType();
 * 
 * client.get(&quot;www.example.com&quot;, new BaseGenericHandler&lt;List&lt;Person&gt;&gt;(type) {
 * 
 * 	&#064;Override
 * 	public void onStart() {
 * 		// started
 * 	}
 * 
 * 	&#064;Override
 * 	public void onSuccess(int statusCode, Header[] headers, ServiceResponse&lt;List&lt;Person&gt;&gt; result) {
 * 		super.onSuccess(statusCode, headers, result);
 * 		List&lt;Person&gt; persons = result.getData();
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
public class BaseGenericHandler<T> extends BaseGsonHandler<T> {

	/**
	 * Type of collection to be parsed. Needed by {@link Gson} in oreder to do parse.
	 */
	protected Type type;

	/**
	 * Creates an new instance of {@link BaseGenericHandler}.
	 * 
	 * @param type the type of collection requested.
	 */
	public BaseGenericHandler(Type type) {
		this.type = type;
	}

	@Override
	protected ServiceResponse<T> parseServiceResponse(String response) throws GsonParsingException {
		// Create collection using GSONParser
		T data = GSONParser.createObjectListFromResponse(type, response);

		// Create new ServiceResponse object and set parsed data
		ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
		serviceResponse.setData(data);
		return serviceResponse;
	}

}