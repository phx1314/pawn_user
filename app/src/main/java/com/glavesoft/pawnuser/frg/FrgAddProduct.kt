//
//  FrgAddProduct
//
//  Created by 86139 on 2020-06-04 19:48:42
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.glavesoft.F
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.ada.AdaAddProduct
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.mod.StoreGoodsInfo
import com.glavesoft.pawnuser.model.ModelUpload
import com.glavesoft.util.GlideLoader
import com.glavesoft.util.UploadFileRequestBody
import com.guoxiaoxing.phoenix.compress.video.VideoCompressor
import com.guoxiaoxing.phoenix.core.common.PhoenixConstant
import com.guoxiaoxing.phoenix.core.model.MediaEntity
import com.guoxiaoxing.phoenix.picker.Phoenix
import com.mdx.framework.Frame
import com.mdx.framework.activity.TitleAct
import com.mdx.framework.model.ModelDx
import com.mdx.framework.service.subscriber.S
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_add_product.*
import kotlinx.android.synthetic.main.frg_add_product.mGridView
import okhttp3.MultipartBody
import java.io.File


class FrgAddProduct : BaseFrg() {
    var p_id: String? = null
    var video: String? = null
    var video_img: String? = null
    var cateCode: Int? = null
    var mProgressDialog: ProgressDialog? = null


    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_add_product)
        p_id = activity!!.intent.getStringExtra("id")
    }

    override fun disposeMsg(type: Int, obj: Any?) {
        when (type) {
            0 -> {
                F.takePhoto(
                    activity!!, 40,
                    maxPickNumber = 9 - (mGridView.adapter as AdaAddProduct).count
                )
            }
            1 -> {
                (mGridView.adapter as AdaAddProduct).remove(obj)
                if (!TextUtils.isEmpty((mGridView.adapter as AdaAddProduct).get((mGridView.adapter as AdaAddProduct).count - 1))) {
                    (mGridView.adapter as AdaAddProduct).add("")
                }
            }
            2 -> {
                mEditText2.text = (obj as ModelDx).string
                cateCode = (obj as ModelDx).id
                if (cateCode == 4 || cateCode == 6) {
                    mLinearLayout_son_code.visibility = View.VISIBLE
                } else {
                    mLinearLayout_son_code.visibility = View.GONE
                }
                mEditText3.text = ""
            }
            3 -> {
                mEditText3.text = (obj as ModelDx).string
                cateCode = (obj as ModelDx).id
            }
        }

    }

    override fun initView() {
        mRelativeLayout_video.setOnClickListener {
            F.takePhoto(
                activity!!,
                20,
                fileType = PhoenixConstant.TYPE_ALL
            )
        }
        mImageView_addvideoimg.setOnClickListener {
            F.takePhoto(activity!!, 30)
        }
        mEditText2.setOnClickListener {
            Helper.startActivity(
                context,
                FrgListDx::class.java,
                TitleAct::class.java,
                "title",
                "选择分类",
                "from",
                "FrgAddProduct",
                "type",
                2,
                "data",
                F.list_fx
            )
        }
        mEditText3.setOnClickListener {
            Helper.startActivity(
                context,
                FrgListDx::class.java,
                TitleAct::class.java,
                "title",
                "选择子分类",
                "from",
                "FrgAddProduct",
                "type",
                3,
                "data",
                if (cateCode == 4) F.list_fx_son_1 else F.list_fx_son_2
            )
        }

        mImageButton.setOnClickListener {
            if (TextUtils.isEmpty(mEditText1.text.toString())) {
                F.toast("请输入名称")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText2.text.toString())) {
                F.toast("请选择分类")
                return@setOnClickListener
            }
            if ((cateCode == 4 || cateCode == 6) && TextUtils.isEmpty(mEditText3.text.toString())) {
                F.toast("请选择子分类")
                return@setOnClickListener
            }
//            if (TextUtils.isEmpty(mEditText4.text.toString())) {
//                F.toast("请输入材质")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText5.text.toString())) {
//                F.toast("请输入质量")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText6.text.toString())) {
//                F.toast("请输入主材")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText7.text.toString())) {
//                F.toast("请输入辅材")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText8.text.toString())) {
//                F.toast("请输入品牌")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText9.text.toString())) {
//                F.toast("请输入年代")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText10.text.toString())) {
//                F.toast("请输入题材")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText11.text.toString())) {
//                F.toast("请输入新旧程度")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText12.text.toString())) {
//                F.toast("请输入样式")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText13.text.toString())) {
//                F.toast("请输入种地")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(mEditText14.text.toString())) {
//                F.toast("请输入尺寸")
//                return@setOnClickListener
//            }
            if (TextUtils.isEmpty(mEditText15.text.toString())) {
                F.toast("请输入售价")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText16.text.toString())) {
                F.toast("请输入描述")
                return@setOnClickListener
            }
//            if (!TextUtils.isEmpty(video_img) && TextUtils.isEmpty(video)) {
//                F.toast("请选择视频")
//                return@setOnClickListener
//            }
//            if (TextUtils.isEmpty(video_img) && !TextUtils.isEmpty(video)) {
//                F.toast("请选择视频封面")
//                return@setOnClickListener
//            }
            var mHashMap = HashMap<String, String?>()
            if (!TextUtils.isEmpty(p_id)) {
                mHashMap.put("id", p_id)
            }
            mHashMap["token"] = LocalData.getInstance().getUserInfo().getToken()
            mHashMap["name"] = mEditText1.text.toString()
            mHashMap["cateCode"] = cateCode.toString()
            mHashMap["cateCodeSon"] = ""
            mHashMap["material"] = mEditText4.text.toString()
            mHashMap["weight"] = mEditText5.text.toString()
            mHashMap["mainMaterial"] = mEditText6.text.toString()
            mHashMap["otherMaterial"] = mEditText7.text.toString()
            mHashMap["brand"] = mEditText8.text.toString()
            mHashMap["createYear"] = mEditText9.text.toString()
            mHashMap["theme"] = mEditText10.text.toString()
            mHashMap["newPercent"] = mEditText11.text.toString()
            mHashMap["style"] = mEditText12.text.toString()
            mHashMap["materialName"] = mEditText13.text.toString()
            mHashMap["ccAll"] = mEditText14.text.toString()
            mHashMap["price"] = mEditText15.text.toString()
            mHashMap["wordDescript"] = mEditText16.text.toString()
            mHashMap["total"] = "1"
            mHashMap["state"] = "1"
            mHashMap["bannerVideo"] = video ?: ""
            mHashMap["bannerVideoFace"] = video_img ?: ""
            var imgs = ""
            for (it in (mGridView.adapter as AdaAddProduct).list) {
                if (!TextUtils.isEmpty(it))
                    imgs += "$it,"
            }
            mHashMap.put("imgs", if (imgs == "") imgs else imgs.substring(0, imgs.length - 1))
            load(F.gB().save(mHashMap), "save")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK) {
            //返回的数据
            val result = Phoenix.result(data)
            if (requestCode === 20) {
                if (result[0].mimeType.contains("video")) {

                    mProgressDialog = ProgressDialog(context)
                    mProgressDialog?.setTitle("文件压缩中")
                    mProgressDialog?.show()
                    val compressCachePath = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                        "phoenix"
                    )
                    compressCachePath.mkdir()
                    val compressFile = File.createTempFile("compress", ".mp4", compressCachePath)
                    F.getVideoBody(result[0].localPath, compressFile,
                        object : VideoCompressor.Listener {
                            override fun onTranscodeProgress(progress: Double) {
                            }

                            override fun onTranscodeCompleted() {
                                doSomeThing(File(compressFile.absolutePath))
                            }

                            override fun onTranscodeCanceled() {
                                if (context != null)
                                    mProgressDialog?.dismiss()
                                Log.i("剪裁", "拒绝")
                            }

                            override fun onTranscodeFailed(exception: java.lang.Exception) {
                                Log.i("剪裁", "失败")
                                doSomeThing(File(result[0].localPath))
                            }
                        })
                } else {
                    F.toast("请选择视频文件")
                }
            } else if (requestCode === 30) {
                Glide.with(this)
                    .load(result[0].localPath)
                    .into(mImageView_addvideoimg)
                load(F.gB().upload(F.getBody(result[0].localPath)), "upload2")
            } else if (requestCode === 40) {
                load(F.gB().uploadFiles(F.getBodys(result)), "upload3")
            }
        }
    }

    fun doSomeThing(compressFIle: File) {
        if (context != null) {
            mProgressDialog?.dismiss()
            Glide.with(context!!)
                .setDefaultRequestOptions(
                    RequestOptions()
                        .frame(1000000)
                        .centerCrop()
                )
                .load(compressFIle.absolutePath)
                .into(mImageView_addvideo)
            var s = S(
                this@FrgAddProduct,
                ProgressDialog(context).apply {
                    this.setTitle("文件上传中")
                    this.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    this.max = 100
                },
                "upload1",
                true
            )
            var body =
                MultipartBody.Part.createFormData(
                    "file",
                    compressFIle.name,
                    UploadFileRequestBody(compressFIle, s)
                )
            load(
                F.gB(10000).upload(body),
                "upload1", s = s
            )
        }
    }


    override fun loaddata() {
        if (!TextUtils.isEmpty(p_id)) {
            load(
                F.gB().storeGoodsDetail(
                    p_id!!
                ), "storeGoodsDetail"
            )
        } else {
            var mModelGridImgDatas = ArrayList<String>()
            mModelGridImgDatas.add("")
            mGridView.adapter = AdaAddProduct(context!!, mModelGridImgDatas)
        }

    }

    override fun onProgress(progress: Int) {
        Log.i("进度", progress.toString())
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "upload1") {
            var mModelUpload = F.data2Model(data, ModelUpload::class.java)
            video = mModelUpload?.id
            mImageView_bf.visibility = View.VISIBLE
            Log.i("进度", "上传成功")
        } else if (method == "upload2") {
            var mModelUpload = F.data2Model(data, ModelUpload::class.java)
            video_img = mModelUpload?.id
        } else if (method == "upload3") {
            var mModelUpload = F.data2Model(data, ModelUpload::class.java)

            (mGridView.adapter as AdaAddProduct).remove((mGridView.adapter as AdaAddProduct).count - 1)
            (mGridView.adapter as AdaAddProduct).AddAll(
                mModelUpload.id.split(",")
            )
            if ((mGridView.adapter as AdaAddProduct).count < 8) {
                (mGridView.adapter as AdaAddProduct).add("")
            }
        } else if (method == "storeGoodsDetail") {
            var item = F.data2Model(data, StoreGoodsInfo::class.java)
            mEditText1.setText(item.title)
            for (it in F.list_fx) {
                if (it.id == item.cateCode) {
                    mEditText2.text = it.string
                    break
                }
            }
            cateCode = item.cateCode
            if (item.cateCode in 9..11) {
                mEditText2.text = "古董艺术"
                for (it in F.list_fx_son_1) {
                    if (it.id == item.cateCode.toInt()) {
                        mEditText3.text = it.string
                        break
                    }
                }
            }
            if (item.cateCode in 12..15) {
                mEditText2.text = "彩色珠宝"
                for (it in F.list_fx_son_2) {
                    if (it.id == item.cateCode.toInt()) {
                        mEditText3.text = it.string
                        break
                    }
                }
            }
            mEditText4.setText(item.material)
            mEditText5.setText(item.weight)
            mEditText6.setText(item.mainMaterial)
            mEditText7.setText(item.otherMaterial)
            mEditText8.setText(item.brand)
            mEditText9.setText(item.createYear)
            mEditText10.setText(item.theme)
            mEditText11.setText(item.newPercent)
            mEditText12.setText(item.style)
            mEditText13.setText(item.materialName)
            mEditText14.setText(item.ccAll)
            mEditText15.setText(item.price)
            mEditText16.setText(item.wordDescript)
            GlideLoader.loadImage(
                BaseConstant.Image_URL + item.bannerVideoFace,
                mImageView_addvideoimg,
                R.drawable.add2
            )
            GlideLoader.loadImage(
                BaseConstant.Image_URL + item.bannerVideoFace,
                mImageView_addvideo,
                R.drawable.add2
            )
            video = item.bannerVideo
            video_img = item.bannerVideoFace
            if (!TextUtils.isEmpty(item.bannerVideo)) {
                mImageView_bf.visibility = View.VISIBLE
            }
            var mModelGridImgDatas = ArrayList<String>()
            for (url in item.images.split(",")) {
                mModelGridImgDatas.add(url)
            }
            if (mModelGridImgDatas.size < 8) mModelGridImgDatas.add("")
            mGridView.adapter = AdaAddProduct(context!!, mModelGridImgDatas)
        } else if (method == "save") {
            Helper.toast("操作成功")
            Frame.HANDLES.sentAll("FrgAddProductList", 0, "")
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mProgressDialog?.let {
            if (it.isShowing)
                it.dismiss()
        }

    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        mHead.setTitle("新增商品", "N E W   P R O D U C T S");
    }
}