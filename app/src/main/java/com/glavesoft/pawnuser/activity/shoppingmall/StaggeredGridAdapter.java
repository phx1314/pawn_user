package com.glavesoft.pawnuser.activity.shoppingmall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.SubmitBuyActivity;
import com.glavesoft.pawnuser.activity.main.WebActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.StoreBannerInfo;
import com.glavesoft.pawnuser.mod.StoreGoodsInfo;
import com.glavesoft.util.ScreenUtils;
import com.glavesoft.view.FlexibleRoundedBitmapDisplayer;
import com.glavesoft.view.SlideShowView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.MyViewHolder> implements View.OnClickListener
{

	private List<StoreGoodsInfo> mDatas;
	private LayoutInflater mInflater;
	private List<Integer> mHeights;
	public  final int TYPE_HEADER = 0;
	public  final int TYPE_NORMAL = 1;
	private Context mContext;
	private View mHeaderView;
	private DisplayImageOptions options;
	private ArrayList<StoreBannerInfo> StoreBannerList=new ArrayList<>();
	private SlideShowView ssv_header_shoppingmall_pic;
	private LinearLayout ll_shoppingmall_zb,ll_shoppingmall_fc,ll_shoppingmall_hty,ll_shoppingmall_gudong,
			ll_shoppingmall_sh,ll_shoppingmall_cszb,ll_shoppingmall_zs,ll_shoppingmall_gd;
	RequestOptions options1;

	public interface OnItemClickLitener
	{
		void onItemClick(View view, int position);

		void onItemLongClick(View view, int position);
	}

	private OnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
	{
		this.mOnItemClickLitener = mOnItemClickLitener;
	}


	public void refreshDatas(List<StoreGoodsInfo>  data) {
		this.mDatas = data;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return mHeaderView == null ? mDatas.size() : mDatas.size() + 1;
	}

	public void setHeaderView(View headerView,ArrayList<StoreBannerInfo> StoreBannerList) {
		mHeaderView = headerView;
		this.StoreBannerList=StoreBannerList;
		notifyItemInserted(0);
	}

	public View getHeaderView() {
		return mHeaderView;
	}

	@Override
	public int getItemViewType(int position) {
		if (mHeaderView == null) return TYPE_NORMAL;
		if (position == 0) return TYPE_HEADER;
		return TYPE_NORMAL;
	}

	@Override
	public void onViewAttachedToWindow(MyViewHolder holder) {
		super.onViewAttachedToWindow(holder);
		ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
		if(lp != null
				&& lp instanceof StaggeredGridLayoutManager.LayoutParams) {
			StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
			p.setFullSpan(holder.getLayoutPosition() == 0);
		}
	}

	public StaggeredGridAdapter(Context context, List<StoreGoodsInfo> datas)
	{
		mInflater = LayoutInflater.from(context);
		mDatas = datas;
		mContext=context;
		options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).showStubImage(R.drawable.sy_bj)
			.showImageForEmptyUri(R.drawable.sy_bj).cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		options1 = new RequestOptions()
				.placeholder(com.foamtrace.photopicker.R.mipmap.default_error)
				.error(com.foamtrace.photopicker.R.mipmap.default_error)
				.priority(Priority.NORMAL)
				.diskCacheStrategy(DiskCacheStrategy.NONE);

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
	{
		MyViewHolder viewHolder;
		if (mHeaderView != null && viewType == TYPE_HEADER) {
			viewHolder = new MyViewHolder(mHeaderView);
			ssv_header_shoppingmall_pic = (SlideShowView) mHeaderView.findViewById(R.id.ssv_header_shoppingmall_pic);
			ll_shoppingmall_zb= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_zb);
			ll_shoppingmall_fc= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_fc);
			ll_shoppingmall_hty= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_hty);
			ll_shoppingmall_gudong= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_gudong);
			ll_shoppingmall_sh= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_sh);
			ll_shoppingmall_cszb= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_cszb);
			ll_shoppingmall_zs= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_zs);
			ll_shoppingmall_gd= (LinearLayout) mHeaderView.findViewById(R.id.ll_shoppingmall_gd);
		}else {
			View view = mInflater.inflate(R.layout.item_new_staggered, viewGroup, false);
			viewHolder = new MyViewHolder(view);
			viewHolder.iv_type= (ImageView) view.findViewById(R.id.iv_type);
			viewHolder.tv_type= (TextView) view.findViewById(R.id.tv_type);
			viewHolder.tv = (TextView) view.findViewById(R.id.tv_item_staggered_desc);
			viewHolder.tv_price_staggered = (TextView) view.findViewById(R.id.tv_price_staggered);
			viewHolder.image= (ImageView) view.findViewById(R.id.iv_item_staggered_icon);
			viewHolder.ll_item_staggered= (LinearLayout) view.findViewById(R.id.ll_item_staggered);
			viewHolder.tv_submit= (TextView) view.findViewById(R.id.tv_submit);
		}
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, int position)
	{

		if(getItemViewType(position) == TYPE_HEADER){

			if(StoreBannerList.size()>0){
				String[] headUrls = new String[StoreBannerList.size()] ;

				for (int i=0;i<StoreBannerList.size();i++){
					headUrls[i]=BaseConstant.Image_URL+StoreBannerList.get(i).getUrl();
				}
				ssv_header_shoppingmall_pic.initAndSetImagesUrl(headUrls,
						new SlideShowView.OnImageClickListener() {
							@Override
							public void onClick(View v, int position) {
								//0不跳转；1网址；2富文本；3认证商城商品详情页；4绝当商城商品详情页；5视频详情页
								if (StoreBannerList.get(position).getType().equals("1")){
									Intent intent = new Intent(mContext,WebActivity.class);
									intent.putExtra("titleName","详情");
									intent.putExtra("url",StoreBannerList.get(position).getContent());
									mContext.startActivity(intent);
								}else if (StoreBannerList.get(position).getType().equals("2")){
									Intent intent = new Intent(mContext,WebActivity.class);
									intent.putExtra("titleName","详情");
									intent.putExtra("url",StoreBannerList.get(position).getContent());
									mContext.startActivity(intent);
								}else if (StoreBannerList.get(position).getType().equals("3")){
									Intent intent = new Intent(mContext, GoodsDetailActivity.class);
									intent.putExtra("type","rz");
									intent.putExtra("id",StoreBannerList.get(position).getContent());
									mContext.startActivity(intent);
								}else if (StoreBannerList.get(position).getType().equals("4")){
									if(StoreBannerList.get(position).getState().equals("1")){
										Intent intent = new Intent(mContext, JdGoodsDetailActivity.class);
										intent.putExtra("id",StoreBannerList.get(position).getContent());
										mContext.startActivity(intent);
									}else{
										Intent intent = new Intent(mContext, GoodsDetailActivity.class);
										intent.putExtra("type","jd");
										intent.putExtra("id",StoreBannerList.get(position).getContent());
										mContext.startActivity(intent);
									}

								}else if (StoreBannerList.get(position).getType().equals("5")){

								}

							}
						});
			}

			ll_shoppingmall_zb.setOnClickListener(this);
			ll_shoppingmall_fc.setOnClickListener(this);
			ll_shoppingmall_hty.setOnClickListener(this);
			ll_shoppingmall_gudong.setOnClickListener(this);
			ll_shoppingmall_sh.setOnClickListener(this);
			ll_shoppingmall_cszb.setOnClickListener(this);
			ll_shoppingmall_zs.setOnClickListener(this);
			ll_shoppingmall_gd.setOnClickListener(this);
		}else{
			if(position>0){

				LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(holder.ll_item_staggered.getLayoutParams());
				if (position%2==0){
					lp1.setMargins(ScreenUtils.dp2px(mContext,4), ScreenUtils.dp2px(mContext,5),
							ScreenUtils.dp2px(mContext,8),  ScreenUtils.dp2px(mContext,5));
				}else{
					lp1.setMargins(ScreenUtils.dp2px(mContext,8), ScreenUtils.dp2px(mContext,5),
							ScreenUtils.dp2px(mContext,4),  ScreenUtils.dp2px(mContext,5));
				}
				holder.ll_item_staggered.setLayoutParams(lp1);
				position = position - 1;
				holder.tv.setText(mDatas.get(position).getTitle());
				holder.tv_price_staggered.setText("￥"+mDatas.get(position).getPrice());

				String imageurl=mDatas.get(position).getImg();

				int width= ScreenUtils.getInstance().getWidth();
				width=width-ScreenUtils.dp2px(mContext,24);
				width=width/2;
				ViewGroup.LayoutParams lp = holder.image.getLayoutParams();
				lp.width=width;
				lp.height = width*100/168;
				holder.image.setLayoutParams(lp);

				if(!imageurl.equals("")){
					ImageLoader.getInstance().displayImage(BaseConstant.Image_URL + imageurl,holder.image,options);
				}else{
					ImageLoader.getInstance().displayImage("",holder.image,options);
				}

//				if(!imageurl.equals("")){
//					holder.image.setVisibility(View.VISIBLE);
//					final String finalImageurl = imageurl;
//
//					ImageLoader.getInstance().loadImage(BaseConstant.Image_URL+imageurl,options, new SimpleImageLoadingListener(){
//
//						@Override
//						public void onLoadingComplete(String imageUri, View view,
//													  Bitmap loadedImage) {
//							super.onLoadingComplete(imageUri, view, loadedImage);
//
//							int width= ScreenUtils.getInstance().getWidth();
//							width=width-ScreenUtils.dp2px(mContext,28);
//							width=width/2;
//
//							LayoutParams lp = holder.image.getLayoutParams();
//							lp.width=width;
//							lp.height = width*loadedImage.getHeight()/loadedImage.getWidth();
//
//							holder.image.setLayoutParams(lp);
//							//holder.image.setImageBitmap(loadedImage);
//							setRoundedImage(
//									BaseConstant.Image_URL+ finalImageurl,
//									ScreenUtils.dp2px(mContext,5),
//									FlexibleRoundedBitmapDisplayer.CORNER_TOP_LEFT|FlexibleRoundedBitmapDisplayer.CORNER_TOP_RIGHT,
//									R.drawable.shape_coner_white2,
//									holder.image
//							);
//
//						}
//
//					});
//				}else{
//					holder.image.setVisibility(View.GONE);
//				}

				if(mDatas.get(position).getSource()!=null){
					if (mDatas.get(position).getSource().equals("3")){
						holder.tv_type.setBackgroundResource(R.drawable.shape_orange);
						holder.tv_type.setText("自营");
					}else if (mDatas.get(position).getSource().equals("4")){
						holder.tv_type.setBackgroundResource(R.drawable.shape_red1);
						holder.tv_type.setText("臻品");
					}else if (mDatas.get(position).getSource().equals("2")){
						holder.tv_type.setBackgroundResource(R.drawable.shape_green1);
						holder.tv_type.setText("绝当品");
					}
				}


				final int finalPosition = position;
				holder.itemView.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(mContext, GoodsDetailActivity.class);
						intent.putExtra("type","rz");
						intent.putExtra("id",mDatas.get(finalPosition).getId());
						mContext.startActivity(intent);
					}
				});

				holder.tv_submit.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if(BaseConstant.isLogin()){
							Intent intent=new Intent();
							intent.setClass(mContext,SubmitBuyActivity.class);
							intent.putExtra("type","goodsdetail");
							intent.putExtra("state","rz");
							intent.putExtra("storeGoodsInfo",mDatas.get(finalPosition));
							mContext.startActivity(intent);
						}else{
							mContext.startActivity(new Intent(mContext, LoginActivity.class));
						}


					}
				});
			}

		}
	}


//	public void addData(int position)
//	{
//		mDatas.add(position, "Insert One");
//		mHeights.add( (int) (100 + Math.random() * 300));
//		notifyItemInserted(position);
//	}

//	public void removeData(int position)
//	{
//		mDatas.remove(position);
//		notifyItemRemoved(position);
//	}

	class MyViewHolder extends ViewHolder
	{
		LinearLayout ll_item_staggered;
		TextView tv,tv_price_staggered,tv_submit,tv_type;
		ImageView image,iv_type;
		public MyViewHolder(View view)
		{
			super(view);
		}
	}

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

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent();
		switch (v.getId())
		{
			case R.id.ll_shoppingmall_zb:
				intent.setClass(mContext, StoreGoodsListActivity.class);
				intent.putExtra("type", "1");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
			case R.id.ll_shoppingmall_fc:
				intent.setClass(mContext, StoreGoodsListActivity.class);
				intent.putExtra("type", "2");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
			case R.id.ll_shoppingmall_hty:
				intent.setClass(mContext, StoreGoodsListActivity.class);
				intent.putExtra("type", "3");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
			case R.id.ll_shoppingmall_gudong:
				intent.setClass(mContext, JdTypeActivity.class);
				intent.putExtra("type", "4");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
			case R.id.ll_shoppingmall_sh:
				intent.setClass(mContext, StoreGoodsListActivity.class);
				intent.putExtra("type", "5");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
			case R.id.ll_shoppingmall_cszb:
				intent.setClass(mContext, JdTypeActivity.class);
				intent.putExtra("type", "6");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
			case R.id.ll_shoppingmall_zs:
				intent.setClass(mContext, StoreGoodsListActivity.class);
				intent.putExtra("type", "7");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
			case R.id.ll_shoppingmall_gd:
				intent.setClass(mContext, StoreGoodsListActivity.class);
				intent.putExtra("type", "8");
				intent.putExtra("state", "rz");
				mContext.startActivity(intent);
				break;
		}
	}


	public static void setMargins (View v, int l, int t, int r, int b) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins(l, t, r, b);
			v.requestLayout();
		}
	}
}