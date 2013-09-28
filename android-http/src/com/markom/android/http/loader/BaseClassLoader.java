package com.markom.android.http.loader;

import android.content.Context;

import com.google.gson.Gson;
import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.model.ServiceResponse;
import com.markom.android.http.parser.GSONParser;

/**
 * Extends {@link BaseGsonLoader} and enables parsing an plain object data from json by providing {@link Class} of
 * parsed element. By default {@link BaseGsonLoader#parseServiceResponse(String)} methods try's to parse received
 * response with {@link GSONParser} immediately. In order to parse objects from custom json schema (example: meta,
 * pagination, data...), create custom {@link ServiceResponse} class and override this class to implement your own
 * parsing logic.
 * <p>
 * Example:
 * <p>
 * 
 * <pre>
 * public class ExampleActivity extends FragmentActivity implements LoaderCallbacks&#60;LoaderResponse&#60;Person&#62;&#62; {
 * 
 *     &#064;Override
 *     public Loader&#60;LoaderResponse&#60;Person&#62;&#62; onCreateLoader(int id, Bundle bundle) {
 *         BaseClassLoader loader = new BaseClassLoader(this, "www.example.com", Person.class);
 *         // additional loader HTTP setup like request parameters, http method etc.
 *         return loader;
 *     }
 *     
 *     &#064;Override
 *     public void onLoadFinished(Loader&#60;LoaderResponse&#60;Person&#62;&#62; loader, LoaderResponse&#60;Person&#62; response) {
 *         if (response.isSuccess()) {
 *            Person person = response.getServiceResponse().getData();
 *            // Handle succes response
 *         } else {
 *            // Handle failure response
 *         }
 *     }
 *     
 *     &#064;Override
 *     public void onLoaderReset(Loader&#60;LoaderResponse&#60;Person&#62;&#62; loader) {
 *     }
 *     
 * });
 * </pre>
 * 
 * @param <T> generic type of data encapsulated by {@link LoaderResponse}.
 * 
 * @author Marko Milos
 */
public class BaseClassLoader<T> extends BaseGsonLoader<T> {

	/**
	 * {@link Class} instance of object to be parsed. Needed by {@link Gson} in order to do parse.
	 */
	protected Class<T> clazz;

	/**
	 * Creates new instance of {@link BaseClassLoader} that is going to execute HTTP GET method with no url parameters.
	 * Use set methods provided by this class to customize http request.
	 * 
	 * @param context used by loader.
	 * @param url for HTTP request.
	 * @param clazz {@link Class} instance of object to be parsed. Needed by {@link Gson} in oreder to do parse.
	 */
	public BaseClassLoader(Context context, String url, Class<T> clazz) {
		super(context, url);
		this.clazz = clazz;
	}

	@Override
	protected ServiceResponse<T> parseServiceResponse(String response) throws GsonParsingException {
		T data = GSONParser.createObjectFromResponse(clazz, response);

		ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
		serviceResponse.setData(data);
		return serviceResponse;
	}

}