package com.markom.android.http.loader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.conn.ssl.SSLSocketFactory;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.markom.android.http.exceptions.GsonParsingException;
import com.markom.android.http.exceptions.ParsingNotImplementedException;
import com.markom.android.http.model.HttpMethod;
import com.markom.android.http.model.ServiceResponse;
import com.markom.android.http.parser.GSONParser;

/**
 * Extends {@link AsyncTaskLoader} and represent base Gson loader class that returns {@link LoaderResponse} object as
 * loader result data.
 * <p>
 * Extending this abstract class requires implementing {@link BaseGsonLoader#parseServiceResponse(String)} method that
 * returns {@link ServiceResponse} or any of it's subclasses. Basic parsing is implemented inside of
 * {@link BaseClassLoader} for simple objects or {@link BaseGenericLoader} for collection's. For custom json schema
 * extend {@link ServiceResponse} class and either {@link BaseClassLoader} or {@link BaseGenericLoader}, and override
 * parsing method.
 * 
 * @param <T> generic type of data encapsulated by {@link LoaderResponse}.
 * 
 * @see {@link BaseClassLoader}, {@link BaseGenericLoader}, {@link LoaderResponse}
 * 
 * @author Marko Milos
 */
public abstract class BaseGsonLoader<T> extends AsyncTaskLoader<LoaderResponse<T>> {

	private SyncHttpClient syncHttpClient;
	private String url;
	private RequestParams requestParams;
	private HttpMethod httpMethod;
	private Header[] headers;
	private HttpEntity entity;
	private String contentType;

	// Response informations
	private String errorResponse;
	private Throwable errorThrowable;
	private LoaderResponse<T> data;

	/**
	 * Creates new instance of {@link BaseGsonLoader} that is going to execute HTTP GET method with no url parameters.
	 * Use set methods provided by this class to customize http request.
	 * 
	 * @param context used by loader.
	 * @param url for HTTP request.
	 */
	public BaseGsonLoader(Context context, String url) {
		super(context);

		this.syncHttpClient = new SyncHttpClient() {

			@Override
			public String onRequestFailed(Throwable error, String content) {
				errorThrowable = error;
				errorResponse = content;
				return null;
			}
		};

		this.url = url;
		this.requestParams = null;
		this.httpMethod = HttpMethod.GET;
		this.headers = null;
		this.entity = null;
		this.contentType = null;
	}

	/**
	 * Set url request parameters.
	 * 
	 * @param requestParams additional parameters to send with the request.
	 */
	public void setRequestParameters(RequestParams requestParams) {
		this.requestParams = requestParams;
	}

	/**
	 * Set HTTP method that will be exectued by loader.
	 * 
	 * @param httpMethod {@link HttpMethod}.
	 */
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * Set headers for this request.
	 * 
	 * @param headers
	 */
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	/**
	 * Set request entity.
	 * 
	 * @param entity a raw {@link HttpEntity} to send with the request, for example, use this to send string/json/xml
	 *            payloads to a server by passing a {@link org.apache.http.entity.StringEntity}.
	 */
	public void setEntity(HttpEntity entity) {
		this.entity = entity;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * Get syncronus client. Syncronus client is since everything happens in loader thread.
	 * 
	 * @return {@link SyncHttpClient} used by loader.
	 */
	public SyncHttpClient getSyncClient() {
		return syncHttpClient;
	}

	/**
	 * Sets an optional CookieStore to use when making requests
	 * 
	 * @param cookieStore The CookieStore implementation to use, usually an instance of {@link PersistentCookieStore}
	 */
	public void setCookieStore(CookieStore cookieStore) {
		syncHttpClient.setCookieStore(cookieStore);
	}

	/**
	 * Sets the User-Agent header to be sent with each request. By default,
	 * "Android Asynchronous Http Client/VERSION (http://loopj.com/android-async-http/)" is used.
	 * 
	 * @param userAgent the string to use in the User-Agent header.
	 */
	public void setUserAgent(String userAgent) {
		syncHttpClient.setUserAgent(userAgent);
	}

	/**
	 * Sets the connection time oout. By default, 10 seconds
	 * 
	 * @param timeout the connect/socket timeout in milliseconds
	 */
	public void setTimeout(int timeout) {
		syncHttpClient.setTimeout(timeout);
	}

	/**
	 * Sets the SSLSocketFactory to user when making requests. By default, a new, default SSLSocketFactory is used.
	 * 
	 * @param sslSocketFactory the socket factory to use for https requests.
	 */
	public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
		syncHttpClient.setSSLSocketFactory(sslSocketFactory);
	}

	/**
	 * Sets headers that will be added to all requests this client makes (before sending).
	 * 
	 * @param header the name of the header
	 * @param value the contents of the header
	 */
	public void addHeader(String header, String value) {
		syncHttpClient.addHeader(header, value);
	}

	/**
	 * Sets basic authentication for the request. Uses AuthScope.ANY. This is the same as
	 * setBasicAuth('username','password',AuthScope.ANY)
	 * 
	 * @param username
	 * @param password
	 */
	public void setAuthentication(String username, String password) {
		syncHttpClient.setBasicAuth(username, password);
	}

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
	 * Helper function to take care of releasing resources associated with an actively loaded data set.
	 */
	protected void onReleaseResources(LoaderResponse<T> data) {
		// For a simple list or object there is nothing to do but for a Cursor we would close it here
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();

		// AsyncTaskLoader doesn't start unless you forceLoad
		// http://code.google.com/p/android/issues/detail?id=14944
		if (data != null) {
			deliverResult(data);
		}

		if (takeContentChanged() || data == null) {
			forceLoad();
		}
	}

	@Override
	public LoaderResponse<T> loadInBackground() {
		final LoaderResponse<T> loaderResponse = new LoaderResponse<T>();

		// Execute appropirate HTTP method for response
		String response = null;
		switch (httpMethod) {
		case GET:
			response = syncHttpClient.get(url, headers, requestParams);
			break;

		case POST:
			// Check if paremeters are passed as url params or body entity
			if (requestParams != null) {
				response = syncHttpClient.post(url, headers, requestParams, contentType);
			} else {
				response = syncHttpClient.post(url, headers, entity, contentType);
			}

			break;

		case PUT:
			// Check if paremeters are passed as url params or body entity
			if (requestParams != null) {
				response = syncHttpClient.put(url, requestParams);
			} else {
				response = syncHttpClient.put(url, headers, entity, contentType);
			}

			break;

		case DELETE:
			response = syncHttpClient.delete(url, headers);
			break;
		}

		// Check if there is any response. Null is only if request fails and onRequestFailed gets called
		if (response != null) {
			ServiceResponse<T> serviceResponse;
			try {
				// Try to parse service response data
				serviceResponse = parseServiceResponse(response);

				// Check if there is existing service response object
				if (serviceResponse != null) {

					// Check if there is existing data enapsulated by service response object
					// data can be null when empty string is retuned as response ""
					if (serviceResponse.getData() != null) {
						// Everything went well, return succesful loader response result
						loaderResponse.setSuccess(true);
						loaderResponse.setError(null);
					} else {
						// There is no data. Error happened during parsing process trigger error
						loaderResponse.setSuccess(false);
						loaderResponse.setError(new GsonParsingException());
					}
				} else {
					// ServiceResponse is null, parsing is not implemented trigger error
					loaderResponse.setSuccess(false);
					loaderResponse.setError(new ParsingNotImplementedException());
				}

				// Data needed in every scenario
				loaderResponse.setServiceResponse(serviceResponse);
				loaderResponse.setHttpStatusCode(syncHttpClient.getResponseCode());
				loaderResponse.setResponse(response);

			} catch (GsonParsingException e) {
				// Error triggered while parsing response, setup failure loader response
				loaderResponse.setServiceResponse(null);
				loaderResponse.setSuccess(false);
				loaderResponse.setHttpStatusCode(syncHttpClient.getResponseCode());
				loaderResponse.setError(e);
				loaderResponse.setResponse(response);
			}
		} else {
			// Called when request fails, setup loader response to return
			loaderResponse.setSuccess(false);
			loaderResponse.setHttpStatusCode(syncHttpClient.getResponseCode());
			loaderResponse.setError(errorThrowable);
			loaderResponse.setResponse(errorResponse);
			loaderResponse.setServiceResponse(null);
		}

		return loaderResponse;
	}

	@Override
	public void deliverResult(LoaderResponse<T> data) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We don't need the result
			if (this.data != null) {
				onReleaseResources(this.data);
			}

			// http://stackoverflow.com/questions/7474756/onloadfinished-not-called-after-coming-back-from-a-home-button-press
			return;
		}

		LoaderResponse<T> oldData = this.data;
		this.data = data;

		if (isStarted()) {
			super.deliverResult(data);
		}

		// At this point we can release the resources associated with 'oldPackage' if needed now that the new result is
		// delivered we know that it is no longer in use
		if (oldData != null) {
			onReleaseResources(oldData);
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	public void onCanceled(LoaderResponse<T> data) {
		super.onCanceled(data);
		onReleaseResources(data);
	}

	@Override
	protected void onReset() {
		super.onReset();

		// ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated if needed
		if (this.data != null) {
			onReleaseResources(this.data);
			this.data = null;
		}
	}

}