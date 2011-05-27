package roman10reborn.threelinesiconifiedlist;

import roman10reborn.apl.main.R;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ThreeLinesIconifiedTextView extends RelativeLayout {
	private TextView mTextLine1;
	private TextView mTextLine2;
	private TextView mTextLine3;
	private ImageView mIcon;
	private TextView mIconText;
	
	public ThreeLinesIconifiedTextView(Context context, ThreeLinesIconifiedText aIconifiedText) {
		super(context);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.three_lines_icon_text_view, this, true);
		
		mIcon = (ImageView) findViewById(R.id.three_lines_icon_text_view_icon);
		mIcon.setPadding(5, 5, 20, 3); // left, top, right, bottom
		mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //mIcon.setLayoutParams(new LayoutParams(72, 72)); 
		mIcon.setImageDrawable(aIconifiedText.getIcon());
		
		mIconText = (TextView) findViewById(R.id.three_lines_icon_text_view_line_under_icon);
		//mIconText.setTypeface(Typeface.DEFAULT_BOLD, 0);
		mIconText.setText(aIconifiedText.getIconText());
		
		mTextLine1 = (TextView) findViewById(R.id.three_lines_icon_text_view_line1);
		//mTextLine1.setTypeface(Typeface.DEFAULT_BOLD, 0);
		mTextLine1.setText(aIconifiedText.getTextLine1());
		
		mTextLine2 = (TextView) findViewById(R.id.three_lines_icon_text_view_line2);
		//mTextLine2.setTypeface(Typeface.SERIF, 0);
		mTextLine2.setText(aIconifiedText.getTextLine2());
		
		mTextLine3 = (TextView) findViewById(R.id.three_lines_icon_text_view_line3);
		//mTextLine3.setTypeface(Typeface.SERIF, 0);
		mTextLine3.setText(aIconifiedText.getTextLine3());
	}

	public void setTextLine1(String words) {
		mTextLine1.setText(words);
	}
	
	public void setTextLine2(String words) {
		mTextLine2.setText(words);
	}
	
	public void setTextLine3(String words) {
		mTextLine3.setText(words);
	}
	
	public void setIconText(String words) {
		mIconText.setText(words);
	}
	
	public void setIcon(Drawable bullet) {
		mIcon.setImageDrawable(bullet);
	}

	public void setIcon(int res_id) {
		mIcon.setImageResource(res_id);
	}
}