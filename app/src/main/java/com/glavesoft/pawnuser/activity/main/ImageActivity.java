package com.glavesoft.pawnuser.activity.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author 严光
 * @date: 2018/5/31
 * @company:常州宝丰
 */
public class ImageActivity extends BaseActivity{

    private String url="";
    private PhotoView photoView;
    RequestOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initData();
        initView();
    }

    private void initData() {
        url=getIntent().getStringExtra("url");
    }

    private void initView() {
        options = new RequestOptions()
                .placeholder(com.foamtrace.photopicker.R.mipmap.default_error)
                .error(com.foamtrace.photopicker.R.mipmap.default_error)
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        photoView= (PhotoView) findViewById(R.id.photoview);

        if(url.equals("1")){
            Glide.with(this).load(R.drawable.yangzhang).into(photoView);
            PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);
            mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {//单击关闭
                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    finish();
                }
            });
        }else if(url.equals("2")){
            Glide.with(this).load(R.drawable.yangzhang1).into(photoView);
            PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);
            mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {//单击关闭
                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    finish();
                }
            });
        }else {
            Glide.with(this)
                    .load(url)
                    .apply(options)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //在这里添加一些图片加载完成的操作
                            PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);
                            mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {//单击关闭
                                @Override
                                public void onViewTap(View arg0, float arg1, float arg2) {
                                    finish();
                                }
                            });

                            mAttacher.setOnLongClickListener(new View.OnLongClickListener() {//长按下载
                                @Override
                                public boolean onLongClick(View arg0) {
                                    //btn_download.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            });

                            return false;
                        }
                    })
                    .into(photoView);
        }

    }

}
