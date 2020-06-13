//
//  FrgEditThAddress
//
//  Created by 86139 on 2020-06-03 10:13:34
//  Copyright (c) 86139 All rights reserved.


/**

 */

package com.glavesoft.pawnuser.frg;

import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import com.glavesoft.F
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.model.ModelGrzl
import com.glavesoft.util.PhoneFormatCheckUtils
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.citypickerview.CityPickerView
import com.mdx.framework.Frame
import com.mdx.framework.utility.Helper
import kotlinx.android.synthetic.main.frg_edit_th_address.*
import kotlinx.android.synthetic.main.frg_geren_renzheng.*


class FrgEditThAddress : BaseFrg() {
    lateinit var item: ModelGrzl.ReturnAddressBean
    var mPicker = CityPickerView()
    override fun create(savedInstanceState: Bundle?) {
        setContentView(R.layout.frg_edit_th_address)
        mPicker.init(context);
        item = activity!!.intent.getSerializableExtra("item") as ModelGrzl.ReturnAddressBean
    }

    override fun initView() {

        mPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
            override fun onSelected(
                province: ProvinceBean,
                city: CityBean,
                district: DistrictBean
            ) {
                mTextView_arera.setText("$province$city$district")
            }

            override fun onCancel() {
                super.onCancel()
            }
        })
        mTextView_arera.setOnClickListener {
            mPicker.showCityPicker()
        }
        mImageView_add.setOnClickListener {
            if (TextUtils.isEmpty(mTextView_name.text.toString())) {
                Helper.toast("请输入收件人姓名")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mTextView_phone.text.toString())) {
                Helper.toast("请输入联系电话")
                return@setOnClickListener
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(mEditText_phone.getText().toString().trim())) {
                Helper.toast("请输入正确电话")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mTextView_arera.text.toString())) {
                Helper.toast("请选择省、市、区")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mTextView_address.text.toString())) {
                Helper.toast("请输入详细地址")
                return@setOnClickListener
            }
            item.userName = mTextView_name.text.toString()
            item.phone = mTextView_phone.text.toString()
            item.area = mTextView_arera.text.toString()
            item.address = mTextView_address.text.toString()
            load(
                F.gB().saveReturnAddress(
                    item.id.toString(),
                    item.userId.toString(),
                    item.userName,
                    item.area,
                    item.address,
                    item.isDefault.toString(),
                    item.phone,
                    item.createTime,
                    item.modifyTime
                ), "saveReturnAddress"
            )
        }
    }

    override fun loaddata() {
        mTextView_name.setText(item?.userName)
        mTextView_phone.setText(item?.phone)
        mTextView_arera.text = item?.area
        mTextView_address.setText(item?.address)
    }

    override fun onSuccess(data: String?, method: String) {
        if (method == "saveReturnAddress") {
            Helper.toast("操作成功")
            Frame.HANDLES.sentAll("FrgThAddress", 0, "")
            finish()
        }
    }

    override fun setActionBar(mActionBar: LinearLayout?) {
        super.setActionBar(mActionBar)
        if (TextUtils.isEmpty(item.id.toString())) {
            mHead.setTitle("新增退货地址", "A D D   R E T U R N   A D D R E S S");
        } else {
            mHead.setTitle("编辑退货地址", "E D I T   R E T U R N   A D D R E S S");
        }

    }
}