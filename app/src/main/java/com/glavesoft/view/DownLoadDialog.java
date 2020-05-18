package com.glavesoft.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.glavesoft.pawnuser.R;

public class DownLoadDialog {

    private Context mContext;

    private View mDialogView;
    private TextView mTvMsg;
    private TextView mTvProgress;
    private Dialog mDialog;



    public DownLoadDialog(Context context){

        this.mContext = context;

        mDialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_download,null);


        mTvMsg = mDialogView.findViewById(R.id.tv_num_download);
        mTvProgress = mDialogView.findViewById(R.id.tv_progress_download);
        initDialog();

    }


    private void  initDialog(){

        mDialog= new Dialog(mContext,R.style.dialog);
        mDialog.setContentView(mDialogView);
        mDialog.setCanceledOnTouchOutside(false);

    }


    public void setMessage(String msg){
        mTvMsg.setText(msg);
    }

    public void setProgress(String progress){
        if (progress.equals("")){
            mTvProgress.setVisibility(View.GONE);
        }else{
            mTvProgress.setVisibility(View.VISIBLE);
            mTvProgress.setText(progress);
        }
    }

    public  void show(){

        if(mDialog!=null)
            mDialog.show();
    }


    public  void dismiss(){

        if(mDialog!=null)
            mDialog.dismiss();
    }


    public  boolean isShowing(){

        return  mDialog.isShowing();
    }
}
