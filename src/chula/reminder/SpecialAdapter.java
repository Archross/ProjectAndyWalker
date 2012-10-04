package chula.reminder;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

public class SpecialAdapter extends ArrayAdapter<String> {
	private int[] colors = new int[] { 0x30FF0000, 0x300000FF };
	
	private List<String> mWeights;

	public SpecialAdapter(Context context, List<String> objects) {
	    super(context, android.R.layout.simple_list_item_1, objects);
	    mWeights = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    View v = super.getView(position, convertView, parent);
	    if (position == 0) {
	        v.setBackgroundColor(Color.BLACK);
	    }
	    return v;
	}
}