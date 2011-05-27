package roman10reborn.threelinesiconifiedlist;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ThreeLinesIconifiedTextListAdapter extends BaseAdapter {
	private Context mContext;

	private List<ThreeLinesIconifiedText> mItems = new ArrayList<ThreeLinesIconifiedText>();

	public ThreeLinesIconifiedTextListAdapter(Context context) {
		mContext = context;
	}

	public void setListItems(List<ThreeLinesIconifiedText> lit) 
	{ mItems = lit; }

	public int getCount() { return mItems.size(); }

	public Object getItem(int position) 
	{ return mItems.get(position); }

	public boolean areAllItemsSelectable() { return false; }

	public boolean isSelectable(int position) { 
		return mItems.get(position).isSelectable();
	}

	/** Use the array index as a unique id. */
	public long getItemId(int position) {
		return position;
	}

	/** @param convertView The old view to overwrite, if one is passed
	 * @returns a IconifiedTextView that holds wraps around an IconifiedText */
	public View getView(int position, View convertView, ViewGroup parent) {
		ThreeLinesIconifiedTextView btv;
		if (convertView == null) {
			btv = new ThreeLinesIconifiedTextView(mContext, mItems.get(position));
		} else { //Reuse/Overwrite the View passed
			// We are assuming(!) that it is castable! 
			btv = (ThreeLinesIconifiedTextView) convertView;
			btv.setTextLine1(mItems.get(position).getTextLine1());
			btv.setTextLine2(mItems.get(position).getTextLine2());
			btv.setTextLine3(mItems.get(position).getTextLine3());
			btv.setIconText(mItems.get(position).getIconText());
			btv.setIcon(mItems.get(position).getIcon());
		}
		return btv;
	}
}