package roman10reborn.iconifiedlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IconifiedTextView extends LinearLayout {	
	private TextView mText;
	private ImageView mIcon;
	public static int mCheckbox_id = 1000;
	public IconifiedTextView(Context context, IconifiedText iconifiedText) {
		super(context);

		/* First Icon and the Text to the right (horizontal),
		 * not above and below (vertical) */
		this.setOrientation(HORIZONTAL);

		mIcon = new ImageView(context);
		mIcon.setPadding(5, 10, 20, 10); // 5px to the right
		mIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mIcon.setLayoutParams(new LayoutParams(80, 80)); 
        //mIcon.setMaxHeight(72);
        //mIcon.setMaxWidth(72);
		mIcon.setImageDrawable(iconifiedText.getIcon());
		mIcon.setVisibility(View.GONE);
		addView(mIcon);
		
		mText = new TextView(context);
		mText.setTypeface(Typeface.SERIF, 0);
		mText.setTextColor(Color.BLACK);
		mText.setTextSize(24);
		mText.setText(iconifiedText.getText());
		LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		params.weight = 1;
		params.gravity = Gravity.CENTER_VERTICAL;
		addView(mText, params);
	}

	public void setText(String words) {
		mText.setText(words);
	}
	
	public void setIcon(Drawable bullet) {
		mIcon.setImageDrawable(bullet);
	}

	public void setIcon(int res_id) {
		mIcon.setImageResource(res_id);
	}
}