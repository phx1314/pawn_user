//
//  FrgGerenRenzheng
//
//  Created by 86139 on 2020-05-30 14:16:19
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.glavesoft.F
import com.glavesoft.F.data2Model
import com.glavesoft.F.gB
import com.glavesoft.F.getBody
import com.glavesoft.F.takePhoto
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.constant.BaseConstant.Image_URL
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.model.ModelGrRz
import com.glavesoft.pawnuser.model.ModelUpload
import com.glavesoft.util.GlideLoader
import com.glavesoft.util.PhoneFormatCheckUtils
import com.guoxiaoxing.phoenix.compress.picture.internal.PictureCompressor
import com.guoxiaoxing.phoenix.picker.Phoenix
import com.hengyi.wheelpicker.ppw.CityWheelPickerPopupWindow
import com.mdx.framework.Frame
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_geren_renzheng.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class FrgGerenRenzheng : BaseFrg() {
    var mModelGrRz: ModelGrRz? = null
    lateinit var mHashMap: HashMap<String, String?>
    var logo: String? = ""
    var idCardFront: String? = ""
    var idCardBack: String? = ""
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_geren_renzheng)
    }

    override fun initView() {
        val wheelPickerPopupWindow =
            CityWheelPickerPopupWindow(activity)
        wheelPickerPopupWindow.setListener { Province, City, District, PostCode ->
            Toast.makeText(activity, Province + City + District, Toast.LENGTH_LONG)
                .show()
        }
        mImageView.setOnClickListener {
            takePhoto(activity!!, 20)
//            wheelPickerPopupWindow.show()
        }
        mImageView_zm.setOnClickListener {
            takePhoto(activity!!, 30)
        }
        mImageView_fm.setOnClickListener {
            takePhoto(activity!!, 40)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === RESULT_OK) {
            //返回的数据
            val result = Phoenix.result(data)
            var methodName: String = ""
            if (requestCode === 20) {
                Glide.with(this)
                    .load(result[0].localPath)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(mImageView)
                methodName = "upload1"
            } else if (requestCode === 30) {
                Glide.with(this)
                    .load(result[0].localPath)
                    .into(mImageView_zm)
                methodName = "upload2"
            } else if (requestCode === 40) {
                Glide.with(this)
                    .load(result[0].localPath)
                    .into(mImageView_fm)
                methodName = "upload3"
            }
            load(gB().upload(getBody(result[0].localPath)), methodName)
        }
    }

    override fun loaddata() {
        load(gB().authInfo("1"), "authInfo")

        mImageButton_sub.setOnClickListener {
            if (TextUtils.isEmpty(logo)) {
                F.toast("请选择logo");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText_name.text.toString())) {
                F.toast("请输入店铺名称");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText_content.text.toString())) {
                F.toast("请输入店铺介绍");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText_username.text.toString())) {
                F.toast("请输入姓名");
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(mEditText_card.text.toString())) {
                F.toast("请输入身份证号码");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText_phone.text.toString())) {
                F.toast("请输入联系电话");
                return@setOnClickListener
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(mEditText_phone.getText().toString().trim())) {
                Helper.toast("请输入正确电话")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(idCardFront)) {
                F.toast("请上传身份证正面");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(idCardBack)) {
                F.toast("请上传身份证反面");
                return@setOnClickListener
            }

            mHashMap = HashMap()
            mModelGrRz?.let { mHashMap.put("id", it.id.toString()) }
            mHashMap.put("token", LocalData.getInstance().getUserInfo().getToken())
            mHashMap.put("logo", logo)
            mHashMap.put("storeName", mEditText_name.text.toString())
            mHashMap.put("storeIntroduce", mEditText_content.text.toString())
            mHashMap.put("name", mEditText_username.text.toString())
            mHashMap.put("idCard", mEditText_card.text.toString())
            mHashMap.put("phone", mEditText_phone.text.toString())
            mHashMap.put("idCardFront", idCardFront)
            mHashMap.put("idCardBack", idCardBack)
            load(gB().personal(mHashMap), "personal")
        }
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "personal") {
            F.toast("提交成功")
            Frame.HANDLES.close("FrgRenzheng")
        } else if (method == "upload1") {
            var mModelUpload1 = data2Model(data, ModelUpload::class.java)
            logo = mModelUpload1?.id
        } else if (method == "upload2") {
            var mModelUpload2 = data2Model(data, ModelUpload::class.java)
            idCardFront = mModelUpload2?.id
        } else if (method == "upload3") {
            var mModelUpload3 = data2Model(data, ModelUpload::class.java)
            idCardBack = mModelUpload3?.id
        } else if (method == "authInfo") {
            var data: Array<ModelGrRz> = data2Model(data, Array<ModelGrRz>::class.java)
            if (data.toMutableList().size > 0) {
                mModelGrRz = data.toMutableList()[0]
                GlideLoader.loadCircleCropImage(Image_URL+mModelGrRz!!.logo, mImageView, 0)
                logo = mModelGrRz!!.logo
                GlideLoader.loadImage(Image_URL+mModelGrRz!!.idCardFront, mImageView_zm, 0)
                idCardFront = mModelGrRz!!.idCardFront
                GlideLoader.loadImage(Image_URL+mModelGrRz!!.idCardBack, mImageView_fm, 0)
                idCardBack = mModelGrRz!!.idCardBack

                mEditText_name.setText(mModelGrRz!!.storeName)
                mEditText_content.setText(mModelGrRz!!.storeIntroduce)
                mEditText_username.setText(mModelGrRz!!.name)

                mEditText_username.setText(mModelGrRz!!.name)
                mEditText_card.setText(mModelGrRz!!.idCard)
                mEditText_phone.setText(mModelGrRz!!.phone)

            }
        }

    }

}