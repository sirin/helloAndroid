package com.example.translate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;

public class Translate extends Activity {
	private Spinner fromSpinner;
	private Spinner toSpinner;
	private EditText origText;
	private TextView transText;
	private TextView retransText;
	private TextWatcher textWatcher;
	private OnItemSelectedListener itemListener;
	private Handler guiThread;
	private ExecutorService transThread;
	private Runnable updateTask;
	private Future transPending;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translate);
		initThreading();
		findViews();
		setAdapters();
		setListeners();
	}

	private void findViews() {
		fromSpinner = (Spinner) findViewById(R.id.from_language);
		toSpinner = (Spinner) findViewById(R.id.to_language);
		origText = (EditText) findViewById(R.id.original_text);
		transText = (TextView) findViewById(R.id.translated_text);
		retransText = (TextView) findViewById(R.id.retranslated_text);
	}
	
	private void setAdapters() {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.languages, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		fromSpinner.setAdapter(adapter);
		toSpinner.setAdapter(adapter);
		
		fromSpinner.setSelection(8);
		toSpinner.setSelection(30);
	}
	
	private void setListeners() {
		textWatcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				queueUpdate(1000);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		};
		itemListener = new OnItemSelectedListener() {
			public void onItemSelected(AdapterView parent, View v, int position, long id) {
				queueUpdate(200);
			}
			public void onNothingSelected(AdapterView parent) {
				
			}
		};
		origText.addTextChangedListener(textWatcher);
		fromSpinner.setOnItemSelectedListener(itemListener);
		toSpinner.setOnItemSelectedListener(itemListener);
	}
	
	private void initThreading() {
		guiThread = new Handler();
		transThread = Executors.newSingleThreadExecutor();
		updateTask = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String original = origText.getText().toString().trim();
				if (transPending != null)
					transPending.cancel(true);
				if (original.length() == 0) {
					transText.setText(R.string.empty);
					retransText.setText(R.string.empty);
				} else {
					transText.setText(R.string.translating);
					retransText.setText(R.string.translating);
					try {
						TranslateTask translateTask = new TranslateTask(
								Translate.this, original,
								getLang(fromSpinner),
								getLang(toSpinner));
						transPending = transThread.submit(translateTask);
					} catch (RejectedExecutionException e) {
						transText.setText(R.string.translation_error);
						retransText.setText(R.string.translation_error);
					}
					
				}
			}
		};
	}
	
	private String getLang(Spinner spinner) {
		String result = spinner.getSelectedItem().toString();
		int lparen = result.indexOf('(');
		int rparen = result.indexOf(')');
		result = result.substring(lparen+1, rparen);
		return result;
	}
	
	private void queueUpdate(long delayMillis) {
		guiThread.removeCallbacks(updateTask);
		guiThread.postDelayed(updateTask, delayMillis);
	}
	
	public void setTranslated(String text) {
		guiSetText(transText, text);
	}
	
	public void setRetranslated(String text) {
		guiSetText(retransText, text);
	}
	
	private void guiSetText(final TextView view, final String text) {
		guiThread.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				view.setText(text);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_translate, menu);
		return true;
	}

}
