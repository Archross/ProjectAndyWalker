package chula.reminder;

import java.util.ArrayList;
import java.util.Date;

import chula.reminder.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ProjecttActivity extends Activity implements OnClickListener {
	
	private SQLiteAdapter mySQLiteAdapter;
	private  ListView listContent ;
	private Context cc;
	private  ArrayList<Task> taskList;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cc = this;
        listContent = (ListView) findViewById(R.id.contentlist);
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead(mySQLiteAdapter.MYTASK_TABLE);

    //    Cursor cursor = mySQLiteAdapter.queueAll();
     //   startManagingCursor(cursor);

      //  String[] from = new String[]{SQLiteAdapter.KEY_ID,SQLiteAdapter.KEY_NAME,
     //   		SQLiteAdapter.KEY_CATEGORY,SQLiteAdapter.KEY_COMMENT,SQLiteAdapter.KEY_DATE};
       // String[] from = new String[]{SQLiteAdapter.KEY_ID};
        //int[] to = new int[]{R.id.text};

       // SimpleCursorAdapter cursorAdapter =
         //new SimpleCursorAdapter(this, R.layout.row, cursor, from, to);

       // listContent.setAdapter(cursorAdapter);
         taskList = mySQLiteAdapter.getTasKList();
        ArrayList<String> nameList = new ArrayList<String>(10);
        for (int i = 0; i < taskList.size(); i++) {
			nameList.add(taskList.get(i).getName());
		}
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nameList);
        listContent.setAdapter(arrayAdapter); 
        mySQLiteAdapter.close();
        
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