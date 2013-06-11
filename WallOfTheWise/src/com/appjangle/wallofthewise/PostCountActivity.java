package com.appjangle.wallofthewise;

import io.nextweb.Link;
import io.nextweb.Node;
import io.nextweb.Session;
import io.nextweb.fn.Closure;
import io.nextweb.fn.ExceptionListener;
import io.nextweb.fn.ExceptionResult;
import io.nextweb.jre.Nextweb;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.appjangle.demos.appjanglejavademo.Calculations;

public class PostCountActivity extends Activity {

	public static final String ARG_URL = "url";
	
	private static final String TAG = "NextWeb";
	
	
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
	
	
	/** Loads the post data from the given url and displays it in the list */
	private void loadContent(final String url){
		final Session session = Nextweb.createSession();
        try {
           
            Link posts = session.node(url);
            
            // Catch errors
            posts.catchExceptions(new ExceptionListener() {
                public void onFailure(ExceptionResult r) {
                	// TODO: Show error to user
                    Log.e(TAG, "Error", r.exception());
                }
            });

            posts.get(new Closure<Node>() {
                public void apply(Node n) {
                	final Calculations c = new Calculations(n.getSession());
                    final Map<String, Integer> results = c.calculatePostsPerUser(n);
                    
                    // Show the data to the user
                    runOnUiThread(new Runnable(){
                    	public void run(){
                    		showContent(results);
                    	}
                    });
                }
            });
        } catch (Throwable t) {
        	// TODO: Show error to user
            Log.e(TAG, "Error", t);
            finish();
        }
	}
	
	
	/** Shows the given name & posts data in a table */
	private void showContent(final Map<String, Integer> content){
		setContentView(R.layout.activity_post_count);
		final TableLayout table = (TableLayout) findViewById(R.id.tableRoot);
		final LayoutInflater inflater = getLayoutInflater();
		
		// add a row for each piece of data
		for(final String key:content.keySet()){
			final Integer count = content.get(key);
			
			// Create new row
			final TableRow row = (TableRow) inflater.inflate(R.layout.table_row_post_count, null);
			table.addView(row);
			final TextView name = (TextView) row.findViewById(R.id.name);
			final TextView posts = (TextView) row.findViewById(R.id.posts);
			
			// Set data
			name.setText(key);
			posts.setText(count.toString());
		}
	}
	
}
