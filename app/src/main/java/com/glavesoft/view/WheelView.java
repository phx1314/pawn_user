package com.glavesoft.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 滚轮view-ScrollView
 * 
 * <br>
 * <br>
 * 2015年1月5日 <br>
 * <br>
 * 问题：<br>
 * ① 当前只能默认第一个，需要优化为记住上次选择且自动选中并保存该值； <br>
 * ② 只支持数据量较小的需求，尽量保持在999以下，因view未重用； <br>
 * 
 */
public class WheelView extends ScrollView {
	public static final String TAG = WheelView.class.getSimpleName();

	public static class OnWheelViewListener {
		public void onSelected(int selectedIndex, String item) {
		}
	}

	// private ScrollView scrollView;
	private LinearLayout views;
	private Context context;
	// String[] items;
	List<String> items;

	public WheelView(Context context) {
		super(context);
		init(context);
	}

	public WheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public WheelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * 获取数据集合
	 * 
	 * @return
	 */
	private List<String> getItems() {
		return items;
	}

	/**
	 * 清空view和数据集合
	 */
	public void clearItems() {
		if (this.items == null) {
			return;
		}
		this.items.clear();
		views.removeAllViews();
	}

	/**
	 * 填充数据
	 * 
	 * @param list
	 */
	public void setItems(List<String> list) {
		if (null == items) {
			items = new ArrayList<String>();
		}
		items.clear();
		items.addAll(list);

		// 前面和后面补全
		for (int i = 0; i < offset; i++) {
			items.add(0, "");
			items.add("");
		}

		initData();

	}
	
	/**
	 * 设置item字体大小
	 * @param size
	 */
	public void setItemTextSize(int size){
		this.ITEM_TEXT_SIZE = size;
	}
	
	/**
	 * 设置选中item字体大小
	 * @param size
	 */
	public void setItemCheckTextSize(int size){
		this.ITEM_CHECK_TEXT_SIZE = size;
	}

	public static final int OFF_SET_DEFAULT = 1;
	/**
	 * 偏移量（需要在最前面和最后面补全，默认是1，上下各自偏移一个）
	 */
	int offset = OFF_SET_DEFAULT; // 

	public int getOffset() {
		return offset;
	}

	/**
	 * 设置选中数据的上下偏移量,默认为1显示三个，2为5个，参见方法{@link #setDisplayItemCounts()} <br><br>
	 * 不设置则不需要调用 <br>
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/** 每页显示的数量 */
	int displayItemCount; // 

	int selectedIndex = 1;

	@SuppressLint("NewApi")
	private void init(Context context) {
		this.context = context;

//		scrollView = ((ScrollView)this.getParent());
//		Log.i("steve", "scrollview: " + scrollView);
//		Log.i("steve", "parent: " + this.getParent());
//		this.setOrientation(VERTICAL);
		this.setVerticalScrollBarEnabled(false);

		views = new LinearLayout(context);
		views.setOrientation(LinearLayout.VERTICAL);
		this.addView(views);

		scrollerTask = new Runnable() {

			public void run() {

				int newY = getScrollY();
				if (initialY - newY == 0) { // stopped
					final int remainder = initialY % itemHeight;
					final int divided = initialY / itemHeight;
//					Log.i("steve", "initialY: " + initialY);
//					Log.i("steve", "remainder: " + remainder + ", divided: " + divided);
					if (remainder == 0) {
						selectedIndex = divided + offset;

						onSeletedCallBack();
					} else {
						if (remainder > itemHeight / 2) {
							WheelView.this.post(new Runnable() {
								@Override
								public void run() {
									WheelView.this.smoothScrollTo(0, initialY
											- remainder + itemHeight);
									selectedIndex = divided + offset + 1;
									onSeletedCallBack();
								}
							});
						} else {
							WheelView.this.post(new Runnable() {
								@Override
								public void run() {
									WheelView.this.smoothScrollTo(0, initialY
											- remainder);
									selectedIndex = divided + offset;
									onSeletedCallBack();
								}
							});
						}

					}

				} else {
					initialY = getScrollY();
					WheelView.this.postDelayed(scrollerTask, newCheck);
				}
			}
		};

	}

	int initialY;

	Runnable scrollerTask;
	int newCheck = 50;

	public void startScrollerTask() {

		initialY = getScrollY();
		this.postDelayed(scrollerTask, newCheck);
	}

	/**
	 * 设置每页显示的个数，和  offset搭配使用
	 */
	private void setDisplayItemCounts(){
		displayItemCount = offset * 2 + 1;
	}
	
	private void initData() {
		setDisplayItemCounts();
		
		for (String item : items) {
			views.addView(createView(item));
		}

		refreshItemView(0);
	}

	private int itemHeight = 0;
	private int ITEM_TEXT_SIZE = 15;
	private int ITEM_CHECK_TEXT_SIZE = 15;

	private TextView createView(String item) {
		TextView tv = new TextView(context);
		tv.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		tv.setSingleLine(true);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, ITEM_TEXT_SIZE);
		tv.setText(item);
		tv.setGravity(Gravity.CENTER);
		int padding = dip2px(context, 10);
		tv.setPadding(0, padding, 0, padding);
		if (0 == itemHeight) {
			itemHeight = getViewMeasuredHeight(tv);
			Log.i("steve", "itemHeight: " + itemHeight);
			// view宽度占满
			views.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, itemHeight
							* displayItemCount));
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this
					.getLayoutParams();
			Log.i("steve", "lp.width: " + lp.width);
//			this.setLayoutParams(new LinearLayout.LayoutParams(lp.width,
//					itemHeight * displayItemCount));// 设置可见高度=item高度 * 每页显示数量
			// 宽度占满整个view
			this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					itemHeight * displayItemCount));// 设置可见高度=item高度 * 每页显示数量
		}
		return tv;
	}

	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredHeight(View view) {
		// int height = view.getMeasuredHeight();
		// if(0 < height){
		// return height;
		// }
		calcViewMeasure(view);
		return view.getMeasuredHeight();
	}

	/**
	 * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
	 * 
	 * @param view
	 * @return
	 */
	public static int getViewMeasuredWidth(View view) {
		// int width = view.getMeasuredWidth();
		// if(0 < width){
		// return width;
		// }
		calcViewMeasure(view);
		return view.getMeasuredWidth();
	}

	/**
	 * 测量控件的尺寸
	 * 
	 * @param view
	 */
	public static void calcViewMeasure(View view) {
		// int width =
		// View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		// int height =
		// View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
		// view.measure(width,height);

		int width = MeasureSpec.makeMeasureSpec(0,
				MeasureSpec.UNSPECIFIED);
		int expandSpec = MeasureSpec.makeMeasureSpec(
				Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		view.measure(width, expandSpec);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		// Logger.d(TAG, "l: " + l + ", t: " + t + ", oldl: " + oldl +
		// ", oldt: " + oldt);

		// try {
		// Field field = ScrollView.class.getDeclaredField("mScroller");
		// field.setAccessible(true);
		// OverScroller mScroller = (OverScroller) field.get(this);
		//
		//
		// if(mScroller.isFinished()){
		// Logger.d(TAG, "isFinished...");
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

		refreshItemView(t);

		if (t > oldt) {
			// Logger.d(TAG, "向*下*滚动");
			scrollDirection = SCROLL_DIRECTION_DOWN;
		} else {
			// Logger.d(TAG, "向↑上↑滚动");
			scrollDirection = SCROLL_DIRECTION_UP;

		}

	}

	private void refreshItemView(int y) {
		int position = y / itemHeight + offset;
		int remainder = y % itemHeight;
		int divided = y / itemHeight;

		if (remainder == 0) {
			position = divided + offset;
		} else {
			if (remainder > itemHeight / 2) {
				position = divided + offset + 1;
			}

			// if(remainder > itemHeight / 2){
			// if(scrollDirection == SCROLL_DIRECTION_DOWN){
			// position = divided + offset;
			// Logger.d(TAG, ">down...position: " + position);
			// }else if(scrollDirection == SCROLL_DIRECTION_UP){
			// position = divided + offset + 1;
			// Logger.d(TAG, ">up...position: " + position);
			// }
			// }else{
			// // position = y / itemHeight + offset;
			// if(scrollDirection == SCROLL_DIRECTION_DOWN){
			// position = divided + offset;
			// Logger.d(TAG, "<down...position: " + position);
			// }else if(scrollDirection == SCROLL_DIRECTION_UP){
			// position = divided + offset + 1;
			// Logger.d(TAG, "<up...position: " + position);
			// }
			// }
			// }

			// if(scrollDirection == SCROLL_DIRECTION_DOWN){
			// position = divided + offset;
			// }else if(scrollDirection == SCROLL_DIRECTION_UP){
			// position = divided + offset + 1;
		}

		int childSize = views.getChildCount();
		for (int i = 0; i < childSize; i++) {
			TextView itemView = (TextView) views.getChildAt(i);
			if (null == itemView) {
				return;
			}
			if (position == i) {
				// check color
				itemView.setTextColor(Color.parseColor(colorCheckValue));
			} else {
				// other color
				itemView.setTextColor(Color.parseColor(colorNoCheckValue));
			}
		}
	}
	
	private String colorCheckValue = "#333333";
	private String colorNoCheckValue = "#999999";
	private String colorCheckValueFrame = "#CACACA";
	
	/**
	 * 设置选中值的颜色
	 * @param colorString [如：#333333]
	 */
	public void setColorCheckValue(String colorString){
		this.colorCheckValue = colorString;
	}
	
	/**
	 * 设置 非 选中值的颜色
	 * @param colorString [如：#999999]
	 */
	public void setColorNoCheckValue(String colorString){
		this.colorNoCheckValue = colorString;
	}
	/**
	 * 设置选中值的边框颜色
	 * @param colorString [如：蓝色：#83cde6.灰色：#CACACA]
	 */
	public void setColorCheckValueFrame(String colorString){
		this.colorCheckValueFrame = colorString;
	}

	/**
	 * 获取选中区域的边界
	 */
	int[] selectedAreaBorder;

	private int[] obtainSelectedAreaBorder() {
		if (null == selectedAreaBorder) {
			selectedAreaBorder = new int[2];
			selectedAreaBorder[0] = itemHeight * offset;
			selectedAreaBorder[1] = itemHeight * (offset + 1);
		}
		return selectedAreaBorder;
	}

	private int scrollDirection = -1;
	private static final int SCROLL_DIRECTION_UP = 0;
	private static final int SCROLL_DIRECTION_DOWN = 1;

	Paint paint;
	int viewWidth;

	@Override
	public void setBackgroundDrawable(Drawable background) {

		if (viewWidth == 0) {
			viewWidth = ((Activity) context).getWindowManager()
					.getDefaultDisplay().getWidth();
			// Logger.d(TAG, "viewWidth: " + viewWidth);
		}

		if (null == paint) {
			paint = new Paint();
			paint.setColor(Color.parseColor(colorCheckValueFrame));// 
			paint.setStrokeWidth(dip2px(context, 1f));
		}

		background = new Drawable() {
			@Override
			public void draw(Canvas canvas) {
				// draw line 画线
//				canvas.drawLine(viewWidth * 1 / 6,
//						obtainSelectedAreaBorder()[0], 
//						viewWidth * 5 / 6,
//						obtainSelectedAreaBorder()[0], paint);
//				canvas.drawLine(viewWidth * 1 / 6,
//						obtainSelectedAreaBorder()[1], 
//						viewWidth * 5 / 6,
//						obtainSelectedAreaBorder()[1], paint);
				canvas.drawLine(0,
						obtainSelectedAreaBorder()[0], 
						viewWidth * 5 / 5,// this modify
						obtainSelectedAreaBorder()[0], paint);
				canvas.drawLine(0,
						obtainSelectedAreaBorder()[1], 
						viewWidth * 5 / 5,// this modify
						obtainSelectedAreaBorder()[1], paint);
			}

			@Override
			public void setAlpha(int alpha) {

			}

			@Override
			public void setColorFilter(ColorFilter cf) {

			}

			@Override
			public int getOpacity() {
				return 0;
			}
		};

		super.setBackgroundDrawable(background);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i("steve", "w: " + w + ", h: " + h + ", oldw: " + oldw +
		 ", oldh: " + oldh);
		viewWidth = w;
//		// 根据屏幕宽度换算
//		ScreenInfo sinfo = new ScreenInfo((Activity) context);
//		w = sinfo.getWidth() / 2;
//		Log.i("steve", "viewWidth: " + viewWidth);
		setBackgroundDrawable(null);
	}

	/**
	 * 选中回调
	 */
	private void onSeletedCallBack() {
		if (null != onWheelViewListener) {
			if(items != null && selectedIndex <= items.size()){
				onWheelViewListener.onSelected(selectedIndex,
						items.get(selectedIndex));
			}else{
				Log.i("steve", "selectedIndex:" + selectedIndex);
			}
			
		}

	}

	public void setSeletion(int position) {
		final int p = position;
		selectedIndex = p + offset;
		this.post(new Runnable() {
			@Override
			public void run() {
				WheelView.this.smoothScrollTo(0, p * itemHeight);
			}
		});

	}

	public String getSeletedItem() {
		return items.get(selectedIndex);
	}

	public int getSeletedIndex() {
		return selectedIndex - offset;
	}

	@Override
	public void fling(int velocityY) {
		super.fling(velocityY / 3);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {

			startScrollerTask();
		}
		return super.onTouchEvent(ev);
	}

	private OnWheelViewListener onWheelViewListener;

	public OnWheelViewListener getOnWheelViewListener() {
		return onWheelViewListener;
	}

	public void setOnWheelViewListener(OnWheelViewListener onWheelViewListener) {
		this.onWheelViewListener = onWheelViewListener;
	}

}
