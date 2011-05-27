package roman10reborn.threelinesiconifiedlist;

import android.graphics.drawable.Drawable;

public class ThreeLinesIconifiedText implements Comparable<ThreeLinesIconifiedText>{
	private String mTextLine1 = "";
	private String mTextLine2 = "";
	private String mTextLine3 = "";
	private Drawable mIcon;
	private String mIconText = "";
	private boolean mSelectable = true;

	public ThreeLinesIconifiedText(String _iconLine, String _line1, String _line2, String _line3, Drawable bullet) {
		mIcon = bullet;
		mIconText = _iconLine;
		mTextLine1 = _line1;
		mTextLine2 = _line2;
		mTextLine3 = _line3;
	}
	
	
	public boolean isSelectable() {
		return mSelectable;
	}
	
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}
	
	public String getIconText() {
		return mIconText;
	}
	
	public String getTextLine1() {
		return mTextLine1;
	}
	
	public String getTextLine2() {
		return mTextLine2;
	}
	
	public String getTextLine3() {
		return mTextLine3;
	}
	
	public void setIconText(String text) {
		mIconText = text;
	}
	
	public void setTextLine1(String text) {
		mTextLine1 = text;
	}
	
	public void setTextLine2(String text) {
		mTextLine2 = text;
	}
	
	public void setTextLine3(String text) {
		mTextLine3 = text;
	}
	
	public void setIcon(Drawable icon) {
		mIcon = icon;
	}
	
	public Drawable getIcon() {
		return mIcon;
	}
	//compare two objects
	public int compareTo(ThreeLinesIconifiedText other) {
		int l_firstCompare = this.mTextLine1.compareTo(other.mTextLine1);
		if (l_firstCompare!=0) {
			return l_firstCompare;
		} else {
			int l_secondCompare = this.mTextLine2.compareTo(other.mTextLine2);
			if (l_secondCompare!=0) {
				return l_secondCompare;
			} else {
				return this.mTextLine3.compareTo(other.mTextLine3);
			}
		}
	}
}
