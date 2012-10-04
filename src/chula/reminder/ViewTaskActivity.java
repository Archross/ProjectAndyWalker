package chula.reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Projection;

import chula.reminder.R;

import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewTaskActivity extends MapActivity {
	private TextView nameView;
	private TextView commentView;
	private TextView dateView;
	 private Spinner categorySpinnerView;
	 private SQLiteAdapter mySQLiteAdapter;
	 private GeoPoint loc;
	    private MapView gMap;
	    private MyLocationOverlay locOverlay;	 
	    
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.view_task);
	        mySQLiteAdapter = new SQLiteAdapter(this);
	        Bundle bundle = this.getIntent().getExtras();
	        //binding
	        nameView = (TextView)findViewById(R.id.vt_name);
	        categorySpinnerView = (Spinner)findViewById(R.id.vt_catSpinner);
	        commentView = (TextView)findViewById(R.id.vt_comment);
	        dateView = (TextView)findViewById(R.id.vt_date);
	        
	        //set value from intent
	        nameView.setText(bundle.getString("name"));
	       setSpinner(bundle.getInt("category"));
	       String commentTx = bundle.getString("comment")+", point = ("+bundle.getInt("latitude")+","+
	    		   bundle.getInt("longtitude")+")";
	    	//commentView.setText(bundle.getString("comment"));
	       commentView.setText(commentTx);
	    	Date d = new Date(bundle.getLong("date"));	    	
	    	dateView.setText(dateToString(d));
	        
	    	 gMap = (MapView) findViewById(R.id.gMap);
	         gMap.setBuiltInZoomControls(true);
	         locOverlay = new MyLocationOverlay(this,gMap);
	         locOverlay.enableCompass();
	         locOverlay.enableMyLocation();
	         gMap.getOverlays().add(locOverlay);
	    }
	 
	 private void setSpinner(int position) {
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
		categorySpinnerView.setAdapter(arrayAdapter);
		categorySpinnerView.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				// TODO Auto-generated method stub
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		categorySpinnerView.setSelection(position);
		 mySQLiteAdapter.close();
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

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
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
	    	Log.d("map",(String)loc.toString());
	    }
	    return result;

	}
}

