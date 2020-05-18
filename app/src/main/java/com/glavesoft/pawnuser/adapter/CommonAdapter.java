package com.glavesoft.pawnuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.glavesoft.pawnuser.base.BaseApplication;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
	protected LayoutInflater mInflater;
	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId)
	{
		if(context!=null){
			this.mContext = context;
		}else{
			this.mContext = BaseApplication.getInstance().getApplicationContext();
		}
		this.mInflater = LayoutInflater.from(mContext);
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}
	 public void onDateChange(List<T> result) {
			this.mDatas = result;
			this.notifyDataSetChanged();
		}
	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final ViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();

	}

	public abstract void convert(ViewHolder helper, T item);

	private ViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent)
	{
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

}
