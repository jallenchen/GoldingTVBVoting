package com.golding.tvbvotingsystem.ui;

import com.golding.tvbvotingsystem.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/** 
 * @author  :Jallen 
 * @date    ��2017-6-16 ����6:53:59 
 * @version :1.0 
 * @desc :
 */
public class ImageButtonText extends LinearLayout {

	private ImageView imageViewbutton;
	private TextView textView;

	public ImageButtonText(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		imageViewbutton = new ImageView(context, attrs);
		imageViewbutton.setPadding(0, 0, 0, 0);
		imageViewbutton.setMinimumHeight((int)context.getResources().getDimension(R.dimen.unlike_check_size));
		imageViewbutton.setMinimumWidth((int)context.getResources().getDimension(R.dimen.unlike_check_size));
		textView = new TextView(context, attrs);
		// 水平居中
		textView.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
		textView.setPadding(0, 0, 0, 0);
		textView.setTextSize(context.getResources().getDimension(R.dimen.text_size20));
		setClickable(true);
		setFocusable(true);
		//setBackgroundResource(android.R.drawable.btn_default);
		setOrientation(LinearLayout.VERTICAL);
		addView(imageViewbutton);
		addView(textView);
	}

}
