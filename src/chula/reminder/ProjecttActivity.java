package chula.reminder;

import java.util.ArrayList;
import java.util.Date;

import chula.reminder.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ProjecttActivity extends Activity implements OnClickListener {
	public final int DELETE_ID = 0;
	private SQLiteAdapter mySQLiteAdapter;
	private  ListView listContent ;
	private Context cc;
	private  ArrayList<Task> taskList;
	private ArrayAdapter<String> arrayAdapter;
	private ArrayList<String> nameList ;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cc = this;
        listContent = (ListView) findViewById(R.id.contentlist);
        mySQLiteAdapter = new SQLiteAdapter(this);
       
       nameList = fillData();
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
     /*   listContent.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				// TODO Auto-generated method stub
				mySQLiteAdapter.openToWrite(mySQLiteAdapter.MYTASK_TABLE);
				mySQLiteAdapter.delete_byID(mySQLiteAdapter.MYTASK_TABLE,taskList.get(pos).getId());
				mySQLiteAdapter.close();
				taskList.remove(pos);
				arrayAdapter.remove(taskList.get(pos).getName());
				arrayAdapter.notifyDataSetChanged();
				return true;
			}
		}); */
     
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