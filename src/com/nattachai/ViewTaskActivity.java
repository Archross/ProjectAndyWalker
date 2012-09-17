package com.nattachai;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class ViewTaskActivity extends Activity {
	private TextView nameView;
	private TextView categoryView;
	private TextView commentView;
	private TextView dateView;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.view_task);
	        
	        Bundle bundle = this.getIntent().getExtras();
	        //binding
	        nameView = (TextView)findViewById(R.id.vt_name);
	        categoryView = (TextView)findViewById(R.id.vt_cat);
	        commentView = (TextView)findViewById(R.id.vt_comment);
	        dateView = (TextView)findViewById(R.id.vt_date);
	        
	        //set value from intent
	        nameView.setText(bundle.getString("name"));
	        categoryView.setText(bundle.getString("category"));
	    	commentView.setText(bundle.getString("comment"));
	    	Date d = new Date(bundle.getLong("date"));	    	
	    	dateView.setText(dateToString(d));
	        
	    }
	 
	 private String dateToString(Date d){
		 try{
		 SimpleDateFormat formatter ; 
		  formatter = new SimpleDateFormat("dd-MMM-yy");
		  String s = formatter.format(d);
		  return s;
		  } catch (ParseException e)
		  {System.out.println("Exception :"+e);  
		  }
		return null;  
		 
	}
}
