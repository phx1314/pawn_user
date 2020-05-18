package com.glavesoft.pawnuser.activity.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.VideoInfo;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.util.ShareUtil;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen
 * On 2016/02/07 01:20
 */
public class VideoListAdapter extends BaseAdapter{

    public static final String TAG = "JieCaoVideoPlayer";
    private ArrayList<VideoInfo> videoInfos=new ArrayList<>();
    Context context;
    private DisplayImageOptions  options,optionsHead;

    public VideoListAdapter(Context context,ArrayList<VideoInfo> videoInfos) {
        this.context = context;
        this.videoInfos=videoInfos;
        optionsHead = new DisplayImageOptions.Builder().showStubImage(R.drawable.tx).
                showImageForEmptyUri(R.drawable.tx)
                .cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
        options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).
                cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        return videoInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_videoview, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        View v_video= (View) convertView.findViewById(R.id.v_video);
        TextView item_video_name= (TextView) convertView.findViewById(R.id.item_video_name);
        item_video_name.setText(videoInfos.get(position).getTitle());
        viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
        viewHolder.jcVideoPlayer.setUp(BaseConstant.Video_URL+videoInfos.get(position).getVideo(), JCVideoPlayer.SCREEN_LAYOUT_LIST, videoInfos.get(position).getTitle());
        if(videoInfos.get(position).getImg()!=null&&!videoInfos.get(position).getImg().equals("")){
            ImageLoader.getInstance().displayImage(BaseConstant.Image_URL + videoInfos.get(position).getImg(), viewHolder.jcVideoPlayer.thumbImageView,options);
        }else{
            ImageLoader.getInstance().displayImage("", viewHolder.jcVideoPlayer.thumbImageView,options);
        }

        if(position==0){
            v_video.setVisibility(View.VISIBLE);
        }else{
            v_video.setVisibility(View.GONE);
        }

        TextView item_video_member= (TextView) convertView.findViewById(R.id.item_video_member);
        item_video_member.setText(videoInfos.get(position).getComCnt());
        item_video_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SingleVideoActivity.class);
                    intent.putExtra("url",BaseConstant.Video_URL+videoInfos.get(position).getVideo());
                    intent.putExtra("type",JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL);
                    intent.putExtra("name",videoInfos.get(position).getTitle());
                    intent.putExtra("id",videoInfos.get(position).getId());
//                    intent.putExtra("videoThumbs",videoInfos.get(position).getVideoThumb());
                context.startActivity(intent);
            }
        });
        ImageView item_video_share= (ImageView) convertView.findViewById(R.id.item_video_share);
        item_video_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil share=new ShareUtil(context,videoInfos.get(position).getId());
                share.showSharePopupWindow();
            }
        });
        return convertView;
    }
    class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
//    private Bitmap createVideoThumbnail(String url, int width, int height) {
//        Bitmap bitmap = null;
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
//        try {
//            if (Build.VERSION.SDK_INT >= 14) {
//                retriever.setDataSource(url, new HashMap<String, String>());
//            } else {
//                retriever.setDataSource(url);
//            }
//            bitmap = retriever.getFrameAtTime();
//        } catch (IllegalArgumentException ex) {
//            // Assume this is a corrupt video file
//        } catch (RuntimeException ex) {
//            // Assume this is a corrupt video file.
//        } finally {
//            try {
//                retriever.release();
//            } catch (RuntimeException ex) {
//                // Ignore failures while cleaning up.
//            }
//        }
//        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
//            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
//                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//        }
//        return bitmap;
//    }

    /*
     * 设置图片---自定义图片4个角中的指定角为圆角
     * @param url 图片的url
     * @param cornerRadius 圆角像素大小
     * @param corners 自定义圆角:<br>
     * 以下参数为FlexibleRoundedBitmapDisplayer中静态变量:<br>
     * CORNER_NONE　无圆角<br>
     * CORNER_ALL 全为圆角<br>
     * CORNER_TOP_LEFT | CORNER_TOP_RIGHT | CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT　指定圆角（选其中若干组合  ） <br>
     * @param image url为空时加载该图片
     * @param imageView 要设置图片的ImageView
     */

    public void setRoundedImage(String url, int cornerRadius, int corners, int image, ImageView imageView) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(image).showStubImage(image)
                .showImageForEmptyUri(image)//url为空时显示的图片
                .showImageOnFail(image)//加载失败显示的图片
                .cacheInMemory()//内存缓存
                .cacheOnDisc()//磁盘缓存
                .displayer(new FlexibleRoundedBitmapDisplayer(cornerRadius,corners)) // 自定义增强型BitmapDisplayer
                .build();
        imageLoader.displayImage(url, imageView, options);

    }
}
