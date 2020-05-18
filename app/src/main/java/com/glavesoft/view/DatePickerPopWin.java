package com.glavesoft.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.glavesoft.pawnuser.R;
import com.glavesoft.view.PickerView.onSelectListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * 年月日
 */
public class DatePickerPopWin extends PopupWindow {

	ArrayList<String> yearList = new ArrayList<String>();
	ArrayList<String> monthList = new ArrayList<String>();
	ArrayList<String> dateList = new ArrayList<String>();

	View mTimeView;
	PickerView v_year, v_month, v_date;
	Button btn_cancel, btn_confirm;
	String date = "";
	String month = "";
	String year = "";

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				v_month.setData(monthList);
				v_month.setSelected(month);
			}
			if (msg.what == 0) {
				v_date.setData(dateList);
				v_date.setSelected(date);
			}
		}
	};


	/**
	 * 
	 * @param context
	 * @param type  0今天以前,1今天以后
	 */
	public DatePickerPopWin(Activity context, final int type) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mTimeView = inflater.inflate(R.layout.pw_datepicker, null);

		v_year = (PickerView) mTimeView.findViewById(R.id.pv_year);
		v_date = (PickerView) mTimeView.findViewById(R.id.pv_date);
		v_month = (PickerView) mTimeView.findViewById(R.id.pv_month);

		btn_cancel = (Button) mTimeView.findViewById(R.id.btn_dcancel);
		btn_confirm = (Button) mTimeView.findViewById(R.id.btn_dconfirm);
		btn_cancel.setOnClickListener(onClickListener);
		btn_confirm.setOnClickListener(onClickListener);

		Calendar calendar = Calendar.getInstance();
		final int year_now = calendar.get(Calendar.YEAR);
		final int month_now = calendar.get(Calendar.MONTH) + 1;
		System.out.println("month_now:" + month_now);
		final int day_now = calendar.get(Calendar.DAY_OF_MONTH);

		if (type == 0) {
			for (int i = 0; i < 80; i++) {
				yearList.add((year_now - i) + "年");
			}

			for (int i = 1; i <= month_now; i++) {
				monthList.add(i + "月");
			}

			for (int i = 1; i <= day_now; i++) {
				dateList.add(i + "日");
			}
		} else {
			for (int i = 0; i < 80; i++) {
				yearList.add((year_now + i) + "年");
			}

			for (int i = month_now; i <= 12; i++) {
				monthList.add(i + "月");
			}

			for (int i = day_now; i <= getMaxDay(year_now, month_now); i++) {
				dateList.add(i + "日");
			}
		}

		v_year.setData(yearList);
		v_year.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				year = text;
				int y = Integer.parseInt(year.substring(0, year.length() - 1));
				int m = Integer.parseInt(month.substring(0, month.length() - 1));
				monthList.clear();
				dateList.clear();
				switch (type) {
				case 0://以前
					if (y == year_now){
						for (int i = 1; i <= month_now; i++) {
							monthList.add(i + "月");
						}
						for (; m > monthList.size(); m--) {
						}
						month = monthList.get(m - 1);
						if (m == month_now) {
							for (int i = 1; i <= day_now; i++) {
								dateList.add(i + "日");
							}
						} else {
							for (int i = 1; i <= getMaxDay(y, m); i++) {
								dateList.add(i + "日");
							}
						}
					}else {
						for (int i = 1; i <= 12; i++) {
							monthList.add(i + "月");
						}
						for (int i = 1; i <= getMaxDay(y, m); i++) {
							dateList.add(i + "日");
						}
					}
					if (Integer.parseInt(date.replace("日", "")) > getMaxDay(
							y, m)) {
						date = getMaxDay(y, m) + "日";
					}
					break;
				case 1://以后
					if (y == year_now){
						for (int i = month_now; i <= 12; i++) {
							monthList.add(i + "月");
						}
						if (m <= month_now) {
							m = month_now;
							month = monthList.get(0);
							for (int i = day_now; i <= getMaxDay(y, m); i++) {
								dateList.add(i + "日");
							}
						} else {
							for (int i = 1; i <= getMaxDay(y, m); i++) {
								dateList.add(i + "日");
							}
						}
					}else {
						for (int i = 1; i <= 12; i++) {
							monthList.add(i + "月");
						}
						for (int i = 1; i <= getMaxDay(y, m); i++) {
							dateList.add(i + "日");
						}
					}
					if (Integer.parseInt(date.replace("日", "")) < day_now) {
						date = day_now + "日";
					}
					if (Integer.parseInt(date.replace("日", "")) > getMaxDay(
							y, m)) {
						date = getMaxDay(y, m) + "日";
					}
					break;
				}
				handler.sendEmptyMessage(1);// 刷新month数据
				handler.sendEmptyMessage(0);// 刷新day数据
			}
		});

		v_month.setData(monthList);
		v_month.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				month = text;

				int y = Integer.parseInt(year.substring(0, year.length() - 1));
				int m = Integer.parseInt(month.substring(0, month.length() - 1));
				dateList.clear();
				switch (type) {
				case 0://以前
					if (y == year_now && m == month_now) {
						for (int i = 1; i <= day_now; i++) {
							dateList.add(i + "日");
						}
					}else {
						for (int i = 1; i <= getMaxDay(y, m); i++) {
							dateList.add(i + "日");
						}
					}
					if (Integer.parseInt(date.replace("日", "")) > getMaxDay(
							y, m)) {
						date = getMaxDay(y, m) + "日";
					}
					break;
				case 1://以后
					if (y == year_now && m == month_now){
						for (int i = day_now; i <= getMaxDay(y, m); i++) {
							dateList.add(i + "日");
						}
					}else {
						for (int i = 1; i <= getMaxDay(y, m); i++) {
							dateList.add(i + "日");
						}
					}
					if (Integer.parseInt(date.replace("日", "")) < day_now) {
						date = day_now + "日";
					}
					if (Integer.parseInt(date.replace("日", "")) > getMaxDay(y, m)) {
						date = getMaxDay(y, m) + "日";
					}
					break;
				}
				handler.sendEmptyMessage(0);// 刷新day数据
			}
		});

		v_date.setData(dateList);
		v_date.setOnSelectListener(new onSelectListener() {
			public void onSelect(String text) {
				date = text;
			}

		});

		// 设置SelectPicPopupWindow的View
		this.setContentView(mTimeView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
	}

	public void setDayView() {
		Calendar calendar = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		int year_now = calendar.get(Calendar.YEAR);
		int month_now = calendar.get(Calendar.MONTH) + 1;
		int date_now = calendar.get(Calendar.DAY_OF_MONTH);
		v_year.setSelected(year_now + "年");
		v_month.setSelected(month_now + "月");
		v_date.setSelected(date_now + "日");
		year = year_now + "年";
		month = month_now + "月";
		date = date_now + "日";
	}

	OnTimeSetListener onTimeSetListener;

	public OnTimeSetListener getOnTimeSetListener() {
		return onTimeSetListener;
	}

	public void setOnTimeSetListener(OnTimeSetListener onTimeSetListener) {
		this.onTimeSetListener = onTimeSetListener;
	}

	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_dcancel:
				dismiss();
				break;
			case R.id.btn_dconfirm:
				String dateString = year + month + date;
				dateString = dateString.replace("年", "-").replace("月", "-")
						.replace("日", "");
				if (onTimeSetListener != null) {
					onTimeSetListener.onTimeSet(dateString);
				}
				dismiss();
				break;
			default:
				break;
			}
		}
	};

	// 得到某年某月最大天数
	public int getMaxDay(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, 1);
		c.add(Calendar.DAY_OF_YEAR, -1);
		return c.get(Calendar.DAY_OF_MONTH);
	}

}
