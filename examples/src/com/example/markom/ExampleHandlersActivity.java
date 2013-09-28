package com.example.markom;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarytest.R;
import com.example.markom.http.handler.CustomClassHandler;
import com.example.markom.http.handler.CustomGenericHandler;
import com.example.markom.http.schema.CustomServiceResponse;
import com.example.markom.model.Person;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.markom.android.http.handler.BaseClassHandler;
import com.markom.android.http.handler.BaseGenericHandler;
import com.markom.android.http.model.ServiceResponse;

public class ExampleHandlersActivity extends Activity {

	private AsyncHttpClient client;
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.example_handlers_activity);

		tvResult = (TextView) findViewById(R.id.tvResponse);
		client = new AsyncHttpClient();
	}

	public void executeSimpleObjectRequest(View view) {
		client.get(TestUrls.URL_SIMPLE_OBJECT, new BaseClassHandler<Person>(Person.class) {

			@Override
			public void onStart() {
				super.onStart();
			}

			// OVERRIDE ANY onSucesss METHOD

			@Override
			public void onSuccess(String content) {
				tvResult.setText(content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, ServiceResponse<Person> result) {
				super.onSuccess(statusCode, headers, result);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

		});
	}

	public void executeSimpleCollectionRequest(View view) {
		Type type = new TypeToken<List<Person>>() {
		}.getType();

		client.get(TestUrls.URL_SIMPLE_COLLECTION, new BaseGenericHandler<List<Person>>(type) {

			@Override
			public void onStart() {
				super.onStart();
			}

			// OVERRIDE ANY onSucesss METHOD

			@Override
			public void onSuccess(String content) {
				tvResult.setText(content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, ServiceResponse<List<Person>> result) {
				super.onSuccess(statusCode, headers, result);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

		});
	}

	public void executeCustomSchemaObjectRequest(View view) {
		client.get(TestUrls.URL_CUSTOM_OBJECT, new CustomClassHandler<Person>(Person.class) {

			@Override
			public void onStart() {
				super.onStart();
			}

			// OVERRIDE ANY onSucesss METHOD

			@Override
			public void onSuccess(String content) {
				tvResult.setText(content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, ServiceResponse<Person> result) {
				// Cast to CustomServiceResponse
				CustomServiceResponse<Person> customServiceResponse = (CustomServiceResponse<Person>) result;
				Toast.makeText(getApplicationContext(), "Person name: " + customServiceResponse.getData().getName(),
						Toast.LENGTH_LONG).show();
				super.onSuccess(statusCode, headers, result);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

		});
	}

	public void executeCustomSchemaCollectionRequest(View view) {
		Type type = new TypeToken<List<Person>>() {
		}.getType();

		client.get(TestUrls.URL_CUSTOM_COLLECTION, new CustomGenericHandler<List<Person>>(type) {

			@Override
			public void onStart() {
				super.onStart();
			}

			// OVERRIDE ANY onSucesss METHOD

			@Override
			public void onSuccess(String content) {
				tvResult.setText(content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, ServiceResponse<List<Person>> result) {
				// Cast to CustomServiceResponse
				CustomServiceResponse<List<Person>> customServiceResponse = (CustomServiceResponse<List<Person>>) result;
				Toast.makeText(getApplicationContext(),
						"First Person name: " + customServiceResponse.getData().get(0).getName(), Toast.LENGTH_LONG)
						.show();
				super.onSuccess(statusCode, headers, result);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

		});
	}

}