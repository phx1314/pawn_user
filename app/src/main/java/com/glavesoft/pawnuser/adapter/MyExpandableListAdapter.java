package com.glavesoft.pawnuser.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glavesoft.F;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.activity.main.GoodsDetailActivity;
import com.glavesoft.pawnuser.activity.main.ShopcarSubmitActivity;
import com.glavesoft.pawnuser.activity.main.StoreActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.StoreGoodsListActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.frg.FrgProductDetail;
import com.glavesoft.pawnuser.mod.ShopCar;
import com.glavesoft.pawnuser.shoppingcar.OnShoppingCartChangeListener;
import com.glavesoft.pawnuser.shoppingcar.ShoppingCartBiz;
import com.glavesoft.util.GlideLoader;
import com.glavesoft.view.UIAlertView;
import com.mdx.framework.activity.TitleAct;
import com.mdx.framework.utility.Helper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * * ---------神兽保佑 !---------
 * <p/>
 * ... ┏┓        ┏┓
 * ..┏┛┻━━━━┛┻┓
 * .┃              ┃
 * ┃      ━       ┃
 * ┃  ┳┛  ┗┳   ┃
 * ┃              ┃
 * ┃      ┻      ┃
 * ┃              ┃
 * ┗━┓      ┏━┛
 * ... ┃      ┃
 * .. ┃      ┃
 * . ┃      ┗━━━┓
 * ┃              ┣┓
 * ┃             ┏┛
 * ┗┓┓┏━┳┓┏┛
 * . ┃┫┫  ┃┫┫
 * .┗┻┛  ┗┻┛
 * <p/>
 * Created by 绯若虚无 on 2015/10/10.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ShopCar> mListGoods = new ArrayList<>();
    private OnShoppingCartChangeListener mChangeListener;
    private boolean isSelectAll = false;

    DisplayImageOptions options ;

    public MyExpandableListAdapter(Context context) {
        mContext = context;
        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_trans).showImageForEmptyUri(R.drawable.bg_trans).
                cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    public void setList(List<ShopCar> mListGoods) {
        this.mListGoods = mListGoods;
        setSettleInfo();
    }

    public void setOnShoppingCartChangeListener(OnShoppingCartChangeListener changeListener) {
        this.mChangeListener = changeListener;
    }

    public View.OnClickListener getAdapterListener() {
        return listener;
    }

    @Override
    public int getGroupCount() {
        return mListGoods.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListGoods.get(groupPosition).getGoods().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListGoods.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mListGoods.get(groupPosition).getGoods().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_group_test, parent, false);
            holder.tvGetCoupon= (TextView) convertView.findViewById(R.id.tvGetCoupon);
            holder.tvGroup = (TextView) convertView.findViewById(R.id.tvShopNameGroup);
            holder.tvEdit = (TextView) convertView.findViewById(R.id.tvEdit);
            holder.ivCheckGroup = (ImageView) convertView.findViewById(R.id.ivCheckGroup);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tvGroup.setText(mListGoods.get(groupPosition).getOrgName());
        ShoppingCartBiz.checkItem(mListGoods.get(groupPosition).isGroupSelected(), holder.ivCheckGroup);
        boolean isEditing = mListGoods.get(groupPosition).isEditing();
//        boolean isEditing=false;
        if (isEditing) {
            holder.tvEdit.setText("完成");
        } else {
            holder.tvEdit.setText("编辑");
        }
        holder.tvGroup.setTag(mListGoods.get(groupPosition).getOrgId()+"");
        holder.ivCheckGroup.setTag(groupPosition);
        holder.ivCheckGroup.setOnClickListener(listener);
        holder.tvGetCoupon.setOnClickListener(listener);
        holder.tvEdit.setTag(groupPosition);
        holder.tvEdit.setOnClickListener(listener);
        holder.tvGroup.setOnClickListener(listener);
        return convertView;
    }

    /**
     * child view
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_elv_child_test, parent, false);
            holder.rl_item_shopcar= (RelativeLayout) convertView.findViewById(R.id.rl_item_shopcar);
            holder.tvChild = (TextView) convertView.findViewById(R.id.tvItemChild);
            holder.ivGoods= (ImageView) convertView.findViewById(R.id.ivGoods);
            holder.tvDel = (TextView) convertView.findViewById(R.id.tvDel);
            holder.ivCheckGood = (ImageView) convertView.findViewById(R.id.ivCheckGood);
            holder.llGoodInfo = (LinearLayout) convertView.findViewById(R.id.llGoodInfo);
            holder.ivAdd = (ImageView) convertView.findViewById(R.id.ivAdd);
            holder.ivReduce = (ImageView) convertView.findViewById(R.id.ivReduce);
            holder.tvGoodsParam = (TextView) convertView.findViewById(R.id.tvGoodsParam);
            holder.tvPriceNew = (TextView) convertView.findViewById(R.id.tvPriceNew);
            holder.tvPriceOld = (TextView) convertView.findViewById(R.id.tvPriceOld);
            holder.tvPriceOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//数字被划掉效果
            holder.tvNum = (TextView) convertView.findViewById(R.id.tvNum);
            holder.tvNum2 = (TextView) convertView.findViewById(R.id.tvNum2);
            holder.rl_all= (RelativeLayout) convertView.findViewById(R.id.rl_all);
            holder.tv_isonline= (TextView) convertView.findViewById(R.id.tv_online);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        ShopCar.GoodsBean goods = mListGoods.get(groupPosition).getGoods().get(childPosition);

        if (goods.getIsOnline()!=1){
            //holder.rl_all.setVisibility(View.GONE);
            holder.tv_isonline.setVisibility(View.VISIBLE);
        }else{
            holder.tv_isonline.setVisibility(View.GONE);
        }
        boolean isChildSelected = mListGoods.get(groupPosition).getGoods().get(childPosition).isChildSelected();
        boolean isEditing = goods.isEditing();
        String priceNew = "¥" + goods.getGoodsPrice();
//        String priceOld = "¥" + goods.getMkPrice();
        String num = goods.getNum()+"";
//        String pdtDesc = goods.getPdtDesc();
        String pdtDesc ="描述";
        String goodName = mListGoods.get(groupPosition).getGoods().get(childPosition).getGoodsName();
        if(!goods.getGoodsImg().equals("")) {
//            ImageLoader.getInstance().displayImage(BaseConstant.Image_URL+goods.getGoodsImg(),  holder.ivGoods, options);
            GlideLoader.loadRoundImage(BaseConstant.Image_URL + goods.getGoodsImg(), holder.ivGoods, R.drawable.sy_bj);
        }else{
            ImageLoader.getInstance().displayImage("",  holder.ivGoods, options);
        }
        holder.ivCheckGood.setTag(groupPosition + "," + childPosition);
        holder.tvChild.setText(goodName);
        holder.tvPriceNew.setText(priceNew);
//        holder.tvPriceOld.setText(priceOld);
        holder.tvNum.setText("X " + num);
        holder.tvNum2.setText(num);
        holder.tvGoodsParam.setText(pdtDesc);

        holder.ivAdd.setTag(goods);
        holder.ivReduce.setTag(goods);
        holder.tvDel.setTag(groupPosition + "," + childPosition);
        holder.tvDel.setTag(groupPosition + "," + childPosition);
        holder.ivGoods.setTag(goods);
        holder.llGoodInfo.setTag(goods.getGoodsId()+"");
        holder.rl_item_shopcar.setTag(groupPosition + "," + childPosition);
        ShoppingCartBiz.checkItem(isChildSelected, holder.ivCheckGood);
        if (isEditing) {
            holder.llGoodInfo.setVisibility(View.GONE);
        } else {
            holder.llGoodInfo.setVisibility(View.VISIBLE);
        }
        holder.rl_item_shopcar.setOnClickListener(listener);
        holder.ivCheckGood.setOnClickListener(listener);
        holder.tvDel.setOnClickListener(listener);
        holder.ivAdd.setOnClickListener(listener);
        holder.ivReduce.setOnClickListener(listener);
        holder.llGoodInfo.setOnClickListener(listener);
        holder.ivGoods.setOnClickListener(listener);
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //main
                case R.id.ivSelectAll:
                    isSelectAll = ShoppingCartBiz.selectAll(mListGoods, isSelectAll, (ImageView) v);
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
//                case R.id.tvEditAll:
//                    break;
                case R.id.btnSettle:

                    if (ShoppingCartBiz.hasSelectedGoods(mListGoods)) {
                        Intent intent=new Intent();
                        if(BaseConstant.isLogin()){
                            //todo
                            ArrayList<String> ids=ShoppingCartBiz.getShoppingIds(mListGoods);
                            ArrayList<String> nums=ShoppingCartBiz.getShoppingNUms(mListGoods);
                            intent=new Intent();
                            intent.setClass(mContext,ShopcarSubmitActivity.class);
                            intent.putExtra("type","goodsdetail");
                            intent.putExtra("state","rz");
                            intent.putStringArrayListExtra("ids", ids);
                            intent.putStringArrayListExtra("nums", nums);
                            mContext.startActivity(intent);
                        }else{
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        }
                        //ToastHelper.getInstance()._toast("结算跳转");
                    } else {
                        Toast.makeText(mContext,"亲，先选择商品！",Toast.LENGTH_SHORT).show();
                       // ToastHelper.getInstance()._toast("亲，先选择商品！");
                    }
                    //group
                    break;
                case R.id.tvEdit://切换界面，属于特殊处理，假如没打算切换界面，则不需要这块代码
                    int groupPosition2 = Integer.parseInt(String.valueOf(v.getTag()));
                    boolean isEditing = !(mListGoods.get(groupPosition2).isEditing());
                    mListGoods.get(groupPosition2).setIsEditing(isEditing);
                    for (int i = 0; i < mListGoods.get(groupPosition2).getGoods().size(); i++) {
                        mListGoods.get(groupPosition2).getGoods().get(i).setIsEditing(isEditing);
                    }
                    notifyDataSetChanged();
                    break;
                case R.id.ivCheckGroup:
                    int groupPosition3 = Integer.parseInt(String.valueOf(v.getTag()));
                    isSelectAll = ShoppingCartBiz.selectGroup(mListGoods, groupPosition3);
                    selectAll();
                    setSettleInfo();
                    notifyDataSetChanged();
                    break;
                //child
                case R.id.ivCheckGood:
                    String tag = String.valueOf(v.getTag());
                    if (tag.contains(",")) {
                        String s[] = tag.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        isSelectAll = ShoppingCartBiz.selectOne(mListGoods, groupPosition, childPosition);
                        selectAll();
                        setSettleInfo();
                        notifyDataSetChanged();
                    }
                    break;
                case R.id.tvDel:
                    String tagPos = String.valueOf(v.getTag());
                    if (tagPos.contains(",")) {
                        String s[] = tagPos.split(",");
                        int groupPosition = Integer.parseInt(s[0]);
                        int childPosition = Integer.parseInt(s[1]);
                        showDelDialog(groupPosition, childPosition);
                    }
                    break;
                case R.id.ivAdd:
                    ShoppingCartBiz.addOrReduceGoodsNum(true, (ShopCar.GoodsBean) v.getTag(), ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                    setSettleInfo();
                    break;
                case R.id.ivReduce:
                    ShopCar.GoodsBean goodsBean=(ShopCar.GoodsBean) v.getTag();
                    if (goodsBean.getNum()!=1){
                        ShoppingCartBiz.addOrReduceGoodsNum(false, goodsBean, ((TextView) (((View) (v.getParent())).findViewById(R.id.tvNum2))));
                        setSettleInfo();
                    }else {
                        Toast.makeText(mContext,"不可以再减",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.llGoodInfo:
                    String id = (String) v.getTag();
                    if (id!=null) {
//                        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
//                        intent.putExtra("id",id+"");
//                        intent.putExtra("type","rz");
//                        mContext.startActivity(intent);
                        F.INSTANCE.go2GoodeDetail(mContext,  id+"","rz");
                    }
                    break;
                case R.id.tvShopNameGroup:
                    String storeId = (String) v.getTag();
                    if (storeId!=null) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, StoreActivity.class);
                        intent.putExtra("storeid", storeId);
                        mContext.startActivity(intent);
                    }

                    break;
                case R.id.ivGoods:
                    ShopCar.GoodsBean goodsBean1 = (ShopCar.GoodsBean) v.getTag();
                    if (goodsBean1!=null) {
//                        Intent intent = new Intent(mContext, GoodsDetailActivity.class);
//                        intent.putExtra("id",goodsBean1.getGoodsId()+"");
//                        intent.putExtra("type","rz");
//                        mContext.startActivity(intent);
                        F.INSTANCE.go2GoodeDetail(mContext,   goodsBean1.getGoodsId()+"","rz");
                    }
                    break;
                case R.id.tvGetCoupon:
                    Toast.makeText(mContext,"暂无可用优惠券",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void selectAll() {
        if (mChangeListener != null) {
            mChangeListener.onSelectItem(isSelectAll);
        }
    }

    private void setSettleInfo() {
        String[] infos = ShoppingCartBiz.getShoppingCount(mListGoods);
        //删除或者选择商品之后，需要通知结算按钮，更新自己的数据；
        if (mChangeListener != null && infos != null) {
            mChangeListener.onDataChange(infos[0], infos[1]);
        }
    }

    private void showDelDialog(final int groupPosition, final int childPosition) {
        final UIAlertView delDialog = new UIAlertView(mContext, "温馨提示", "确认删除该商品吗?",
                "取消", "确定");
        delDialog.show();

        delDialog.setClicklistener(new UIAlertView.ClickListenerInterface() {

                                       @Override
                                       public void doLeft() {
                                           delDialog.dismiss();
                                       }

                                       @Override
                                       public void doRight() {
                                           String productID = mListGoods.get(groupPosition).getGoods().get(childPosition).getGoodsId()+"";
                                           ShoppingCartBiz.delGood(productID);
                                           delGoods(groupPosition, childPosition);
                                           setSettleInfo();
                                           notifyDataSetChanged();
                                           delDialog.dismiss();
                                       }
                                   }
        );
    }

    private void delGoods(int groupPosition, int childPosition) {
        mListGoods.get(groupPosition).getGoods().remove(childPosition);
        if (mListGoods.get(groupPosition).getGoods().size() == 0) {
            mListGoods.remove(groupPosition);
        }
        notifyDataSetChanged();
    }

    class GroupViewHolder {
        TextView tvGetCoupon;
        TextView tvGroup;
        TextView tvEdit;
        ImageView ivCheckGroup;
    }

    class ChildViewHolder {
        TextView tv_isonline;
        RelativeLayout rl_all;
        RelativeLayout rl_item_shopcar;
        /** 商品名称 */
        TextView tvChild;
        /** 商品规格 */
        TextView tvGoodsParam;
        /** 选中 */
        ImageView ivCheckGood;
        /** 非编辑状态 */
        LinearLayout llGoodInfo;
        /** +1 */
        ImageView ivAdd;
        /** -1 */
        ImageView ivReduce;
        ImageView ivGoods;
        /** 删除 */
        TextView tvDel;
        /** 新价格 */
        TextView tvPriceNew;
        /** 旧价格 */
        TextView tvPriceOld;
        /** 商品状态的数量 */
        TextView tvNum;
        /** 编辑状态的数量 */
        TextView tvNum2;
    }
}
