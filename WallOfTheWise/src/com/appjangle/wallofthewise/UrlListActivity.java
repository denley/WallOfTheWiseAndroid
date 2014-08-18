package com.appjangle.wallofthewise;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class UrlListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_url_list_new);
		
		final String[] items = getResources().getStringArray(R.array.url_list);
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, items));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch(position){
            case 0:
                startPostCount("http://slicnet.com/seed1/seed1/6/2/8/7/h/sd");
                break;
            case 1:
                startPostCount("http://slicnet.com/seed1/seed1/6/2/7/9/h/sd");
                break;
            case 2:
                startPostCount("http://slicnet.com/seed1/seed1/6/2/8/3/h/sd");
                break;
            case 3:
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
			input.setText(R.string.url_dialog_default);
			
			// Show url input dialog
			new AlertDialog.Builder(this)
			.setTitle(R.string.url_dialog_title)
			.setMessage(R.string.url_dialog_message)
			.setView(input)
			.setNegativeButton(android.R.string.cancel, null)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startPostCount(input.getText().toString());
				}
			})
			.show();
			break;
		default:

			break;
		}
	}
	
	/** Launches a new PostCountActivity for the given url */
	private void startPostCount(final String url){
		startActivity(new Intent(this, PostCountActivity.class)
		.putExtra(PostCountActivity.ARG_URL, url));
	}

}
