package com.glavesoft.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;

/**
 * 基本的提示框显示
 */
public class BAlertDialog {
	
	private Dialog alertDialog;
	private TextView titleView, tv_d,tv_d1;
	private TextView messageView;
	private Button yesButton, noButton;
	private OnClickListener yesListener, noListener;
	private RatingBar ratingbar;
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			alertDialog.dismiss();
			switch (v.getId()) {
			case R.id.ok:
				if(yesListener != null) yesListener.onClick(v);
				break;
			case R.id.no:
				if(noListener != null) noListener.onClick(v);
				break;
			}
		}
	};

	public BAlertDialog(Context context,boolean isCanclable) {
		alertDialog = new Dialog(context);
		alertDialog.setCancelable(isCanclable);
		alertDialog.setCanceledOnTouchOutside(isCanclable);
		alertDialog.show();
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.balertdialog);
		window.setBackgroundDrawable(new BitmapDrawable());
		titleView = (TextView) window.findViewById(R.id.title);
		tv_d = (TextView) window.findViewById(R.id.tv_d);
		tv_d1 = (TextView) window.findViewById(R.id.tv_d1);
		messageView = (TextView) window.findViewById(R.id.message);
		yesButton = (Button) window.findViewById(R.id.ok);
		yesButton.setOnClickListener(onClickListener);
		noButton = (Button) window.findViewById(R.id.no);
		noButton.setOnClickListener(onClickListener);
		//ratingbar = (RatingBar) window.findViewById(R.id.ratingbar);
	}


	public BAlertDialog setRatingbar() {
		ratingbar.setVisibility(View.VISIBLE);
		return this;
	}
	
	public RatingBar getRatingbar() {
		return ratingbar;
	}
	
	public BAlertDialog setTitle(String title) {
		titleView.setVisibility(View.VISIBLE);
		titleView.setText(title);
		return this;
	}
	

	public BAlertDialog setMessage(String message, boolean warn) {
		messageView.setText(message);
		if(message.equals("")){
			messageView.setVisibility(View.GONE);
		}else{
			messageView.setVisibility(View.VISIBLE);
		}
		if (warn) {
			messageView.setTextColor(Color.parseColor("#C80C0C"));
		}
		return this;
	}

	public BAlertDialog setYesButton(String text,final OnClickListener listener,boolean isSmall) {
		yesButton.setVisibility(View.VISIBLE);
		tv_d1.setVisibility(View.VISIBLE);
		if(isSmall){
			yesButton.setTextSize(13f);
		}
		yesButton.setText(text);
		yesListener = listener;
		yesButton.setOnClickListener(onClickListener);
		yesButton.setVisibility(View.VISIBLE);
		return this;
	}

	public BAlertDialog setNoButton(String text,final OnClickListener listener) {
		tv_d.setVisibility(View.VISIBLE);
		noButton.setVisibility(View.VISIBLE);
		noButton.setText(text);
		noListener = listener;
		noButton.setOnClickListener(onClickListener);
		noButton.setVisibility(View.VISIBLE);
		return this;
	}

	public void show() {
		alertDialog.show();
	}

	public void dismiss() {
		alertDialog.dismiss();
	}

	public boolean isShowing() {
		return alertDialog.isShowing();
	}

	public void setOndismissListener(OnDismissListener dismissListener) {
		alertDialog.setOnDismissListener(dismissListener);
	}

}
