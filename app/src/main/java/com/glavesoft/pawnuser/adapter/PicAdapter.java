package com.glavesoft.pawnuser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.glavesoft.pawnuser.R;
import com.glavesoft.util.ImageUtils;

import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/10/28
 * @company:常州宝丰
 */
public class PicAdapter extends BaseAdapter {
    Context context;
    int MAX_IMAGE_SIZE = 9;
    private ArrayList<String> urlList ;
    private ArrayList<String> imagePaths ;
    public PicAdapter(Context context,ArrayList<String> urlList,ArrayList<String> imagePaths){
        this.urlList=urlList;
        this.imagePaths=imagePaths;
        this.context=context;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        // 多返回一个用于展示添加图标
        if (urlList == null) {
            return 1;
        } else if (urlList.size() == MAX_IMAGE_SIZE) {
            return MAX_IMAGE_SIZE;
        } else {
            return urlList.size() + 1;
        }
    }

    public ArrayList<String> geturlLists() {
        return urlList;
    }

    public ArrayList<String> getimagePaths() {
        return imagePaths;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PicAdapter.ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pic_add, parent, false);
            holder = new PicAdapter.ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (PicAdapter.ViewHolder) convertView.getTag();
        }

        if (isShowAddItem(position)) {
            holder.item_iv_pic.setImageResource(R.color.transparent);
            holder.item_iv_del.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(urlList.get(position)).into(holder.item_iv_pic);
//            holder.item_iv_pic.setImageBitmap(ImageUtils.getBitmapByPath(urlList.get(position)));
            holder.item_iv_del.setVisibility(View.VISIBLE);
        }

        holder.item_iv_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                urlList.remove(position);
                imagePaths.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private boolean isShowAddItem(int position) {
        int size = urlList == null ? 0 : urlList.size();
        return position == size;
    }

    private class ViewHolder {
        ImageView item_iv_pic, item_iv_del;

        ViewHolder(View view) {
            item_iv_pic = (ImageView) view.findViewById(R.id.item_iv_pic);
            item_iv_del = (ImageView) view.findViewById(R.id.item_iv_del);
        }
    }
}
