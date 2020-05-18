package com.glavesoft.pawnuser.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.LocalData;

/**
 * @author 严光
 * @date: 2017/11/21
 * @company:常州宝丰
 */
public class IdCardActivity extends BaseActivity implements View.OnClickListener{

    private ImageView iv_zm_showpic,iv_fm_showpic;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idcard);
        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("身份证");

        iv_zm_showpic=(ImageView) findViewById(R.id.iv_zm_showpic);
        iv_fm_showpic=(ImageView) findViewById(R.id.iv_fm_showpic);
        String imageurl= LocalData.getInstance().getUserInfo().getIdCardImg();
        getImageLoader().displayImage(BaseConstant.Image_URL+imageurl,iv_zm_showpic,getImageLoaderOptions());
        String imageurl1= LocalData.getInstance().getUserInfo().getIdCardReverse();
        getImageLoader().displayImage(BaseConstant.Image_URL+imageurl1,iv_fm_showpic,getImageLoaderOptions());
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
//            case R.id.tv_submit_bankcard:
//                add();
//                break;
        }
    }

}
