package vn.tungdx.mediapicker.imageloader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import vn.tungdx.mediapicker.R;

/**
 * @author TUNGDX
 */

public class MediaImageLoaderImpl implements MediaImageLoader {

    public MediaImageLoaderImpl(Context context) {
    }

    @Override
    public void displayImage(Uri uri, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.picker_imageloading)
                .error(R.color.picker_imageloading)
                .priority(Priority.NORMAL)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(imageView.getContext())
                .load(uri)
                .apply(options)
                .into(imageView);
    }
}