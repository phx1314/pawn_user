package com.glavesoft.view;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.glavesoft.pawnuser.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

/**
 *  滑动广告栏
 *
 */
public class SlideShowView extends FrameLayout implements Callback
{
	private String[] imageMods;
	private OnImageClickListener imageClickListener;
	private ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
	
	private ScheduledExecutorService scheduledExecutorService;
	private Handler handler = new Handler(this);
	
	private int lastDotIndex, currentPageIndex;
	private boolean isChanged = false, isMulti = false;

	private ViewPager viewPager;
	private ViewGroup dotsLayout;

	private Context context;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public SlideShowView(Context context)
	{
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.context = context;

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.sy_bj).showImageForEmptyUri(R.drawable.sy_bj).
			cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public void initAndSetImagesUrl(String[] imageMods, OnImageClickListener imageClickListener)
	{
		stopPlay();
		
		this.imageMods = imageMods;
		this.imageClickListener = imageClickListener;
		
		initUI(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		if(isMulti)
		{
			switch (ev.getAction())
			{
			case KeyEvent.ACTION_DOWN:
				stopPlay();
				break;
			case KeyEvent.ACTION_UP:
				startPlay();
				break;
			}
		}

		return super.dispatchTouchEvent(ev);
	}

	private void initUI(Context context)
	{
		isMulti = imageMods.length > 1;
		
		LayoutInflater.from(context).inflate(R.layout.ss_slideshow, this, true);
		viewPager = (ViewPager) findViewById(R.id.ssv_ll_vp_images);
		viewPager.removeAllViews();
		dotsLayout = (ViewGroup) findViewById(R.id.ssv_ll_dots);
		dotsLayout.removeAllViews();
		
		imageViewList.clear();
		
		lastDotIndex = 0;
		currentPageIndex = 1;

		// 增加第1个界面,实际上他显示的是最后一个界面
		addImageView(imageMods.length - 1);
		
		for (int i = 0; i < imageMods.length; i++)
		{
			addImageView(i);
			addDot(i);
		}
		
		// 增加最后一个界面，实际上他显示的是第一个界面
		addImageView(0);

		viewPager.setAdapter(new CustomPagerAdapter(imageViewList));
		viewPager.setOnPageChangeListener(new CustomPageChangeListener());

		viewPager.setFocusable(true);
		viewPager.setCurrentItem(currentPageIndex, false);
		
		if(isMulti)
		{
			startPlay();
		}
	}

	private void addImageView(final int i)
	{
		ImageView view = new ImageView(context);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		view.setTag(imageMods[i]);
		//view.setBackgroundResource(R.drawable.d_ad_default);
		view.setScaleType(ScaleType.FIT_XY);
		imageViewList.add(view);
		view.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				imageClickListener.onClick(v, i);
			}
		});
	}
	
	private void addDot(int i)
	{
		ImageView dotView = new ImageView(context);
		dotView.setBackgroundResource(i == 0 ? R.drawable.lb_yd_: R.drawable.lb_yd);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(10, 0, 10, 0);
		dotsLayout.addView(dotView, params);
	}
	
	private void setCurrentDot(int positon)
	{
		// 界面实际显示的序号是第1, 2, 3。而点的序号应该是0, 1, 2.所以减1.
		positon = positon - 1;
		if (positon < 0 || positon > imageViewList.size() - 1 || lastDotIndex == positon)
		{
			return;
		}

		dotsLayout.getChildAt(positon).setBackgroundResource(R.drawable.lb_yd_);
		dotsLayout.getChildAt(lastDotIndex).setBackgroundResource(R.drawable.lb_yd);
		
		lastDotIndex = positon;
	}
	
	private class CustomPagerAdapter extends PagerAdapter
	{
		private ArrayList<ImageView> viewList;

		public CustomPagerAdapter(ArrayList<ImageView> viewList)
		{
			this.viewList = viewList;
		}

		@Override
		public int getCount()
		{
			if (viewList != null)
			{
				return viewList.size();
			}
			return 0;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			ImageView imageView = imageViewList.get(position);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			//imageView.setImageResource(R.drawable.sy_bj);
			imageLoader.displayImage(imageView.getTag().toString(), imageView, options);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return (view == object);
		}
		
		@Override
		public int getItemPosition(Object object)
		{
			return POSITION_NONE;
		}
	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 */
	private class CustomPageChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int state)
		{
			if (ViewPager.SCROLL_STATE_IDLE == state)
			{
				if (isChanged)
				{
					isChanged = false;
					viewPager.setCurrentItem(currentPageIndex, false);
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{

		}

		@Override
		public void onPageSelected(int pos)
		{
			isChanged = true;
			
			if (pos > imageMods.length)
			{
				currentPageIndex = 1;
			}
			else if (pos < 1)
			{
				currentPageIndex = imageMods.length;
			}
			else
			{
				currentPageIndex = pos;
			}
			
			setCurrentDot(currentPageIndex);
		}
	}

	/**
	 * 执行轮播图切换任务
	 */
	private class SlideShowTask implements Runnable
	{
		@Override
		public void run()
		{
			currentPageIndex = (currentPageIndex + 1) % (imageViewList.size() + 2);
			handler.obtainMessage().sendToTarget();
		}
	}

	/**
	 * 停止轮播图切换
	 */
	private void stopPlay()
	{
		if(scheduledExecutorService != null && !scheduledExecutorService.isShutdown())
		{
			scheduledExecutorService.shutdownNow();
		}
	}

	/**
	 * 开始轮播图切换
	 */
	private void startPlay()
	{
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleWithFixedDelay(new SlideShowTask(), 3, 3, TimeUnit.SECONDS);
	}

	/**
	 * 销毁ImageView资源，回收内存
	 */
	public void destoryBitmaps()
	{
		if (imageViewList != null)
		{
			for (int i = 0; i < imageViewList.size(); i++)
			{
				Drawable drawable = imageViewList.get(i).getDrawable();
				if (drawable != null)
				{
					// 解除drawable对view的引用
					drawable.setCallback(null);
				}
			}
		}
	}

	public void destroy()
	{
		System.out.println("SlideShowView销毁");
		
		stopPlay();
		destoryBitmaps();
	}
	
	@Override
	public boolean handleMessage(Message msg)
	{
		viewPager.setCurrentItem(currentPageIndex, false);

		return false;
	}
	
	public interface OnImageClickListener
	{
		public void onClick(View v, int position);
	}
}
