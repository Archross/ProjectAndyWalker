package chula.reminder;

import java.util.ArrayList;
import chula.reminder.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ProjecttActivity extends Activity implements OnClickListener {
	public final int DELETE_ID = 0;
	public final int CATEGORY_FILTER_ID = 1;
	public final int LOCATION_FILTER_ID= 2;
	private SQLiteAdapter mySQLiteAdapter;
	private  ListView listContent,categoryListView ;
	private Context cc;
	private  ArrayList<Task> taskList;
	private ArrayAdapter<String> arrayAdapter,categoryAdapter;
	private ArrayList<String> nameList ;
	private ArrayList<String> categoryList ;
	 private Dialog dlg;
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
        Button d = (Button) findViewById(R.id.m_deleteall);
        d.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYTASK_TABLE);
			        mySQLiteAdapter.deleteAll();
			        mySQLiteAdapter.close();
			}
		});
        listContent.setOnItemClickListener(listItemListener);
        listContent.setOnCreateContextMenuListener(this);
   
     
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	// TODO Auto-generated method stub
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0,DELETE_ID,0,"Delete");
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId()){
    	case DELETE_ID : 
    		int pos = menuInfo.position;
    		mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYTASK_TABLE);
    		mySQLiteAdapter.delete_byID(mySQLiteAdapter.MYTASK_TABLE,taskList.get(pos).getId());
    		mySQLiteAdapter.close();
    		String temp = taskList.get(pos).getName();
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
        mySQLiteAdapter.close();
        
        categoryList.add(getResources().getString(R.string.add_category));
        dlg = new Dialog(cc);
		dlg.setTitle("Filter by category");
		dlg.setContentView(R.layout.category_list);
		 categoryListView =(ListView) dlg.findViewById(R.id.categoryList);
		 categoryAdapter = new ArrayAdapter<String>(dlg.getContext(),android.R.layout.simple_list_item_1, categoryList);
		 categoryListView.setAdapter(categoryAdapter);
		 categoryListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				//dlg.setTitle((String)categoryList.get(pos));
				if(pos<categoryList.size()-1){
					filterByCategory(pos);
				}
				dlg.dismiss();
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
			startActivity(intent);
		}
	};

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent= new Intent(this,AddTaskActivity.class);
		this.startActivity(intent);
	}
}