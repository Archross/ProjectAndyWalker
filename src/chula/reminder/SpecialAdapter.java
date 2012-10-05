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
	private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
	
	private List<Factor> factors;

	public SpecialAdapter(Context context, List<String> objects,List<Factor> f) {
	    super(context, android.R.layout.simple_list_item_1, objects);
	    factors = f;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = super.getView(position, convertView, parent);
	    TextView textView=(TextView) v.findViewById(android.R.id.text1);
	    textView.setTextColor(Color.BLACK);
	    Factor factor=factors.get(position);
	    if(factor.isLate()){
	    	v.setBackgroundColor(Color.RED);
	    }else if(factor.isNow()){
	    	v.setBackgroundColor(Color.GREEN);
	    }
	   // if (position == 0) {
	    //    v.setBackgroundColor(Color.BLACK);
	   // }
	    return v;
	}
}