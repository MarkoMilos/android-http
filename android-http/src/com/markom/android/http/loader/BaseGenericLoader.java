package com.markom.android.http.loader;

import java.lang.reflect.Type;
import java.util.Collection;

import android.content.Context;

import com.google.gson.Gson;
import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.model.ServiceResponse;
import com.markom.android.http.parser.GSONParser;

/**
 * Extends {@link BaseGsonLoader} and enables parsing {@link Collection} data from json by providing {@link Type} of
 * parsed element. By default {@link BaseGsonLoader#parseServiceResponse(String)} methods try's to parse received
 * response with {@link GSONParser} immediately. In order to parse collection from custom json schema (example: meta,
 * pagination, data...), create custom {@link ServiceResponse} class and override this class to implement your own
 * parsing logic.
 * <p>
 * Example:
 * <p>
 * 
 * <pre>
 * public class ExampleActivity extends FragmentActivity implements LoaderCallbacks&#60;LoaderResponse&#60;List&#60;Person&#62;&#62; {
 * 
 *     &#064;Override
 *     public Loader&#60;LoaderResponse&#60;List&#60;Person&#62;&#62; onCreateLoader(int id, Bundle bundle) {
 *         Type type = new TypeToken&#60;List&#60;Person&#62;&#62;() {}.getType();
 *         BaseGenericLoader loader = new BaseGenericLoader(this, "www.example.com", type);
 *         // additional loader HTTP setup like request parameters, http method etc.
 *         return loader;
 *     }
 *     
 *     &#064;Override
 *     public void onLoadFinished(Loader&#60;LoaderResponse&#60;List&#60;Person&#62;&#62; loader, LoaderResponse&#60;List&#60;Person&#62;&#62; response) {
 *         if (response.isSuccess()) {
 *            List&#60;Person&#62; persons = response.getServiceResponse().getData();
 *            // Handle succes response
 *         } else {
 *            // Handle failure response
 *         }
 *     }
 *     
 *     &#064;Override
 *     public void onLoaderReset(Loader&#60;LoaderResponse&#60;List&#60;Person&#62;&#62; loader) {
 *     }
 *     
 * });
 * </pre>
 * 
 * 
 * @param <T> generic type of data encapsulated by {@link LoaderResponse}.
 * 
 * @author Marko Milos
 */
public class BaseGenericLoader<T> extends BaseGsonLoader<T> {

	/**
	 * Type of collection to be parsed. Needed by {@link Gson} in oreder to do parse.
	 */
	protected Type type;

	/**
	 * Creates new instance of {@link BaseGenericLoader} that is going to execute HTTP GET method with no url
	 * parameters. Use set methods provided by this class to customize http request.
	 * 
	 * @param context used by loader.
	 * @param url for HTTP request.
	 * @param type represents type of collection data. Needed by {@link Gson} while maping json to objects.
	 */
	public BaseGenericLoader(Context context, String url, Type type) {
		super(context, url);
		this.type = type;
	}

	@Override
	protected ServiceResponse<T> parseServiceResponse(String response) throws GsonParsingException {
		T data = GSONParser.createObjectListFromResponse(type, response);

		ServiceResponse<T> serviceResponse = new ServiceResponse<T>();
		serviceResponse.setData(data);
		return serviceResponse;
	}

}