//
//  FrgProductDetail
//
//  Created by 86139 on 2020-06-05 09:51:33
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import com.glavesoft.F
import com.glavesoft.F.list_fx
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.activity.login.LoginActivity
import com.glavesoft.pawnuser.activity.main.SubmitBuyActivity
import com.glavesoft.pawnuser.activity.pawn.GoodsCommentsActivity
import com.glavesoft.pawnuser.activity.video.SingleVideoActivity
import com.glavesoft.pawnuser.ada.AdaProductDetail
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.mod.StoreGoodsInfo
import com.glavesoft.util.CommonUtils
import com.glavesoft.util.GlideLoader
import com.glavesoft.view.CustomToast
import com.sobot.chat.SobotApi
import com.sobot.chat.api.enumtype.SobotChatTitleDisplayMode
import com.sobot.chat.api.model.ConsultingContent
import com.sobot.chat.api.model.Information
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
import kotlinx.android.synthetic.main.frg_product_detail.*
import kotlinx.android.synthetic.main.item_head.view.*


class FrgProductDetail : BaseFrg() {
    lateinit var p_id: String
    var type: String = "rz"
    lateinit var item: StoreGoodsInfo
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_product_detail)
        p_id = activity!!.intent.getStringExtra("id")
        type = activity!!.intent.getStringExtra("type")
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        when (type) {
            0 -> {
                val intent = Intent(activity, SingleVideoActivity::class.java)
                intent.putExtra(
                    "url",
                    BaseConstant.Video_URL + item.bannerVideo
                )
                intent.putExtra("type", JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL)
                intent.putExtra("name", item.title)
                intent.putExtra("id", p_id)
                startActivity(intent)
            }
        }
    }

    override fun initView() {
        mTextView_pj.setOnClickListener {
            startActivity(
                Intent(context, GoodsCommentsActivity::class.java).putExtra(
                    "type",
                    "goods"
                ).putExtra("goodsid", p_id)
            )
        }
        mTextView_share.setOnClickListener {
            load(
                F.gB().getShareText(
                    p_id, if (type == "rz") "1" else "2"
                ), "getShareText"
            )
        }
        mLinearLayout_addcart.setOnClickListener {
            if (BaseConstant.isLogin()) {
                load(
                    F.gB().updateCart(
                        p_id
                    ), "updateCart"
                )
            } else {
                startActivity(Intent(context, LoginActivity::class.java))
            }
        }
        mTextView_gm.setOnClickListener {
            if (BaseConstant.isLogin()) {
                startActivity(
                    Intent(context, SubmitBuyActivity::class.java).putExtra(
                        "type",
                        "goodsdetail"
                    ).putExtra("state", type).putExtra("storeGoodsInfo", item)
                )
            }
        }
        mTextView_qw.setOnClickListener {
            mTextView_content.maxLines = Int.MAX_VALUE
            mTextView_qw.visibility = View.GONE
        }
    }

    override fun loaddata() {
        load(
            F.gB().storeGoodsDetail(
                p_id
            ), "storeGoodsDetail"
        )
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "updateCart") {
            if (data == "1") {
                F.toast("添加成功")
            } else {
                F.toast("库存不足")
            }
        } else if (method == "getShareText") {
            try {
                //获取剪贴板管理器
                val cm =
                    context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 创建普通字符型ClipData
                val mClipData =
                    ClipData.newPlainText("Label", data)
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData)
                BaseConstant.isCopy = true
                CustomToast.show("已复制口令，快去分享给好友吧！")
            } catch (e: Exception) {
            }
        } else if (method == "storeGoodsDetail") {
            item = F.data2Model(data, StoreGoodsInfo::class.java)
            mLinearLayout.visibility = View.VISIBLE
            mTextView_name.text = item.orgName
            mTextView_xj_reason.text = "下架原因：" + item.reasonOfDismounting
            mTextView_p_name.text = item.title
            mTextView_content.text = item.wordDescript
            if (item.source == "6") {
                mTextView_renzheng_type.text = "个人认证"
            } else if (item.source == "7") {
                mTextView_renzheng_type.text = "企业认证"
            } else if (item.source == "1") {
                mTextView_renzheng_type.text = "平台"
            } else if (item.source == "2") {
                mTextView_renzheng_type.text = "机构"
            } else if (item.source == "3") {
                mTextView_renzheng_type.text = "服务商"
            } else if (item.source == "4") {
                mTextView_renzheng_type.text = "供应商"
            } else if (item.source == "5") {
                mTextView_renzheng_type.text = "寄拍"
            }

            for (it in list_fx) {
                if (it.id == item.cateCode) {
                    mTextView1.text = it.string
                    break
                }
            }

            mTextView2.text = item.cateCodeSon
            mTextView3.text = item.material
            mTextView4.text = item.weight
            mTextView5.text = item.mainMaterial
            mTextView6.text = item.otherMaterial
            mTextView7.text = item.brand
            mTextView8.text = item.createYear
            mTextView9.text = item.theme
            mTextView10.text = item.newPercent
            mTextView11.text = item.style
            mTextView12.text = item.materialName
            mTextView13.text = item.ccAll + "厘米"
            mTextView14.text = item.price + "元"
            GlideLoader.loadImage(
                BaseConstant.Image_URL + item.orgLogo,
                mImageView_topic,
                R.drawable.add2
            )
            if (!TextUtils.isEmpty(item.bannerVideo)) {
                item.images = item.bannerVideoFace + "," + item.images
                mGridView.adapter = AdaProductDetail(context!!, item.images.split(","), true)
            } else {
                mGridView.adapter = AdaProductDetail(context!!, item.images.split(","), false)
            }

        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("商品详情", "P R O D U C T   D E T A I L S")
        mHead.setRightRes(R.drawable.kefu2)
        mHead.mImageButton_right.setOnClickListener {
            val info = Information()
            info.appkey = "e9cc7fa955a94500b364641e84adcc35"
            //用户资料
            //用户资料
            if (BaseConstant.isLogin()) {
                info.uid = LocalData.getInstance().userInfo.id
                info.uname = LocalData.getInstance().userInfo.id
                info.tel = LocalData.getInstance().userInfo.account
                info.face = BaseConstant.Image_URL + LocalData.getInstance().userInfo
                    .headImg
            } else {
                info.uid = CommonUtils.getDeviceId(context)
                info.uname = "游客"
            }
            //1仅机器人 2仅人工 3机器人优先 4人工优先
            //1仅机器人 2仅人工 3机器人优先 4人工优先
            info.initModeType = 4
            //转接类型(0-可转入其他客服，1-必须转入指定客服)
            //转接类型(0-可转入其他客服，1-必须转入指定客服)
            info.tranReceptionistFlag = 1
            //指定客服id
            //指定客服id
            info.receptionistId = "71764f63c3ca497ba974f938b26389eb"
            if (item != null) {
                //咨询内容
                val consultingContent = ConsultingContent()
                //咨询内容标题，必填
                consultingContent.sobotGoodsTitle = item.name
                //咨询内容图片，选填 但必须是图片地址
                if (item.images != "") {
                    val list = item.images.split(",")
                    consultingContent.sobotGoodsImgUrl =
                        BaseConstant.Image_URL + list.toMutableList()[0]
                }
                //咨询来源页，必填
                consultingContent.sobotGoodsFromUrl = "www.sobot.com"
                //描述，选填
                //consultingContent.setSobotGoodsDescribe("XXX超级电视 S5");
                //标签，选填
                consultingContent.sobotGoodsLable = "￥" + item.price
                //可以设置为null
                info.consultingContent = consultingContent
            }
            //设置聊天界面标题显示模式
            //设置聊天界面标题显示模式
            SobotApi.setChatTitleDisplayMode(
                context,
                SobotChatTitleDisplayMode.Default, ""
            )
            SobotApi.startSobotChat(context, info)
        }
    }
}