package roman10reborn.expandableiconifiedlist;

import roman10reborn.apl.main.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class ExpandableThreeLinesIconifiedTextGroupView extends RelativeLayout {
	private TextView mTextLine1_text1, mTextLine1_text2;
	private TextView mTextLine2;
	private TextView mTextLine3;
	//private ImageView mLeftIcon;
	public ImageButton mLeftIcon;
	//private ImageView mRightIcon;
	public ImageButton mRightIcon;
	//private TextView mLeftIconText;
	private TextView mRightIconText;
	private ExpandableThreeLinesIconifiedText content;
	
	private View filler, filler2;
	
	public ExpandableThreeLinesIconifiedTextGroupView(Context context, ExpandableThreeLinesIconifiedText aIconifiedText) {
		super(context);
		content = aIconifiedText;

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.expandable_three_lines_icon_text_group_view, this, true);
		
		//mLeftIcon = (ImageView) findViewById(R.id.expandable_three_lines_icon_text_view_left_icon);
		mLeftIcon = (ImageButton) findViewById(R.id.expandable_three_lines_icon_text_view_left_icon);
		mLeftIcon.setPadding(5, 5, 5, 15); // left, top, right, bottom
		mLeftIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
		mLeftIcon.setLayoutParams(new LayoutParams(100, 100)); 
		//mLeftIcon.setBackgroundResource(R.drawable.photoframe4);
		mLeftIcon.setImageDrawable(aIconifiedText.getLeftIcon());
		mLeftIcon.setClickable(true);
		mLeftIcon.setFocusable(false);
		
//		mRightIcon = (ImageView) findViewById(R.id.expandable_three_lines_icon_text_view_right_icon);
//		mRightIcon.setPadding(20, 8, 20, 3); // left, top, right, bottom
//		mRightIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		mRightIcon = (ImageButton) findViewById(R.id.expandable_three_lines_icon_text_view_right_icon);
		mRightIcon.setPadding(20, 8, 20, 3); // left, top, right, bottom
		mRightIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
		mRightIcon.setClickable(true);
		mRightIcon.setFocusable(false);
		
		//mRightIcon.setLayoutParams(new LayoutParams(72, 72));
		mRightIcon.setMaxHeight(72);
		mRightIcon.setMinimumHeight(72);
		mRightIcon.setMaxWidth(72);
		mRightIcon.setMinimumWidth(72);
		mRightIcon.setImageDrawable(aIconifiedText.getRightIcon1());
		//mRightIcon.setBackgroundDrawable(null);
		//mRightIcon.setBackgroundColor(Color.TRANSPARENT);
		mRightIcon.setBackgroundResource(R.drawable.list_button);
		//mRightIcon.setAlpha(255);
		//mRightIcon.setBackgroundDrawable(aIconifiedText.getRightIcon1());
		
//		mLeftIconText = (TextView) findViewById(R.id.expandable_three_lines_icon_text_view_line_under_left_icon);
//		//mIconText.setTypeface(Typeface.DEFAULT_BOLD, 0);
//		mLeftIconText.setText(aIconifiedText.getLeftIconText());
//		mLeftIconText.setVisibility(View.GONE);
		
		mRightIconText = (TextView) findViewById(R.id.expandable_three_lines_icon_text_view_line_under_right_icon);
		mRightIconText.setText(aIconifiedText.getRightIconText());

		mTextLine1_text1 = (TextView) findViewById(R.id.expandable_three_lines_icon_text_view_line1_text1);
		//mTextLine1.setTypeface(Typeface.DEFAULT_BOLD, 0);
		mTextLine1_text1.setText(aIconifiedText.getTextLine1Text1());
		
		mTextLine1_text2 = (TextView) findViewById(R.id.expandable_three_lines_icon_text_view_line1_text2);
		//mTextLine1.setTypeface(Typeface.DEFAULT_BOLD, 0);
		mTextLine1_text2.setText(aIconifiedText.getTextLine1Text2());
		
		mTextLine2 = (TextView) findViewById(R.id.expandable_three_lines_icon_text_view_line2);
		//mTextLine2.setTypeface(Typeface.SERIF, 0);
		mTextLine2.setText(aIconifiedText.getTextLine2());
		
		mTextLine3 = (TextView) findViewById(R.id.expandable_three_lines_icon_text_view_line3);
		//mTextLine3.setTypeface(Typeface.SERIF, 0);
		//mTextLine3.setText(aIconifiedText.getTextLine3());
		SpannableStringBuilder lSpanStr = new SpannableStringBuilder(aIconifiedText.getTextLine3());
		lSpanStr.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 10, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		mTextLine3.setText(lSpanStr, TextView.BufferType.SPANNABLE);
		
		filler = (View)findViewById(R.id.filler);
		filler2 = (View)findViewById(R.id.filler2);
		//filler.setMinimumWidth(5);
	}
	
	public void setFillerWidth(int _dip) {
		//filler.setMinimumWidth(_dip);
		//filler.setLayoutParams(new LayoutParams(_dip, _dip));
		//filler.setPadding(_dip-5, 0, 0, 0);
		//filler.setLayoutParams(new LayoutParams(_dip, RelativeLayout.LayoutParams.FILL_PARENT));
		if (_dip == 1) {
			filler.setVisibility(View.INVISIBLE);
			filler2.setVisibility(View.GONE);
		} else {
			filler2.setVisibility(View.INVISIBLE);
			filler.setVisibility(View.GONE);
		}
	}
	
	public ExpandableThreeLinesIconifiedText getContent() {
		return content;
	}

	public void setTextLine1Text1(String words) {
		mTextLine1_text1.setText(words);
	}
	
	public void setTextLine1Text2(String words) {
		mTextLine1_text2.setText(words);
	}
	
	public void setTextLine2(String words) {
		mTextLine2.setText(words);
	}
	
	public void setTextLine3(String words) {
		mTextLine3.setText(words);
	}
	
	public void setTextLine3(SpannableStringBuilder _panStr, BufferType _type) {
		mTextLine3.setText(_panStr, _type);
	}
	
//	public void setLeftIconText(String words) {
//		mLeftIconText.setText(words);
//	}
	
	public void setRightIconText(String words) {
		mRightIconText.setText(words);
	}
	
	public void setLeftIcon(Drawable bullet) {
		mLeftIcon.setImageDrawable(bullet);
	}

	public void setLeftIcon(int res_id) {
		mLeftIcon.setImageResource(res_id);
	}
	
	public void setRightIcon(Drawable bullet) {
		mRightIcon.setImageDrawable(bullet);
	}

	public void setRightIcon(int res_id) {
		mRightIcon.setImageResource(res_id);
	}
}