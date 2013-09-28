package com.example.markom;

import java.lang.reflect.Type;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarytest.R;
import com.example.markom.http.loader.CustomGenericLoader;
import com.example.markom.model.Person;
import com.google.gson.reflect.TypeToken;
import com.markom.android.http.loader.LoaderResponse;

public class CustomCollectionLoaderActivity extends FragmentActivity implements
		LoaderCallbacks<LoaderResponse<List<Person>>> {

	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loader_activity);

		tvResult = (TextView) findViewById(R.id.tvResult);
	}

	public void startLoaderRequest(View view) {
		getSupportLoaderManager().initLoader(0, null, this);
	}

	public void restartLoaderRequest(View view) {
		getSupportLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public Loader<LoaderResponse<List<Person>>> onCreateLoader(int id, Bundle bundle) {
		Type type = new TypeToken<List<Person>>() {
		}.getType();

		CustomGenericLoader<List<Person>> loader = new CustomGenericLoader<List<Person>>(this,
				TestUrls.URL_CUSTOM_COLLECTION, type);
		// additional loader HTTP setup
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<LoaderResponse<List<Person>>> loader, LoaderResponse<List<Person>> result) {
		// Check result success
		if (result.isSuccess()) {
			tvResult.setText(result.getResponse());
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