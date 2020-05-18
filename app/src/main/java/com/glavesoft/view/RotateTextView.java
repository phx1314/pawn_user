package com.glavesoft.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 呈45°的文字显示
 */
public class RotateTextView extends TextView{


    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度135,上下左右居中
        canvas.rotate(-45, getMeasuredWidth()/3, getMeasuredHeight()/3);
//    	  canvas.rotate(-45, 100, 5);
        super.onDraw(canvas);
    }

}
