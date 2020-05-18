package com.glavesoft.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleViewPagerIndicator extends LinearLayout {

	private static final int COLOR_TEXT_NORMAL = 0xFF000000;
	private static final int COLOR_INDICATOR_COLOR = Color.GREEN;

	private String[] mTitles;
	private int mTabCount;
	private int mIndicatorColor = COLOR_INDICATOR_COLOR;
	private float mTranslationX;
	private Paint mPaint = new Paint();
	private int mTabWidth;
	private OnSelectActionListener mSelect = null;
	private OnChangeColorListener mChange = null;

	private Context context;

	public SimpleViewPagerIndicator(Context context) {
		this(context, null);
		this.context = context;
	}

	public SimpleViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint.setColor(0xFF65Ae51);
		mPaint.setStrokeWidth(5.0F);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTabWidth = w / mTabCount;
	}

	public void setTitles(String[] titles) {
		mTitles = titles;
		mTabCount = titles.length;
		generateTitleView();

	}

	public void setIndicatorColor(int indicatorColor) {
		this.mIndicatorColor = indicatorColor;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		canvas.save();
		canvas.translate(mTranslationX, getHeight() - 2);
		canvas.drawLine(0, 0, mTabWidth, 0, mPaint);
		canvas.restore();
	}

	public void scroll(int position, float offset) {
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */
		mTranslationX = getWidth() / mTabCount * (position + offset);
		invalidate();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			break;
		case MotionEvent.ACTION_MOVE:

			break;
		case MotionEvent.ACTION_UP:

			break;
		}
		return super.dispatchTouchEvent(event);
	}

	private void generateTitleView() {
		if (getChildCount() > 0)
			this.removeAllViews();
		int count = mTitles.length;
		setWeightSum(count);
		for (int i = 0; i < count; i++) {

			LayoutParams lp = new LayoutParams(0,
					LayoutParams.MATCH_PARENT);
			lp.weight = 1;
			// tv.setGravity(Gravity.CENTER);
			// tv.setTextColor(COLOR_TEXT_NORMAL);
			// tv.setText(mTitles[i]);
			// // final String tempStr = mTitles[i];
			// tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			// tv.setLayoutParams(lp);
			// tv.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View v) {
			// Log.e("TAG", tempStr);
			// }
			// });
			// addView(tv);

			final LinearLayout layout = new LinearLayout(getContext());
			layout.setOrientation(LinearLayout.VERTICAL);

//			final ImageView imageView = new ImageView(getContext());
//			if (i == 0) {
//				imageView.setBackground(getResources().getDrawable(
//						R.drawable.icon_qiye_click));
//			} else {
//				imageView.setBackground(getResources().getDrawable(
//						R.drawable.icon_liebiao));
//			}
//			LayoutParams imageLayout = new LayoutParams(
//					30, 30);
//			imageView.setLayoutParams(imageLayout);
//			layout.addView(imageView);

			final TextView textView = new TextView(getContext());
			textView.setText(mTitles[i]);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
//			if (i == 0) {
//				textView.setTextColor(0xFF65Ae51);
//			} else {
//				textView.setTextColor(0xff666666);
//			}
			LayoutParams lptv = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(lptv);
			textView.setBackground(null);
			layout.addView(textView);

			layout.setGravity(Gravity.CENTER);
			layout.setLayoutParams(lp);

			final int pos = i;

			layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mSelect != null) {
						mSelect.OnSelect(pos);
					}

					setTitleColr(pos);
				}
			});
			addView(layout);
		}

	}

	public void setTitleColr(ViewPager viewPage, int pos) {
		setTitleColr(pos);
	}

	public void setTitleColr(int pos) {

		LinearLayout ll_left = (LinearLayout) getChildAt(0);
//		ImageView iv_left = (ImageView) ll_left.getChildAt(0);
		TextView tv_left = (TextView) ll_left.getChildAt(0);
		LinearLayout ll_right = (LinearLayout) getChildAt(1);
//		ImageView iv_right = (ImageView) ll_right.getChildAt(0);

		if (pos == 0) {
//			iv_left.setBackground(getResources().getDrawable(
//					R.drawable.icon_qiye_click));
//			tv_left.setTextColor(0xFF65Ae51);
//			iv_right.setBackground(getResources().getDrawable(
//					R.drawable.icon_liebiao));
			TextView tv_right = (TextView) ll_right.getChildAt(0);
//			tv_right.setTextColor(0xff666666);
		} else {
//			iv_left.setBackground(getResources().getDrawable(
//					R.drawable.icon_qiye));
//			tv_left.setTextColor(0xff666666);
//			iv_right.setBackground(getResources().getDrawable(
//					R.drawable.icon_liebiao_click));
			TextView tv_right = (TextView) ll_right.getChildAt(0);
//			tv_right.setTextColor(0xFF65Ae51);
		}

	}

	// ͷ����л�����
	public void setOnSelectActionListener(OnSelectActionListener select) {
		mSelect = select;
	}

	public void setOnChangeColorListener(OnChangeColorListener change) {
		mChange = change;
	}

	public interface OnSelectActionListener {
		public void OnSelect(int position);
	}

	public interface OnChangeColorListener {
		public void onChangeColor(TextView textView,
                                  int position);
	}

}
