package com.sobot.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.sobot.chat.activity.base.SobotBaseActivity;
import com.sobot.chat.adapter.SobotTicketDetailAdapter;
import com.sobot.chat.api.model.SobotUserTicketEvaluate;
import com.sobot.chat.api.model.SobotUserTicketInfo;
import com.sobot.chat.api.model.StUserDealTicketInfo;
import com.sobot.chat.core.http.callback.StringResultCallBack;
import com.sobot.chat.utils.ToastUtil;
import com.sobot.chat.widget.dialog.SobotTicketEvaluateDialog;

import java.util.ArrayList;
import java.util.List;

public class SobotTicketDetailActivity extends SobotBaseActivity implements SobotTicketEvaluateDialog.SobotTicketEvaluateCallback {
    public static final String INTENT_KEY_UID = "intent_key_uid";
    public static final String INTENT_KEY_COMPANYID = "intent_key_companyid";
    public static final String INTENT_KEY_TICKET_INFO = "intent_key_ticket_info";

    private String mUid = "";
    private String mCompanyId = "";
    private SobotUserTicketInfo mTicketInfo;

    private List<Object> mList = new ArrayList<>();
    private ListView mListView;
    private SobotTicketDetailAdapter mAdapter;

    /**
     * @param context 应用程序上下文
     * @return
     */
    public static Intent newIntent(Context context, String companyId, String uid, SobotUserTicketInfo ticketInfo) {
        Intent intent = new Intent(context, SobotTicketDetailActivity.class);
        intent.putExtra(INTENT_KEY_UID, uid);
        intent.putExtra(INTENT_KEY_COMPANYID, companyId);
        intent.putExtra(INTENT_KEY_TICKET_INFO, ticketInfo);
        return intent;
    }

    @Override
    protected int getContentViewResId() {
        return getResLayoutId("sobot_activity_ticket_detail");
    }

    protected void initBundleData(Bundle savedInstanceState) {
        if (getIntent() != null) {
            mUid = getIntent().getStringExtra(INTENT_KEY_UID);
            mCompanyId = getIntent().getStringExtra(INTENT_KEY_COMPANYID);
            mTicketInfo = (SobotUserTicketInfo) getIntent().getSerializableExtra(INTENT_KEY_TICKET_INFO);
        }
    }

    @Override
    protected void initView() {

        showLeftMenu(getResDrawableId("sobot_btn_back_selector"), getResString("sobot_back"), true);
        setTitle(getResString("sobot_message_details"));
        mListView = (ListView) findViewById(getResId("sobot_listview"));

    }

    @Override
    protected void initData() {
        if (mTicketInfo == null) {
            return;
        }
        zhiChiApi.getUserDealTicketInfoList(SobotTicketDetailActivity.this, mUid, mCompanyId, mTicketInfo.getTicketId(), new StringResultCallBack<List<StUserDealTicketInfo>>() {

            @Override
            public void onSuccess(List<StUserDealTicketInfo> datas) {
                if (datas != null && datas.size() > 0) {
                    mList.clear();
                    mList.add(mTicketInfo);
                    mList.addAll(datas);
                    if (mAdapter == null) {
                        mAdapter = new SobotTicketDetailAdapter(SobotTicketDetailActivity.this, mList);
                        mListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Exception e, String des) {
                ToastUtil.showToast(SobotTicketDetailActivity.this, des);
            }
        });
    }

    @Override
    public void submitEvaluate(final int score, final String remark) {
        zhiChiApi.addTicketSatisfactionScoreInfo(SobotTicketDetailActivity.this, mUid, mCompanyId, mTicketInfo.getTicketId(), score, remark, new StringResultCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                for (int i = 0; i < mList.size(); i++) {
                    Object obj = mList.get(i);
                    if (obj instanceof StUserDealTicketInfo) {
                        StUserDealTicketInfo data = (StUserDealTicketInfo) mList.get(i);
                        if (data.getFlag() == 3 && data.getEvaluate() != null) {
                            SobotUserTicketEvaluate evaluate = data.getEvaluate();
                            evaluate.setScore(score);
                            evaluate.setRemark(remark);
                            evaluate.setEvalution(true);
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Exception e, String des) {
                ToastUtil.showToast(getApplicationContext(), des);
            }
        });


    }
}