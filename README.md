#android-http

This project aims to provide a reusable instrument for asynchronous callback-based Http client with tools to convert JSON response to POJO's.
It is originally based on loopj android-async-http available at http://loopj.com/android-async-http/. 
GSON library is used for creating POJO's from JSON schema. Library is available at https://code.google.com/p/google-gson/.

##Features
--------
- Make **asynchronous** HTTP requests, handle responses in **anonymous callbacks**
- HTTP requests happen **outside the UI thread**
- Requests use a **threadpool** to cap concurrent resource usage
- GET/POST **params builder** (RequestParams)
- **Multipart file uploads** with no additional third party libraries
- Tiny size overhead to your application, only **19kb** for everything
- Automatic smart **request retries** optimized for spotty mobile connections
- Automatic **gzip** response decoding support for super-fast requests
- Optional built-in response parsing into **JSON** (JsonHttpResponseHandler)
- Optional **persistent cookie store**, saves cookies into your app's SharedPreferences
- SyncronusClient that executes requests in the same thread where it is called (useful for services, loaders etc.)
- Several callback handlers that provides raw response, json object or parsed POJO's
- Loader tools that process network request in background thread, can persist configuration changes (screen rotation) and return raw response or POJO's

## Quick Setup

#### 1. Include library in project

#### 2. Android Manifest
``` xml
<manifest>
	<uses-permission android:name="android.permission.INTERNET" />
	...
	<application android:name="MyApplication">
		...
	</application>
</manifest>
```

## Usage

#### 1. Basic:

Create a new AsyncHttpClient instance:
``` java
	AsyncHttpClient client = new AsyncHttpClient();
```

#### 2. Request with handlers

Make a request for simple response:
``` java
	client.get("http://www.google.com", new AsyncHttpResponseHandler() {
		@Override
		public void onSuccess(String response) {
			// Handle response
		}
	});
```

Make a request for JSONObject or JSONArray result with **JsonHttpResponseHandler**:
``` java
	client.get("www.example.url", null, new JsonHttpResponseHandler() {
		@Override
		public void onSuccess(JSONArray timeline) {
			// Parse json response
		}
	});
```

In order to use handler for POJO's as result first create appropriate objects per GSON documentation using JSON field naming support. Example:
``` java
	public class Person {

		@SerializedName("name")
		private String name;

		@SerializedName("age")
		private int age;

		// Empty constuctor needed by GSON
		public Person() {
			this.name = "";
			this.age = -1;
		}
	}
```

Use BaseClassHandler<T> for objects and BaseGenericHandler<T> for collections. Example:
``` java
	client.get("www.example.url", new BaseClassHandler<Person>(Person.class) {

		@Override
		public void onStart() {
		}

		@Override
		public void onSuccess(Person person) {
		}

		@Override
		public void onFailure(Throwable error, String content) {
		}

		@Override
		public void onFinish() {
		}
	});
```

or for collection

``` java
	Type type = new TypeToken<List<Person>>() {}.getType();
		
	client.get("www.example.url", new BaseGenericHandler<List<Person>>(type) {

		@Override
		public void onStart() {
		}

		@Override
		public void onSuccess(List<Person> result) {
		}

		@Override
		public void onFailure(Throwable error, String content) {
		}

		@Override
		public void onFinish() {
		}
	});
```

#### 3. Request with loaders

Common problem with android asyncronus networking is how to persist trough configuration changes like screen rotation.
This library contains loader tools that extends known android `AsyncTaskLoader` class and returns `LoaderResponse<T>` as result. 
`LoaderResponse<T>` contains requested T data as well as additional informations about request status.

Raw response loader (returns simple string response):
``` java
	public class RawResponseLoaderActivty extends FragmentActivity implements LoaderCallbacks<LoaderResponse<String>> {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.loader_activity);
			
			// Start loader
			getSupportLoaderManager().initLoader(0, null, this);
		}

		@Override
		public Loader<LoaderResponse<String>> onCreateLoader(int id, Bundle bundle) {
			RawResponseLoader loader = new RawResponseLoader(this, TestUrls.URL_SIMPLE_OBJECT);
			// additional loader HTTP setup
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<LoaderResponse<String>> loader, LoaderResponse<String> result) {
			// Check result success
			if (result.isSuccess()) {
				String response = result.getResponse();
				// Do something with response
			} else {
				Toast.makeText(getApplicationContext(), result.getError().getMessage(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onLoaderReset(Loader<LoaderResponse<String>> loader) {
		}
	}
```
POJO loader example:
``` java
	public class ObjectLoaderActivity extends FragmentActivity implements LoaderCallbacks<LoaderResponse<Person>> {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.loader_activity);
			
			// Start loader request
			getSupportLoaderManager().initLoader(0, null, this);
		}

		@Override
		public Loader<LoaderResponse<Person>> onCreateLoader(int id, Bundle bundle) {
			BaseClassLoader<Person> loader = new BaseClassLoader<Person>(this, "www.example.com", Person.class);
			// additional loader HTTP setup
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<LoaderResponse<Person>> loader, LoaderResponse<Person> result) {
			// Check result success
			if (result.isSuccess()) {
				Toast.makeText(getApplicationContext(), "Person name: " + result.getServiceResponse().getData().getName(),
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), result.getError().getMessage(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onLoaderReset(Loader<LoaderResponse<Person>> loader) {
		}
	}
```

Collection loader example:
``` java
	public class CollectionLoaderActivity extends FragmentActivity implements LoaderCallbacks<LoaderResponse<List<Person>>> {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.loader_activity);

			// Star loader request
			getSupportLoaderManager().initLoader(0, null, this);
		}

		@Override
		public Loader<LoaderResponse<List<Person>>> onCreateLoader(int id, Bundle bundle) {
			Type type = new TypeToken<List<Person>>() {}.getType();

			BaseGenericLoader<List<Person>> loader = new BaseGenericLoader<List<Person>>(this, "www.example.com", type);
			// additional loader HTTP setup
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<LoaderResponse<List<Person>>> loader, LoaderResponse<List<Person>> result) {
			// Check result success
			if (result.isSuccess()) {
				Toast.makeText(getApplicationContext(),
						"First person name: " + result.getServiceResponse().getData().get(0).getName(), Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), result.getError().getMessage(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void onLoaderReset(Loader<LoaderResponse<List<Person>>> loader) {
		}
	}
```

#### 4. HTTP request setup
While using handlers additional parameters to request can be set directly trough client methods. For example by calling `get(Context context, String url, Header[] headers, RequestParams params, AsyncHttpResponseHandler responseHandler)` method from `AsyncHttpClient` instance.
While using loaders parameters for request that is going to be executed must be set while creating loader. Example:
``` java
		@Override
		public Loader<LoaderResponse<Person>> onCreateLoader(int id, Bundle bundle) {
			RequestParams params = new RequestParams();
			params.put("key", "value");
			params.put("more", "data");

			BaseClassLoader<Person> loader = new BaseClassLoader<Person>(this, "www.example.com", Person.class);
			loader.setRequestParams(params);
			loader.setHttpMethod(HttpMethod.POST);
			loader.setContentType("application/json");
			...
			return loader;
		}
```



#### 5. HTTP client setup
AsyncHttpClient can be setup by using public methods directly, however while using loaders SyncHttpClient is used to process request. In order to setup some parameters for loader like timeout for example call loader methods like:
``` java
		@Override
		public Loader<LoaderResponse<Person>> onCreateLoader(int id, Bundle bundle) {
			BaseClassLoader<Person> loader = new BaseClassLoader<Person>(this, "www.example.com", Person.class);
			loader.setTimeout(15000);
			loader.setUserAgent("myuseragent");
			loader.setAuthentication("user", "pass");
			...
			return loader;
		}
```


// CUSTOM JSON SCHEMA SECTION TODO