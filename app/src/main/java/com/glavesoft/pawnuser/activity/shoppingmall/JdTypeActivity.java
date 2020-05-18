package com.glavesoft.pawnuser.activity.shoppingmall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.adapter.CommonAdapter;
import com.glavesoft.pawnuser.adapter.ViewHolder;
import com.glavesoft.pawnuser.base.BaseActivity;
import java.util.ArrayList;

/**
 * @author 严光
 * @date: 2017/12/29
 * @company:常州宝丰
 */
public class JdTypeActivity  extends BaseActivity {
    private ListView lv_types;
    CommonAdapter commAdapter;
    private String type,state;
    private ArrayList<String> list=new ArrayList<>();
    private ArrayList<String> list1=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
        type=getIntent().getStringExtra("type");
        state=getIntent().getStringExtra("state");
        initView();
    }

    private void initView() {
        setTitleBack();
        if(type.equals("4")){
            setTitleName("古董艺术品");
            list.add("明清砚台");
            list.add("文玩");
            list.add("杂项");
            list1.add("9");
            list1.add("10");
            list1.add("11");
        }else{
            setTitleName("彩色珠宝");
            list.add("红蓝宝石");
            list.add("祖母绿");
            list.add("珍珠");
            list.add("碧玺");
            list1.add("12");
            list1.add("13");
            list1.add("14");
            list1.add("15");

        }

        lv_types=(ListView)findViewById(R.id.lv_types);

        lv_types.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if(state.equals("rz")){
                    intent.setClass(JdTypeActivity.this, StoreGoodsListActivity.class);
                }else{
                    intent.setClass(JdTypeActivity.this, JdStoreGoodsListActivity.class);
                }
                intent.putExtra("type", list1.get(position));
                intent.putExtra("state", state);
                startActivity(intent);


            }
        });

        showList(list);

    }

    private void showList(ArrayList<String> result) {

        commAdapter = new CommonAdapter<String>(JdTypeActivity.this, result,
                R.layout.item_type) {
            @Override
            public void convert(final ViewHolder helper, final String item) {
                helper.setText(R.id.tv_type,item);

            }
        };

        lv_types.setAdapter(commAdapter);

    }
}
