package chula.reminder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import chula.reminder.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class AddTaskActivity extends MapActivity implements LocationListener {
	 /** Called when the activity is first created. */
	private SQLiteAdapter mySQLiteAdapter ;
	   private EditText mDateDisplay;
	    private int mYear;
	    private int mDay,mMonth;
	    private int mCategory;
	    private Spinner categorySpinner;
	    private String mName,mComment;
	    static final int DATE_DIALOG_ID = 0;
	    private GeoPoint loc;
	    private MapView gMap;
	    private MyLocationOverlay locOverlay;	 
	    private Itemization overlay;
	    
	    static int MARKER_WIDTH = 30;
	    static int MARKDER_HEIGHT = 50;
	    
 @Override
 public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.add_task);
     final Context c = this;
 	mySQLiteAdapter = new SQLiteAdapter(c);
 	//addSomeCategory();
     categorySpinner = (Spinner) findViewById(R.id.at_spinner1);
     mDateDisplay =  (EditText) findViewById(R.id.at_addTime);
     mDateDisplay.setKeyListener(null);
     Button add = (Button) findViewById(R.id.at_addButton);
     Button cancel = (Button) findViewById(R.id.at_cancelButton);
     gMap = (MapView) findViewById(R.id.gMap);
     gMap.setBuiltInZoomControls(true);
     locOverlay = new MyLocationOverlay(this,gMap);
     locOverlay.enableCompass();
     locOverlay.enableMyLocation();
     gMap.getOverlays().add(locOverlay);
     Drawable drawable = this.getResources().getDrawable(R.drawable.marker1);
     overlay = new Itemization(drawable,this);
     Projection projection = gMap.getProjection();
 	int y = gMap.getHeight() / 2; 
 	int x = gMap.getWidth() / 2;
 	 GeoPoint center = projection.fromPixels(x, y);
 	 OverlayItem overlayitem = new OverlayItem(center,"Center","Center of the map");
 	 overlay.addOverlay(overlayitem);
 	 gMap.getOverlays().add(overlay);

     
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
			Task a =new Task(mName,mCategory,mComment,new Date(mYear,mMonth,mDay),loc.getLatitudeE6(),loc.getLongitudeE6());
		
	       mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYTASK_TABLE);
	       mySQLiteAdapter.insertTask(a);
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
     
    /* mapCurrent.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			LocationManager locmanager = (LocationManager) c.getSystemService(LOCATION_SERVICE);
			Location lastknownLocation =locmanager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			while(lastknownLocation==null){
			}
			 int lat = (int) (lastknownLocation.getLatitude() * 1E6);
		     int lng = (int) (lastknownLocation.getLongitude() * 1E6);
		     loc = new GeoPoint(lat, lng);
		     Dialog d = new Dialog(c);
		     d.setTitle("O");
		     d.show();
		}
	});*/
     // display the current date (this method is below)
     updateDisplay();
 }
 
 private void addSomeCategory() {
	// TODO Auto-generated method stub
	mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYCATEGORY_TABLE);
	mySQLiteAdapter.insertCategory("Home");
	mySQLiteAdapter.insertCategory("School");
	mySQLiteAdapter.close();
}

final public void chageView(){
	 Intent intent= new Intent(this,ProjecttActivity.class);
		this.startActivity(intent);
 }
 
 private void setSpinner() {
	// TODO Auto-generated method stub
	 mySQLiteAdapter.openToRead(mySQLiteAdapter.MYCATEGORY_TABLE);
     //   startManagingCursor(cursor);
	 ArrayList<Category> categoryList = mySQLiteAdapter.queueAllCategory();
	 ArrayList<String> categoryNameList = new ArrayList<String>(10);
	 for (int i = 0; i <categoryList.size(); i++) {
		categoryNameList.add(categoryList.get(i).getName());
	}
	
      
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
        	(this,android.R.layout.simple_spinner_item, categoryNameList);
	categorySpinner.setAdapter(arrayAdapter);
	categorySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
				long id) {
			// TODO Auto-generated method stub
			mCategory = pos;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	});
	
	 mySQLiteAdapter.close();
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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		System.out.println(location);
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
	    boolean result = super.dispatchTouchEvent(event);
	    if (event.getAction() == MotionEvent.ACTION_UP){
	    	Projection projection = gMap.getProjection();
	    	int y = gMap.getHeight() / 2; 
	    	int x = gMap.getWidth() / 2;

	    	loc = projection.fromPixels(x, y);
	    	gMap.getOverlays().clear();
	    	 Drawable drawable = this.getResources().getDrawable(R.drawable.marker1);
	 
	         overlay = new Itemization(drawable,this);
	     	 OverlayItem overlayitem = new OverlayItem(loc,"Center","Center of the map");
	     	 overlay.addOverlay(overlayitem);
	     	 gMap.getOverlays().add(overlay);
		    gMap.postInvalidate();

	    }
	    	
	    return result;
	}
 
}