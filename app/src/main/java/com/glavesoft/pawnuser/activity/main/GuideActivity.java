package com.glavesoft.pawnuser.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.activity.login.LoginActivity;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.pawnuser.constant.BaseConstant;
import com.glavesoft.util.PreferencesUtils;
import com.glavesoft.view.MyGallery1;
import com.glavesoft.view.PageIndicatorView;


/**
 * 启动指导页
 *
 */
public class GuideActivity extends BaseActivity
{
    MyGallery1 galleryimg;
    private PageIndicatorView view_page;
    private int size;// 广告图的数量

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        size = newFunctionHintPics.length;
        setView();
    }

    private void setView()
    {
        galleryimg = (MyGallery1) findViewById(R.id.gallery_img);
        view_page = (PageIndicatorView) findViewById(R.id.view_page);

        galleryimg.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                view_page.setCurrentPage(position % size);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        galleryimg.setAdapter(new BaseAdapter()
        {
            public View getView(int position, View convertView, ViewGroup parent)
            {
                final ViewClass view;
                if (convertView == null)
                {
                    convertView = LayoutInflater.from(GuideActivity.this).inflate(R.layout.gallery_row_img_layout, null);
                    view = new ViewClass();
                    view.item_img = (ImageView) convertView.findViewById(R.id.item_img);
                    view.start_btn = (ImageView) convertView.findViewById(R.id.start_btn);
                    convertView.setTag(view);
                } else
                {
                    view = (ViewClass) convertView.getTag();
                }

                view.item_img.setBackgroundResource(newFunctionHintPics[position]);

                if ((position == (getCount() - 1)))
                {
                    view.start_btn.setVisibility(View.VISIBLE);
                    view.start_btn.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            PreferencesUtils.putBoolean(GuideActivity.this, "isfirstopensoft", false);
//                            if(PreferencesUtils.getStringPreferences(BaseConstant.AccountManager_NAME, BaseConstant.SharedPreferences_LastLogin,null)==null){
//                                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
//                            }else{
                                startActivity(new Intent(GuideActivity.this, MainActivity.class));
//                            }
                            finish();
                        }
                    });
                } else
                {
                    view.start_btn.setVisibility(View.GONE);
                }
                return convertView;
            }

            public long getItemId(int position)
            {
                return 0;
            }

            public Object getItem(int position)
            {
                return null;
            }

            public int getCount()
            {
                return newFunctionHintPics.length;
            }
        });
        view_page.setTotalPage(size);
        view_page.setCurrentPage(0);
    }

    private class ViewClass
    {
        ImageView start_btn;
        ImageView item_img;
    }

    public static int[] newFunctionHintPics = {R.drawable.yindao1, R.drawable.yindao2, R.drawable.yindao3};

}
