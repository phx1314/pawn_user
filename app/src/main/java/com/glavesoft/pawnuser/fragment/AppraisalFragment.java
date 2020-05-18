package com.glavesoft.pawnuser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.android.volley.VolleyError;
import com.glavesoft.okGo.JsonCallback;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.appraisal.DiamondsActivity;
import com.glavesoft.pawnuser.activity.appraisal.EmeraldActivity;
import com.glavesoft.pawnuser.activity.appraisal.JewelleryActivity;
import com.glavesoft.pawnuser.activity.appraisal.MailAppraisalActivity;
import com.glavesoft.pawnuser.activity.appraisal.NoblemetalActivity;
import com.glavesoft.pawnuser.activity.appraisal.OtherActivity;
import com.glavesoft.pawnuser.activity.appraisal.WatchActivity;
import com.glavesoft.pawnuser.activity.shoppingmall.DeadPawnageActivity;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseFragment;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.pawnuser.mod.DataResult;
import com.glavesoft.pawnuser.mod.LocalData;
import com.glavesoft.pawnuser.mod.PawnCateInfo;
import com.glavesoft.view.CustomToast;
import com.glavesoft.volley.net.ResponseListener;
import com.glavesoft.volley.net.VolleyUtil;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author 严光
 * @date: 2017/10/25
 * @company:常州宝丰
 */
public class AppraisalFragment extends BaseFragment {

    private RelativeLayout titlebar_types;
    private ListView lv_types;
    private ArrayList<PawnCateInfo> list=new ArrayList<>();
    CommonAdapter commAdapter;
    public AppraisalFragment() {
    }

    public static AppraisalFragment newInstance(int index)
    {
        AppraisalFragment fragment = new AppraisalFragment();
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_types, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        titlebar_types=(RelativeLayout) view.findViewById(R.id.titlebar_types);
        titlebar_types.setVisibility(View.GONE);
        lv_types=(ListView)view.findViewById(R.id.lv_types);

        lv_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                if(list.get(position).getCode().equals("1")){
                    intent.setClass(getActivity(), JewelleryActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    intent.putExtra("name",list.get(position).getName());
                    startActivity(intent);
                }else if(list.get(position).getCode().equals("2")){
                    intent.setClass(getActivity(), WatchActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    intent.putExtra("name",list.get(position).getName());
                    startActivity(intent);
                }else if(list.get(position).getCode().equals("3")){
                    intent.setClass(getActivity(), DiamondsActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    intent.putExtra("name",list.get(position).getName());
                    startActivity(intent);
                }else if(list.get(position).getCode().equals("4")){
                    intent.setClass(getActivity(), NoblemetalActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    intent.putExtra("name",list.get(position).getName());
                    startActivity(intent);
                }else if(list.get(position).getCode().equals("5")){
                    intent.setClass(getActivity(), EmeraldActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    intent.putExtra("name",list.get(position).getName());
                    startActivity(intent);
                }else if(list.get(position).getCode().equals("6")){
                    intent.setClass(getActivity(), EmeraldActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    intent.putExtra("name",list.get(position).getName());
                    startActivity(intent);
                }else if(list.get(position).getCode().equals("7")){
                    intent.setClass(getActivity(), OtherActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    startActivity(intent);
                }else if(list.get(position).getCode().equals("8")){
                    intent.setClass(getActivity(), JewelleryActivity.class);
                    intent.putExtra("code",list.get(position).getCode());
                    intent.putExtra("name",list.get(position).getName());
                    startActivity(intent);
                }

            }
        });
        pawnCateList();

    }

    private void showList(ArrayList<PawnCateInfo> result) {

        commAdapter = new CommonAdapter<PawnCateInfo>(getActivity(), result,
                R.layout.item_types) {
            @Override
            public void convert(final ViewHolder helper, final PawnCateInfo item) {
                helper.setText(R.id.tv_name_types,item.getName());
                helper.setText(R.id.tv_pp_types,item.getCateType());

                ImageView iv_pic_types=(ImageView) helper.getView(R.id.iv_pic_types);
                getImageLoader().displayImage(BaseConstant.Image_URL + item.getIcon(),iv_pic_types,getImageLoaderOptions());

                helper.getView(R.id.tv_yj_types).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_yj_types).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent();
                        intent.setClass(getActivity(), MailAppraisalActivity.class);
                        intent.putExtra("type","mail");
                        intent.putExtra("pawnCateCode",item.getCode());
                        startActivity(intent);
                    }
                });
            }
        };

        lv_types.setAdapter(commAdapter);

    }

    private void pawnCateList()
    {
        String token= LocalData.getInstance().getUserInfo().getToken();
        String url=BaseConstant.getApiPostUrl("pawnCateList/pawnCateList");
        getlDialog().show();
        OkGo.<DataResult<ArrayList<PawnCateInfo>>>post(url)
                .params("token", token)
                .execute(new JsonCallback<DataResult<ArrayList<PawnCateInfo>>>() {
                    @Override
                    public void onSuccess(Response<DataResult<ArrayList<PawnCateInfo>>> response) {
                        getlDialog().dismiss();
                        if (response==null){
                            CustomToast.show(getString(R.string.http_request_fail));
                            return;
                        }

                        if(response.body().getErrorCode()== DataResult.RESULT_OK_ZERO){

                            if(response.body().getData()!=null&&response.body().getData().size()>0){
                                list=response.body().getData();
                                showList(list);
                            }

                        }else if (response.body().getErrorCode()==DataResult.RESULT_102)
                        {
                            toLogin();
                        }else {
                            CustomToast.show(response.body().getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Response<DataResult<ArrayList<PawnCateInfo>>> response) {
                        getlDialog().dismiss();
                        showVolleyError(null);
                    }
                });
    }
}
