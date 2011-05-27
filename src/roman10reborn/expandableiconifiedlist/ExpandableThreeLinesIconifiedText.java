package roman10reborn.expandableiconifiedlist;

import roman10reborn.apl.data2.AdvancedPhoneLogRecord;
import android.graphics.drawable.Drawable;

public class ExpandableThreeLinesIconifiedText implements Comparable<ExpandableThreeLinesIconifiedText>{
	private String mTextLine1_text1 = "";
	private String mTextLine1_text2 = "";
	private String mTextLine2 = "";
	private String mTextLine3 = "";
	private Drawable mLeftIcon = null;
	private Drawable mRightIcon = null;
	private String mLeftIconText = "";
	private String mRightIconText = "";
	private boolean mSelectable = true;
	private int mId;
	private AdvancedPhoneLogRecord mApl;		//used to check for phoneLogRecord
	
	private Drawable mRightIcon1, mRightIcon2;
	private int state = 0;

	public ExpandableThreeLinesIconifiedText(String _iconLine, String _iconRightLine, 
			String _line1_text1,
			String _line1_text2, 
			String _line2, String _line3, Drawable _leftIcon,
			Drawable _rightIcon, int _id, AdvancedPhoneLogRecord _mApl) {
		mLeftIcon = _leftIcon;
		mRightIcon = _rightIcon;
		mLeftIconText = _iconLine;
		mRightIconText = _iconRightLine;
		mTextLine1_text1 = _line1_text1;
		mTextLine1_text2 = _line1_text2;
		mTextLine2 = _line2;
		mTextLine3 = _line3;
		mId = _id;
		mApl = _mApl;
	}
	
	public ExpandableThreeLinesIconifiedText(String _iconLine, String _iconRightLine, 
			String _line1_text1,
			String _line1_text2, 
			String _line2, String _line3, Drawable _leftIcon,
			Drawable _rightIcon1, Drawable _rightIcon2, int _id,
			AdvancedPhoneLogRecord _mApl) {
		mLeftIcon = _leftIcon;
		mRightIcon1 = _rightIcon1;
		mRightIcon2 = _rightIcon2;
		mLeftIconText = _iconLine;
		mRightIconText = _iconRightLine;
		mTextLine1_text1 = _line1_text1;
		mTextLine1_text2 = _line1_text2;
		mTextLine2 = _line2;
		mTextLine3 = _line3;
		mId = _id;
		mApl = _mApl;
	}
	
	public AdvancedPhoneLogRecord getAPL() {
		return mApl;
	}
	
	public int getId() {
		return mId;
	}
	
	public void setId(int _id) {
		mId = _id;
	}
	
	public boolean isSelectable() {
		return mSelectable;
	}
	
	public void setSelectable(boolean selectable) {
		mSelectable = selectable;
	}
	
	public String getLeftIconText() {
		return mLeftIconText;
	}
	
	public String getRightIconText() {
		return mRightIconText;
	}
	
	public String getTextLine1Text1() {
		return mTextLine1_text1;
	}
	
	public String getTextLine1Text2() {
		return mTextLine1_text2;
	}
	
	public String getTextLine2() {
		return mTextLine2;
	}
	
	public String getTextLine3() {
		return mTextLine3;
	}
	
	public void setLeftIconText(String text) {
		mLeftIconText = text;
	}
	
	public void setRightIconText(String text) {
		mRightIconText = text;
	}
	
	public void setTextLine1Text1(String text) {
		mTextLine1_text1 = text;
	}
	
	public void setTextLine1Text2(String text) {
		mTextLine1_text2 = text;
	}
	
	public void setTextLine2(String text) {
		mTextLine2 = text;
	}
	
	public void setTextLine3(String text) {
		mTextLine3 = text;
	}
	
	public void setLeftIcon(Drawable icon) {
		mLeftIcon = icon;
	}
	
	public Drawable getLeftIcon() {
		return mLeftIcon;
	}
	
	public void setRightIcon(Drawable icon) {
		mLeftIcon = icon;
	}
	
	
	public Drawable getRightIconWithTwoStates() {
		if (state == 0) {
			state = 1;
			return mRightIcon1;
		} else {
			state = 0;
			return mRightIcon2;
		}
	}
	
	public Drawable getRightIcon1() {
		return mRightIcon1;
	}
	
	public Drawable getRightIcon2() {
		return mRightIcon2;
	}
	
	public Drawable getRightIcon() {
		return mRightIcon;
	}
	//compare two objects
	public int compareTo(ExpandableThreeLinesIconifiedText other) {
		int l_firstCompare = this.mTextLine1_text1.compareTo(other.mTextLine1_text1);
		if (l_firstCompare!=0) {
			return l_firstCompare;
		} else {
			int l_secondCompare = this.mTextLine1_text2.compareTo(other.mTextLine1_text2);
			if (l_secondCompare!=0) {
				return l_secondCompare;
			} else {
				int l_thirdCompare = this.mTextLine2.compareTo(other.mTextLine2);
				if (l_thirdCompare!=0) {
					return l_thirdCompare;
				}
				return this.mTextLine3.compareTo(other.mTextLine3);
			}
		}
	}
}
