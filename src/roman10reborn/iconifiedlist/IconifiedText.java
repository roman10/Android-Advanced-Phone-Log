package roman10reborn.iconifiedlist;

import android.graphics.drawable.Drawable;

public class IconifiedText implements Comparable<IconifiedText>{
	private String mText = "";
	private String mInfo = "";
	private Drawable mIcon;
	private boolean mChecked;

	public IconifiedText(String text, Drawable bullet, boolean _checked, String _hiddenInfo) {
		mIcon = bullet;
		mText = text;
		mChecked = _checked;
		mInfo = _hiddenInfo;
	}
	
	public IconifiedText(String text) {
		mText = text;
		mIcon = null;
		mChecked = false;
		mInfo = "";
	}

	public String getInfo() {
		return mInfo;
	}
	
	public void setInfo(String _info) {
		mInfo = _info;
	}
	
	public String getText() {
		return mText;
	}
	
	public void setText(String text) {
		mText = text;
	}
	
	public boolean isSelectable() {
		return true;
	}
	
	public void setIcon(Drawable icon) {
		mIcon = icon;
	}
	
	public Drawable getIcon() {
		return mIcon;
	}

	public void setChecked(boolean _checked) {
		mChecked = _checked;
	}
	
	public boolean getChecked() {
		return mChecked;
	}
	
	public int compareTo(IconifiedText other) {
		if(this.mText != null)
			return this.mText.compareTo(other.getText()); 
		else 
			throw new IllegalArgumentException();
	}
}
