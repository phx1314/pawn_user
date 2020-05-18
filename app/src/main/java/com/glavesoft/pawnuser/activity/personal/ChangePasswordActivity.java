package com.glavesoft.pawnuser.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.view.CustomToast;

/**
 * @author 严光
 * @date: 2017/10/19
 * @company:常州宝丰
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener{
    private TextView bt_changepsw;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("修改密码");

        bt_changepsw  = (TextView) findViewById(R.id.bt_changepsw);
        bt_changepsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent=null;
        switch (v.getId())
        {
            case R.id.bt_changepsw:

                break;
        }
    }
}
