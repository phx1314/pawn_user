package com.glavesoft.pawnuser.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.glavesoft.pawnuser.R;
import com.glavesoft.util.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2018/10/10
 * @company:常州宝丰
 */
public class UpdatepicAdapter extends BaseAdapter {
    Context context;
    int MAX_IMAGE_SIZE = 9;
    private ArrayList<String> urlList;
    private ArrayList<String> picList;
    private ArrayList<String> imagePaths;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    public UpdatepicAdapter(Context context, ArrayList<String> urlList, ArrayList<String> picList, ArrayList<String> imagePaths, int MAX_IMAGE_SIZE) {
        this.urlList = urlList;
        this.picList = picList;
        this.imagePaths = imagePaths;
        this.context = context;
        this.MAX_IMAGE_SIZE=MAX_IMAGE_SIZE;
    }

    public ImageLoader getImageLoader()
    {
        if (imageLoader == null)
        {
            imageLoader = ImageLoader.getInstance();
        }

        return imageLoader;
    }

    public DisplayImageOptions getImageLoaderOptions()
    {
        if(options == null)
        {
            options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).
                    cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        }
        return options;
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

    public ArrayList<String> getpicLists() {
        return picList;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_pic_add1, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (isShowAddItem(position)) {
            holder.item_iv_pic.setImageResource(R.drawable.jtp);
            holder.item_iv_del.setVisibility(View.GONE);
        } else {
            if(!urlList.get(position).equals("")){
                if(urlList.get(position).substring(0,4).equals("http")){
                    getImageLoader().displayImage(urlList.get(position),holder.item_iv_pic,getImageLoaderOptions());
                }else{
//                    holder.item_iv_pic.setImageBitmap(ImageUtils.getBitmapByPath(urlList.get(position)));
                    Glide.with(context).load(urlList.get(position)).into(holder.item_iv_pic);
                }
            }
            holder.item_iv_del.setVisibility(View.VISIBLE);
        }

        holder.item_iv_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(urlList.get(position).substring(0,4).equals("http")){
                    picList.remove(position);
                }else{
                    imagePaths.remove(position-picList.size());
                }
                urlList.remove(position);
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
            item_iv_pic = view.findViewById(R.id.item_iv_pic);
            item_iv_del = view.findViewById(R.id.item_iv_del);
        }
    }
}
