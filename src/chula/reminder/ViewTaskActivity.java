package chula.reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import chula.reminder.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewTaskActivity extends MapActivity {
	private TextView nameView;
	private TextView commentView;
	private TextView dateView;
	private TextView categoryView;
	 private SQLiteAdapter mySQLiteAdapter;
	  private GeoPoint loc;
	    private MapView gMap;
	    private MyLocationOverlay locOverlay;	 
	    private Itemization overlay;
	 private Context c;
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        c=this;
	        setContentView(R.layout.view_task);
	        mySQLiteAdapter = new SQLiteAdapter(this);
	        Bundle bundle = this.getIntent().getExtras();
	        //binding
	        nameView = (TextView)findViewById(R.id.vt_name);
	        categoryView = (TextView) findViewById(R.id.vt_category);
	        commentView = (TextView)findViewById(R.id.vt_comment);
	        dateView = (TextView)findViewById(R.id.vt_date);
	        
	        //set value from intent
	        nameView.setText(bundle.getString("name"));
	        
	       setSpinner(bundle.getInt("category"));
	    /*   String commentTx = bundle.getString("comment")+", point = ("+bundle.getInt("latitude")+","+
	    		   bundle.getInt("longtitude")+")";*/
	    	commentView.setText(bundle.getString("comment"));
	       //commentView.setText(commentTx);
	    	Date d = new Date(bundle.getLong("date"));	    	
	    	dateView.setText(dateToString(d));
	        
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
	    	 	 
	    	 	 Button back = (Button) findViewById(R.id.vt_backbutton);
	    	 	 back.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						 Intent intent= new Intent(c,ProjecttActivity.class);
						c.startActivity(intent);
					}
				});

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
	     categoryView.setText(categoryNameList.get(position)); 
	     
		
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

