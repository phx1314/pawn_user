//
//  FrgQiyeRenzheng
//
//  Created by 86139 on 2020-06-08 13:50:12
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.app.Activity
import android.content.Intent
import android.os.Bundle;
import android.text.TextUtils

import com.glavesoft.pawnuser.R;

import android.widget.EditText;
import android.widget.ImageView;
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.glavesoft.F
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.model.ModelGrRz
import com.glavesoft.pawnuser.model.ModelQyRz
import com.glavesoft.pawnuser.model.ModelUpload
import com.glavesoft.util.GlideLoader
import com.guoxiaoxing.phoenix.picker.Phoenix
import kotlinx.android.synthetic.main.frg_qiye_renzheng.*
import kotlinx.android.synthetic.main.frg_qiye_renzheng.mEditText_username
import kotlinx.android.synthetic.main.frg_qiye_renzheng.mImageButton_sub
import kotlinx.android.synthetic.main.frg_qiye_renzheng.mImageView_fm
import kotlinx.android.synthetic.main.frg_qiye_renzheng.mImageView_zm


class FrgQiyeRenzheng : BaseFrg() {
    var mModelQyRz: ModelQyRz? = null
    lateinit var mHashMap: HashMap<String, String?>
    var businessLicensePhoto: String? = ""
    var legalPersonCardFront: String? = ""
    var legalPersonCardBack: String? = ""
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_qiye_renzheng)
    }

    override fun initView() {
        mImageView_yyz.setOnClickListener {
            F.takePhoto(activity!!, 20)
        }
        mImageView_zm.setOnClickListener {
            F.takePhoto(activity!!, 30)
        }
        mImageView_fm.setOnClickListener {
            F.takePhoto(activity!!, 40)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode === Activity.RESULT_OK) {
            //返回的数据
            val result = Phoenix.result(data)
            var methodName: String = ""
            if (requestCode === 20) {
                Glide.with(this)
                    .load(result[0].localPath)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(mImageView_yyz)
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
            load(F.gB().upload(F.getBody(result[0].localPath)), methodName)
        }
    }

    override fun loaddata() {
        load(F.gB().authInfo("2"), "authInfo")
        mImageButton_sub.setOnClickListener {
            if (TextUtils.isEmpty(mEditText_username2.text.toString())) {
                F.toast("请输入店铺名称");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText_qyname.text.toString())) {
                F.toast("请输入企业名称");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mEditText_username.text.toString())) {
                F.toast("请输入法人姓名");
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(mEditText_sfz.text.toString())) {
                F.toast("请输入法人身份证");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(businessLicensePhoto)) {
                F.toast("请上传营业执照副本照片");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(legalPersonCardFront)) {
                F.toast("请上传法人身份证正面照片");
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(legalPersonCardBack)) {
                F.toast("请上传法人身份证反面照片");
                return@setOnClickListener
            }

            mHashMap = HashMap()
            mHashMap.put("token", LocalData.getInstance().getUserInfo().getToken())
            mHashMap.put("storeName", mEditText_username2.text.toString())
            mHashMap.put("enterpriseName", mEditText_qyname.text.toString())
            mHashMap.put("legalPersonName", mEditText_username.text.toString())
            mHashMap.put("legalPersonCard", mEditText_sfz.text.toString())
            mHashMap.put("businessLicensePhoto", businessLicensePhoto)
            mHashMap.put("legalPersonCardFront", legalPersonCardFront)
            mHashMap.put("legalPersonCardBack", legalPersonCardBack)
            load(F.gB().enterprise(mHashMap), "enterprise")

        }
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "upload1") {
            var mModelUpload1 = F.data2Model(data, ModelUpload::class.java)
            businessLicensePhoto = mModelUpload1?.id
        } else if (method == "upload2") {
            var mModelUpload2 = F.data2Model(data, ModelUpload::class.java)
            legalPersonCardFront = mModelUpload2?.id
        } else if (method == "upload3") {
            var mModelUpload3 = F.data2Model(data, ModelUpload::class.java)
            legalPersonCardBack = mModelUpload3?.id
        } else if (method == "authInfo") {
            var data: Array<ModelQyRz> = F.data2Model(data, Array<ModelQyRz>::class.java)
            if (data.toMutableList().size > 0) {
                mModelQyRz = data.toMutableList()[0]
                GlideLoader.loadImage(mModelQyRz!!.businessLicensePhoto, mImageView_yyz, 0)
                businessLicensePhoto = mModelQyRz!!.businessLicensePhoto
                GlideLoader.loadImage(mModelQyRz!!.legalPersonCardFront, mImageView_zm, 0)
                legalPersonCardFront = mModelQyRz!!.legalPersonCardFront
                GlideLoader.loadImage(mModelQyRz!!.legalPersonCardBack, mImageView_fm, 0)
                legalPersonCardBack = mModelQyRz!!.legalPersonCardBack

                mEditText_username2.setText(mModelQyRz!!.storeName)
                mEditText_qyname.setText(mModelQyRz!!.enterpriseName)
                mEditText_username.setText(mModelQyRz!!.legalPersonName)
                mEditText_sfz.setText(mModelQyRz!!.legalPersonCard)

            }
        }
    }

}