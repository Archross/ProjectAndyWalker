package chula.reminder;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ListColorActivity extends ListActivity {
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  setContentView(R.layout.main);
		  String[] captionArray = { "USA","India","England","Russia","Europe","Canada","Srilanka","Singapore","Thailand","Australia"};
		  ItemsAdapter ItemsAdapter = new ItemsAdapter(
			ListColorActivity.this, R.layout.list,
		    captionArray);
		  setListAdapter(ItemsAdapter);
		 }
		 
		  
		 private class ItemsAdapter extends BaseAdapter {
		  String[] items;

		  public ItemsAdapter(Context context, int textViewResourceId,
		    String[] items) {
		   // super(context, textViewResourceId, items);
		   this.items = items;
		  }

		  // @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
			  int[] color_arr={Color.BLUE,Color.CYAN,Color.DKGRAY,Color.GREEN,Color.RED};

		   View v = convertView;
		   if (v == null) {
		    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    v = vi.inflate(R.layout.list, null);
		   } 
		   TextView post = (TextView) v
		     .findViewById(R.id.post);
		   post.setText(items[position]);
		   
		   if (position== 0) {
			   v.setBackgroundColor(color_arr[position]);
           } else if(position== 1) {
        	   v.setBackgroundColor(color_arr[position]);
           }

		   
		   return v;
		  }

		  public int getCount() {
		   return items.length;
		  }

		  public Object getItem(int position) {
		   return position;
		  }

		  public long getItemId(int position) {
		   return position;
		  }
		 }
		}
