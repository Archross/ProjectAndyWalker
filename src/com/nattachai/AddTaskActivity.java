package com.nattachai;

import java.sql.Date;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class AddTaskActivity extends Activity {
	 /** Called when the activity is first created. */
	
	   private EditText mDateDisplay;
	    private int mYear;
	    private int mMonth;
	    private int mDay;
	    private Spinner categorySpinner;
	    private String mName,mComment,mCategory;
	    static final int DATE_DIALOG_ID = 0;
	    
 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.add_task);
     final Context c = this;
     
     
     categorySpinner = (Spinner) findViewById(R.id.at_spinner1);
     mDateDisplay =  (EditText) findViewById(R.id.at_addTime);
     Button add = (Button) findViewById(R.id.at_addButton);
     Button cancel = (Button) findViewById(R.id.at_cancelButton);

     //mPickDate = (Button) findViewById(R.id.at_button1);
     setSpinner();
     // add a click listener to the button
     add.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			mName =((EditText)findViewById(R.id.at_addName)).getText().toString();
			((EditText)findViewById(R.id.at_addName)).setText("");
			mComment = ((EditText)findViewById(R.id.at_addComment)).getText().toString();
			((EditText)findViewById(R.id.at_addComment)).setText("");
			Task a =new Task(mName,mCategory,mComment,new Date(mYear,mMonth,mDay));
			SQLiteAdapter mySQLiteAdapter = new SQLiteAdapter(c);
	       mySQLiteAdapter.openToWrite();
	       mySQLiteAdapter.insert(a);
	        mySQLiteAdapter.close();
			chageView();
		}
	});
     cancel.setOnClickListener(new OnClickListener() {
 		
 		public void onClick(View v) {
 			// TODO Auto-generated method stub
 			((EditText)findViewById(R.id.at_addName)).setText("");
 			((EditText)findViewById(R.id.at_addComment)).setText("");
 			chageView();
 		}
 	});
     mDateDisplay.setOnClickListener(new View.OnClickListener() {
         public void onClick(View v) {
             showDialog(DATE_DIALOG_ID);
         }
     });

     // get the current date
     final Calendar ca = Calendar.getInstance();
     mYear = ca.get(Calendar.YEAR);
     mMonth = ca.get(Calendar.MONTH);
     mDay = ca.get(Calendar.DAY_OF_MONTH);

     // display the current date (this method is below)
     updateDisplay();
 }
 
 final public void chageView(){
	 Intent intent= new Intent(this,ProjecttActivity.class);
		this.startActivity(intent);
 }
 
 private void setSpinner() {
	// TODO Auto-generated method stub
	 ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
			  this, R.array.category, android.R.layout.simple_spinner_item );
			adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
	categorySpinner.setAdapter(adapter);
	categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long id) {
			// TODO Auto-generated method stub
			mCategory = (String) categorySpinner.getItemAtPosition(pos);
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	});
}

// updates the date in the TextView
 private void updateDisplay() {
     mDateDisplay.setText(getString(R.string.strSelectedDate,
         new StringBuilder()
                 // Month is 0 based so add 1
                 .append(mMonth + 1).append("-")
                 .append(mDay).append("-")
                 .append(mYear).append(" ")));
 }
 
 // the callback received when the user "sets" the date in the dialog
 private DatePickerDialog.OnDateSetListener mDateSetListener =
         new DatePickerDialog.OnDateSetListener() {

             public void onDateSet(DatePicker view, int year, 
                                   int monthOfYear, int dayOfMonth) {
                 mYear = year;
                 mMonth = monthOfYear;
                 mDay = dayOfMonth;
                 updateDisplay();
             }
         };
         
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}
	
	
 
}