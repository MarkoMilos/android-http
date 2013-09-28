package com.markom.android.http.loader;

import android.content.Context;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.model.ServiceResponse;

/**
 * Use this loader in order to obitain raw string result from HTTP response. In order to receive result it is needed to
 * implement {@link LoaderCallbacks} interface with {@link LoaderResponse} type that have {@link String} as generic
 * parameter.
 * <p>
 * Example:
 * <p>
 * 
 * <pre>
 * public class ExampleActivity extends FragmentActivity implements LoaderCallbacks&#60;LoaderResponse&#60;String&#62;&#62; {
 * 
 *     &#064;Override
 *     public Loader&#60;LoaderResponse&#60;String&#62;&#62; onCreateLoader(int id, Bundle bundle) {
 *         RawResponseLoader loader = new RawResponseLaoder(this, url);
 *         // additional loader HTTP setup
 *         return loader;
 *     }
 *     
 *     &#064;Override
 *     public void onLoadFinished(Loader&#60;LoaderResponse&#60;String&#62;&#62; loader, LoaderResponse&#60;String&#62; response) {
 *         // Do something with string response
 *     }
 *     
 *     &#064;Override
 *     public void onLoaderReset(Loader&#60;LoaderResponse&#60;String&#62;&#62; loader) {
 *     }
 *     
 * });
 * </pre>
 * 
 * @author Marko Milos
 */
public class RawResponseLoader extends BaseGsonLoader<String> {

	/**
	 * @see BaseGsonLoader
	 */
	public RawResponseLoader(Context context, String url) {
		super(context, url);
	}

	@Override
	protected ServiceResponse<String> parseServiceResponse(String response) throws GsonParsingException {
		// We only need response string as data so set passed response as data
		ServiceResponse<String> serviceResponse = new ServiceResponse<String>();
		serviceResponse.setData(response);
		return serviceResponse;
	}

}