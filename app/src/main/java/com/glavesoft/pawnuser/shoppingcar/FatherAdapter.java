package com.glavesoft.pawnuser.shoppingcar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class FatherAdapter<T> extends BaseAdapter {

	public abstract BaseAdapter setList(List<T> list);

	@Override
	public abstract int getCount();

	@Override
	public abstract Object getItem(int position);

	@Override
	public abstract long getItemId(int position);

	@Override
	public abstract View getView(int position, View convertView,
                                 ViewGroup parent);

}
