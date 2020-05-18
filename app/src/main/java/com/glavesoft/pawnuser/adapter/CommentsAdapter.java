package com.glavesoft.pawnuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.main.ImagePageActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.GoodsCommentsInfo;
import com.glavesoft.util.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sinyu on 2018/7/31.
 */

public class CommentsAdapter extends BaseMultiItemQuickAdapter<GoodsCommentsInfo, BaseViewHolder> {


    private Context context;
    public static final int TYPE1 = 1;
    private ImageView iv_comment_star_1, iv_comment_star_2, iv_comment_star_3, iv_comment_star_4, iv_comment_star_5;

    private List<ImageView> starList;

    public CommentsAdapter(List<GoodsCommentsInfo> data, Context context) {
        super(data);
        this.context = context;

        addItemType(TYPE1, R.layout.item_goods_comments);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsCommentsInfo item) {
        switch (helper.getItemViewType()) {
            case 1:
                helper.setText(R.id.username, item.getUserName());
                helper.setText(R.id.desc, item.getInfo());
                helper.setText(R.id.tv_goodsname, item.getGoodsName());
                helper.setText(R.id.time, item.getCreateTime());
                helper.setText(R.id.choose, "默认");
                if(!StringUtils.isEmpty(item.getUserHeadImg())) {
                    DisplayImageOptions imageOptions = DisplayImageOptions.createSimple();
                    ImageLoader.getInstance().displayImage(BaseConstant.Image_URL+item.getUserHeadImg(), (ImageView) helper.getView(R.id.head_img), imageOptions);
                }
                starList = new ArrayList<>();
                starList.add(iv_comment_star_1 = (ImageView) helper.getView(R.id.iv_comment_star_1));
                starList.add(iv_comment_star_2 = (ImageView) helper.getView(R.id.iv_comment_star_2));
                starList.add(iv_comment_star_3 = (ImageView) helper.getView(R.id.iv_comment_star_3));
                starList.add(iv_comment_star_4 = (ImageView) helper.getView(R.id.iv_comment_star_4));
                starList.add(iv_comment_star_5 = (ImageView) helper.getView(R.id.iv_comment_star_5));
                int currentStarCount=item.getScore();
                for (int i = 0, len = starList.size(); i < len; i++) {
                    starList.get(i).setImageResource(i < currentStarCount ? R.mipmap.icon_comment_star_red : R.mipmap.icon_comment_star_gray);
                }
                if (!item.getImg().equals("")){
                    String[] images=item.getImg().split(",");
                    for (int i=0;i<images.length;i++){
                        images[i]= BaseConstant.Image_URL+images[i];
                    }
                    InitFlowLayout(helper, images);
                }

                break;
        }
    }

    private void InitFlowLayout(BaseViewHolder helper, final String[] images) {

        final TagFlowLayout flowLayout = helper.getView(R.id.flowview);
        final LayoutInflater mInflater = LayoutInflater.from(context);
        flowLayout.setAdapter(new TagAdapter<String>(images) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                ImageView imageView = (ImageView) mInflater.inflate(R.layout.comments_img,
                        flowLayout, false);
                Glide.with(context).load(s).into(imageView);
                return imageView;
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                //Toast.makeText(context, images[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(context,
                        ImagePageActivity.class);
                ArrayList<String> picurlList=new ArrayList<>();
                for (int i=0;i<images.length;i++){
                    picurlList.add(images[i]);
                }
                intent.putExtra("picurlList", picurlList);
                intent.putExtra("selectPos", position);
                context.startActivity(intent);
                return true;
            }
        });
    }
}
