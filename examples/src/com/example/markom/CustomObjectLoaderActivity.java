package com.example.markom;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarytest.R;
import com.example.markom.http.loader.CustomClassLoader;
import com.example.markom.model.Person;
import com.markom.android.http.loader.LoaderResponse;

public class CustomObjectLoaderActivity extends FragmentActivity implements LoaderCallbacks<LoaderResponse<Person>> {

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
	public Loader<LoaderResponse<Person>> onCreateLoader(int id, Bundle bundle) {
		CustomClassLoader<Person> loader = new CustomClassLoader<Person>(this, TestUrls.URL_CUSTOM_OBJECT, Person.class);
		// additional loader HTTP setup
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<LoaderResponse<Person>> loader, LoaderResponse<Person> result) {
		// Check result success
		if (result.isSuccess()) {
			tvResult.setText(result.getResponse());
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