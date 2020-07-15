package com.glavesoft.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mdx.framework.Frame;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
/**
 * Created by DELL on 2019/10/16.
 */

public class GlideLoader {
    public static void loadImage(String url, ImageView img, int i) {
        Glide.with(Frame.CONTEXT).load(url).error(i) //异常时候显示的图片
                .placeholder(i) //加载成功前显示的图片
                .fallback(i).into(img);
    }
    public static void loadCircleCropImage(String url, ImageView img, int i) {
        Glide.with(Frame.CONTEXT).load(url).error(i) //异常时候显示的图片
                .placeholder(i) //加载成功前显示的图片
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .fallback(i).into(img);
    }

    public static void loadImage_error(String url, ImageView img) {
        Glide.with(Frame.CONTEXT).load(url).error(img.getDrawable()) //异常时候显示的图片
                .placeholder(img.getDrawable()).into(img);
    }


}
