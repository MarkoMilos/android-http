package com.example.markom;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librarytest.R;
import com.markom.android.http.loader.LoaderResponse;
import com.markom.android.http.loader.RawResponseLoader;

public class RawResponseLoaderActivty extends FragmentActivity implements LoaderCallbacks<LoaderResponse<String>> {

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
	public Loader<LoaderResponse<String>> onCreateLoader(int id, Bundle bundle) {
		RawResponseLoader loader = new RawResponseLoader(this, TestUrls.URL_SIMPLE_OBJECT);
		// additional loader HTTP setup
		return loader;

	}

	@Override
	public void onLoadFinished(Loader<LoaderResponse<String>> loader, LoaderResponse<String> result) {
		// Check result success
		if (result.isSuccess()) {
			tvResult.setText(result.getResponse());
		} else {
			Toast.makeText(getApplicationContext(), result.getError().getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onLoaderReset(Loader<LoaderResponse<String>> loader) {
	}

}