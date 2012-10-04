package chula.reminder;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;

import chula.reminder.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ProjecttActivity extends Activity implements OnClickListener,LocationListener {
	public final int DELETE_ID = 0;
	public final int DELETE_CAT_ID = 1;
	public final int EDIT_CAT_ID = 2;
	public final int CATEGORY_FILTER_ID = 10;
	public final int LOCATION_FILTER_ID= 20;
	private SQLiteAdapter mySQLiteAdapter;
	private  ListView listContent,categoryListView ;
	private Context cc;
	private  ArrayList<Task> taskList;
	private ArrayAdapter<String> arrayAdapter,categoryAdapter;
	private ArrayList<String> nameList,categoryListName ;
	private ArrayList<Category> categoryList ;
	 private Dialog dlg,addCategory,editCategory;
	 private Location currentLocation;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cc = this;
      
        listContent = (ListView) findViewById(R.id.contentlist);
        mySQLiteAdapter = new SQLiteAdapter(this);
        nameList = fillData();
        fillCategory();
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nameList);
        listContent.setAdapter(arrayAdapter); 
        Button b = (Button) findViewById(R.id.m_button1);
        b.setOnClickListener(this);
       
        listContent.setOnItemClickListener(listItemListener);
        listContent.setOnCreateContextMenuListener(this);
       
     
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	// TODO Auto-generated method stub
    	super.onCreateContextMenu(menu, v, menuInfo);
    	if(v.equals(listContent)){
    		menu.add(0,DELETE_ID,0,"Delete");
    	}else if(v.equals(categoryListView)){
    		 MenuItem delete = menu.add("Delete");
             MenuItem edit= menu.add("Edit");
             edit.setIcon(android.R.drawable.ic_menu_upload); //adding icons
             delete.setIcon(android.R.drawable.ic_menu_upload);
             AdapterContextMenuInfo menuInfos = (AdapterContextMenuInfo) menuInfo;
             final int pos = menuInfos.position;
       
             delete.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
            	
            	mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYTASK_TABLE);
        		for (int i = 0; i < taskList.size(); i++) {
        			System.out.println("Category num ="+pos+", task cat num ="+taskList.get(i).getCategory());
					if(taskList.get(i).getCategory()==pos){	
						mySQLiteAdapter.delete_byID(mySQLiteAdapter.MYTASK_TABLE,taskList.get(i).getId());
						String temp = taskList.get(i).getName();
				    	arrayAdapter.remove(temp); 
				    		
					}
				}  
        		mySQLiteAdapter.close();
        		mySQLiteAdapter.openToRead(mySQLiteAdapter.MYTASK_TABLE);
        		taskList= mySQLiteAdapter.getTasKList();
        		mySQLiteAdapter.close();
        		arrayAdapter.notifyDataSetChanged();
        		
            	mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYCATEGORY_TABLE);
        		mySQLiteAdapter.delete_byID(mySQLiteAdapter.MYCATEGORY_TABLE,categoryList.get(pos).getId());
        		mySQLiteAdapter.close();
        		String temp = categoryList.get(pos).getName();
        		categoryList.remove(pos);
        		categoryListName.remove(pos);
        		categoryAdapter.remove(temp);
        		categoryAdapter.notifyDataSetChanged();
        		
        		
                return true;
            }
        });
             
        edit.setOnMenuItemClickListener(new OnMenuItemClickListener() {
        	
            public boolean onMenuItemClick(MenuItem item) {
            	((AutoCompleteTextView)(editCategory.findViewById(R.id.editCategoryTxt))).
            	setText(categoryList.get(pos).getName());
            	editCategory.show();
            	Button ecb =  (Button) editCategory.findViewById(R.id.editCategoryButton);
       		 ecb.setOnClickListener(new OnClickListener() {
       			
       			public void onClick(View v) {
       				// TODO Auto-generated method stub
       				int id = categoryList.get(pos).getId();
       				String name = ((AutoCompleteTextView)(editCategory.findViewById(R.id.editCategoryTxt))).getText().toString();
       				((AutoCompleteTextView)(editCategory.findViewById(R.id.editCategoryTxt))).setText("");
       				mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYCATEGORY_TABLE);
                   	mySQLiteAdapter.updateCategory(name, id);
               		mySQLiteAdapter.close();
               		String temp =categoryList.get(pos).getName();
               		categoryList.get(pos).setName(name);
               		categoryAdapter.remove(temp);
               		categoryAdapter.remove(getResources().getString(R.string.add_category));
               		categoryAdapter.add(name);
               		categoryAdapter.add(getResources().getString(R.string.add_category));
               		categoryAdapter.notifyDataSetChanged();
                   	 
       				editCategory.dismiss();
       			}
       		});
       		 
                return true;
            }
        });
    	}
    }
    
   
    
   
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
    	int pos;
    	String temp;
    	switch(item.getItemId()){
    		
    	case DELETE_ID : 
    		pos = menuInfo.position;
    		mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYTASK_TABLE);
    		mySQLiteAdapter.delete_byID(mySQLiteAdapter.MYTASK_TABLE,taskList.get(pos).getId());
    		mySQLiteAdapter.close();
    		 temp = taskList.get(pos).getName();
    		taskList.remove(pos);
    		arrayAdapter.remove(temp);
    		arrayAdapter.notifyDataSetChanged();
    		return true;
    	}
    	
    	return super.onContextItemSelected(item);
    }

    private ArrayList<String> fillData(){
    	 mySQLiteAdapter.openToRead(mySQLiteAdapter.MYTASK_TABLE);
         taskList = mySQLiteAdapter.getTasKList();
         mySQLiteAdapter.close();
         ArrayList<String>temp = new ArrayList<String>(10);
         for (int i = 0; i < taskList.size(); i++) {
 			temp.add(taskList.get(i).getName());
 		}
         return temp;
    }
    
   
    private void fillCategory(){
    	mySQLiteAdapter.openToRead(mySQLiteAdapter.MYCATEGORY_TABLE);
        categoryList = mySQLiteAdapter.queueAllCategory();
        categoryListName = new ArrayList<String>(10); 
        mySQLiteAdapter.close();
        for (int i = 0; i < categoryList.size(); i++) {
 			categoryListName.add(categoryList.get(i).getName());
 		}
        categoryListName.add(getString(R.string.add_category));
        dlg = new Dialog(cc);
		dlg.setTitle("Filter by category");
		dlg.setContentView(R.layout.category_list);
		
		addCategory = new Dialog(cc);
		addCategory.setTitle("Enter new category name.");
		addCategory.setContentView(R.layout.add_category_dialog);
		
		editCategory = new Dialog(cc);
		editCategory.setTitle("Enter edit category name.");
		editCategory.setContentView(R.layout.edit_category_dialog);
		
		 categoryListView =(ListView) dlg.findViewById(R.id.categoryList);
		 
		 categoryAdapter = new ArrayAdapter<String>(dlg.getContext(),android.R.layout.simple_list_item_1, categoryListName);
		 categoryListView.setAdapter(categoryAdapter);
		 categoryListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				//dlg.setTitle((String)categoryList.get(pos));
				if(pos<categoryListName.size()-1){
					filterByCategory(pos);
				}else if(pos==categoryListName.size()-1){
					categoryListName.remove(pos);
					addCategory.show();
				}
				dlg.dismiss();
			}
		});
		 registerForContextMenu(categoryListView);
		 categoryListView.setOnCreateContextMenuListener(this);
		
		 Button acb = (Button) addCategory.findViewById(R.id.addCategoryButton);
		 acb.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYCATEGORY_TABLE);
				 String newName =((AutoCompleteTextView)(addCategory.findViewById(R.id.addCategoryTxt))).getText().toString();
				mySQLiteAdapter.insertCategory(newName);
				categoryAdapter.add(newName);
				((AutoCompleteTextView)(addCategory.findViewById(R.id.addCategoryTxt))).setText("");
				addCategory.dismiss();
				mySQLiteAdapter.close();
				categoryListName.add(getResources().getString(R.string.add_category));
				
			}
		});
		 
		 
   }
    
   
    private void filterByCategory(int pos) {
		// TODO Auto-generated method stub
    	arrayAdapter.clear();
    	for(int i=0;i<taskList.size();i++){
    		Task temp = taskList.get(i);
    		if(temp.getCategory()==pos){
    			arrayAdapter.add(temp.getName());
    		}
    	}
		arrayAdapter.notifyDataSetChanged();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0,CATEGORY_FILTER_ID,0,"Category");
      menu.add(0,LOCATION_FILTER_ID,0,"Location");
      return super.onCreateOptionsMenu(menu);
    }
    
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	switch (item.getItemId()) {
		case CATEGORY_FILTER_ID:
			dlg.show();
			break;
		case LOCATION_FILTER_ID:
			break;

		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }
    
    public
	
    OnItemClickListener listItemListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> pView, View cView, int pos,long id) {
			// TODO Auto-generated method stub
			Intent intent= new Intent(cc,ViewTaskActivity.class);
			Task temp =taskList.get(pos);
			intent.putExtra("name", temp.getName() );
			intent.putExtra("category", temp.getCategory() );
			intent.putExtra("comment", temp.getComment() );
			intent.putExtra("date", temp.getDate().getTime() );
			intent.putExtra("latitude", temp.getLatitude() );
			intent.putExtra("longtitude", temp.getLongtitute() );
			startActivity(intent);
		}
	};

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent= new Intent(this,AddTaskActivity.class);
		this.startActivity(intent);
	}
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		currentLocation = arg0;
		
	}
	
	public float distanceInMeterFromHere(int lat,int lng){
		Location b = new Location("");
		b.setLatitude(lat/1e6);
		b.setLongitude(lng/1e6);
		return currentLocation.distanceTo(b);
	}
	
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}