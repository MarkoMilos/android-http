package com.example.markom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.librarytest.R;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void openHandlerExamples(View view) {
		Intent intent = new Intent(this, ExampleHandlersActivity.class);
		startActivity(intent);
	}

	public void openRawResponseLoaderExamples(View view) {
		Intent intent = new Intent(this, RawResponseLoaderActivty.class);
		startActivity(intent);
	}

	public void openCustomObjectLoaderExamples(View view) {
		Intent intent = new Intent(this, CustomObjectLoaderActivity.class);
		startActivity(intent);
	}

	public void openCustomCollectionLoaderExamples(View view) {
		Intent intent = new Intent(this, CustomCollectionLoaderActivity.class);
		startActivity(intent);
	}

}