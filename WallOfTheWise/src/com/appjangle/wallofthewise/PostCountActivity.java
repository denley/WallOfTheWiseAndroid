package com.appjangle.wallofthewise;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;

public class PostCountActivity extends Activity {

	public static String ARG_URL = "url";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// we require a URL argument
		final Intent intent = getIntent();
		if(intent==null || !intent.hasExtra(ARG_URL)){
			finish();
			return;
		}
		
		setContentView(R.layout.activity_post_count_loading);
		
		// Load the content from the url. Thread network connections
		new Thread(){
			public void run(){
				loadContent(intent.getStringExtra(ARG_URL));
			}
		}.start();
	}

	private void showContent(final HashMap<String, Integer> content){
		setContentView(R.layout.activity_post_count);
		final TableLayout table = (TableLayout) findViewById(R.id.tableRoot);
		
	}
	
	private void loadContent(final String url){
		
	}
	
	
	
}
