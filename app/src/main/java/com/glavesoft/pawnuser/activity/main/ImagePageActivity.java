package com.glavesoft.pawnuser.activity.main;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片查看器
 */
public class ImagePageActivity extends BaseActivity
{
	private ViewPager viewPager;
	private ArrayList<String> picUrlList;
	private MyAdapter adapter ;
	private View item ;
	private LayoutInflater inflater;
	private RadioGroup rg_picdh;
	private int selectPos;
	private String downLoadUrl;
	private Button btn_download;
	String path ;
	ArrayList<Bitmap> ab;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagepage);
		setView();
		initViewPager();
	}

	private void setView() {

		btn_download =  (Button) findViewById(R.id.btn_download);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		rg_picdh = (RadioGroup) findViewById(R.id.rg_picdh);
		path = Environment.getExternalStorageDirectory() + "/pawn_user/temp/";
		ab = new ArrayList<Bitmap>();

	}


	private void initViewPager() {
		picUrlList = getIntent().getStringArrayListExtra("picurlList");
		selectPos = getIntent().getIntExtra("selectPos", 0);
		inflater = LayoutInflater.from(this);

		if(picUrlList.size()>1){
			rg_picdh.setVisibility(View.VISIBLE);
		}else{
			rg_picdh.setVisibility(View.GONE);
		}

		//放导航栏
		for (int i = 0; i < picUrlList.size(); i++) {
			RadioButton rb = new RadioButton(this);
			rb.setButtonDrawable(R.drawable.selector_picdh);
			rg_picdh.addView(rb, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		}
		adapter = new MyAdapter(picUrlList);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new MyListener());
		viewPager.setCurrentItem(selectPos);
		viewPager.setOffscreenPageLimit(picUrlList.size());
		downLoadUrl = picUrlList.get(selectPos);
		((RadioButton)rg_picdh.getChildAt(selectPos)).setChecked(true);
	}

	/**
	 * 适配器，负责装配 、销毁 数据 和 组件 。
	 */
	private class MyAdapter extends PagerAdapter {

		private List<String> mList;
		RequestOptions options;
		public MyAdapter(List<String> list) {
			mList = list;
			options = new RequestOptions()
					.placeholder(com.foamtrace.photopicker.R.mipmap.default_error)
					.error(com.foamtrace.photopicker.R.mipmap.default_error)
					.priority(Priority.NORMAL)
					.diskCacheStrategy(DiskCacheStrategy.NONE);
		}
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		    ((ViewPager) container).removeView((View) object);
		    try {
//		    	ab.get(position).recycle();//本注释
			} catch (Exception e) {
			    System.out.println("==="+position);
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {

			final PhotoView image = new PhotoView(ImagePageActivity.this);

			final String path = picUrlList.get(position);
			final Uri uri;
			if (path.startsWith("http")) {
				uri = Uri.parse(path);
			} else {
				uri = Uri.fromFile(new File(path));
			}
			Glide.with(ImagePageActivity.this)
					.load(uri)
					.apply(options)
					.into(image);

//			DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.cacheInMemory(false).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
//				.imageScaleType(ImageScaleType.EXACTLY).build();
//			ImageLoader.getInstance().displayImage(picUrlList.get(position),image,options);

			((ViewPager) container).addView(image);

			PhotoViewAttacher mAttacher = new PhotoViewAttacher(image);
		    mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {//单击关闭
				@Override
				public void onViewTap(View arg0, float arg1, float arg2) {
					if(btn_download.getVisibility() == View.VISIBLE){
						btn_download.setVisibility(View.GONE);
					}else{
						for (Bitmap i : ab) {//本注释了
							i.recycle();//本注释了
						}//本注释了
						ImagePageActivity.this.finish();
					}

				}
			});

		    mAttacher.setOnLongClickListener(new OnLongClickListener() {//长按下载
				@Override
				public boolean onLongClick(View arg0) {
					//btn_download.setVisibility(View.VISIBLE);
					return false;
				}
			});

		    return image;
		}

	}
	 /**
	   * 动作监听器，可异步加载图片
	   *
	   */
	  private class MyListener implements OnPageChangeListener {

	    @Override
	    public void onPageScrollStateChanged(int state) {
	      if (state == 0) {
	        //new MyAdapter(null).notifyDataSetChanged();
	      }
	    }


	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }

	    @Override
	    public void onPageSelected(int position) {
			((RadioButton)rg_picdh.getChildAt(position)).setChecked(true);
			downLoadUrl = picUrlList.get(position);
	    }
	  }
}
