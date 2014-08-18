package com.appjangle.wallofthewise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.appjangle.android.AppjangleAndroid;
import com.appjangle.demos.appjanglejavademo.Calculations;

import java.util.Map;

import de.mxro.async.Async;
import io.nextweb.Link;
import io.nextweb.Node;
import io.nextweb.Session;
import io.nextweb.common.Interval;
import io.nextweb.common.Monitor;
import io.nextweb.common.MonitorContext;
import io.nextweb.jre.Nextweb;
import io.nextweb.operations.callbacks.NodeListener;

import de.mxro.fn.*;
import de.mxro.async.callbacks.*;

import io.nextweb.common.*;

import io.nextweb.promise.NextwebPromise;
import io.nextweb.promise.exceptions.*;

public class PostCountActivity extends Activity {

	// Required argument for displaying this Activity
	public static final String ARG_URL = "url";

    private Session session;

    private NextwebPromise<Monitor> monitor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        assert session == null;

		// we require a URL argument
		final Intent intent = getIntent();
		if(intent==null || !intent.hasExtra(ARG_URL)){
			finish();
			return;
		}

        session = AppjangleAndroid.createSession(this.getApplicationContext());

		setContentView(R.layout.activity_post_count_loading);
		
		// Load the content from the url
		loadContent(intent.getStringExtra(ARG_URL));

	}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        monitor.get().stop().get();
        session.close().get();
        session = null;
        monitor = null;
    }

    /** Loads the post data from the given url and displays it in the list */
	private void loadContent(final String url){

        try {
           
            final Link posts = session.node(url);
            
            // Catch errors
            posts.catchExceptions(new ExceptionListener() {
                public void onFailure(ExceptionResult r) {
                	showError(r.exception());
                }
            });

            // Data callback
            posts.get(new Closure<Node>() {
                public void apply(final Node n) {
                	loadContent(n, new SimpleCallback() {
                        @Override
                        public void onSuccess() {
                            installMonitor(n);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            showError(throwable);
                        }
                    });

                }
            });
        } catch (Throwable t) {
        	showError(t);
        }
	}
	
	/** Shows an error to the user. Can be called from any thread. */
	private void showError(final Throwable t){
		runOnUiThread(new Runnable(){
			public void run(){
				Log.e("NextWeb", "Error", t);
				final TextView error = new TextView(PostCountActivity.this);
				Throwable cause = t;
				while(cause.getCause()!=null){
					cause = cause.getCause();
				}
				
				error.setText("Error loading data:\n"+cause.getMessage());
		    	setContentView(error);
			}
		});
	}
	
	/** Sets up a monitor to update the view when new posts are added */
	private void installMonitor(final Node posts) {
        assert monitor == null;
	    monitor = session.node(posts).monitor().setInterval(Interval.FAST)
	                                        .setDepth(2)
	                                        .addListener(new NodeListener() {

	        public void onWhenNodeChanged(MonitorContext context) {
	            posts.reload(2).get(new Closure<Node>() {
	                @Override
	                public void apply(Node n) {
	                     loadContent(posts, Async.doNothing());
	                }
	            });
	        }
	    });
        monitor.get(new Closure<Monitor>() {
            public void apply(Monitor m) {
            }
        });
	}
	
	/** Loads and then displays the post count for the data contained in the given node */
	private void loadContent(final Node n, final SimpleCallback callback){
		// Thread network connections
    	new Thread(){
    		public void run(){
    			final Calculations c = new Calculations(n.getSession());
                final Map<String, Integer> results = c.calculatePostsPerUser(n);
                
                // Show the data to the user
                loadContent(results, callback);
    		}
    	}.start();
	}
	
	
	/** Shows the given name & posts data in a table. Can be called from any thread */
	private void loadContent(final Map<String, Integer> content, final SimpleCallback callback){
		runOnUiThread(new Runnable(){
        	public void run(){
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

                callback.onSuccess();
        	}
        });
	}
	
}
