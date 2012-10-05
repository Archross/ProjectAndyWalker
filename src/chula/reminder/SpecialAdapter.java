package chula.reminder;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SpecialAdapter extends ArrayAdapter<String> {
	private static final int TYPE_COUNT = 2;
	private static final int TYPE_ITEM_RED = 1;
	private static final int TYPE_ITEM_NORMAL = 0;
	private static final int TYPE_ITEM_GREEN= 3;
	
	private List<Factor> factors;

	public SpecialAdapter(Context context, List<String> objects,List<Factor> f) {
	    super(context, android.R.layout.simple_list_item_1, objects);
	    factors = f;
	}
	
	@Override
	public int getViewTypeCount() {
	    return TYPE_COUNT;
	}

	@Override
	public int getItemViewType(int position) {

	    if(factors.get(position).isLate())return TYPE_ITEM_RED;
	    if(factors.get(position).isLate())return TYPE_ITEM_GREEN;
	    
	    return TYPE_ITEM_NORMAL;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = super.getView(position, convertView, parent);
	    TextView textView=(TextView) v.findViewById(android.R.id.text1);
	    textView.setTextColor(Color.BLACK);
	    switch (getItemViewType(position)) {
	    case TYPE_ITEM_RED :
	        v.setBackgroundColor(Color.RED);
	        break;
	    case TYPE_ITEM_NORMAL:
	        break;
	    case TYPE_ITEM_GREEN:
	    	v.setBackgroundColor(Color.GREEN);
	    	break;
	    }
	  
	    return v;
	}
}