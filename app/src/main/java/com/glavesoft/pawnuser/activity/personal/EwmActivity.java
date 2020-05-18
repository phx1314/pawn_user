package com.glavesoft.pawnuser.activity.personal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import com.glavesoft.pawnuser.R;
import com.glavesoft.pawnuser.base.BaseActivity;
import com.glavesoft.util.ScreenUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.util.Hashtable;

/**
 * @author 严光
 * @date: 2017/10/19
 * @company:常州宝丰
 */
public class EwmActivity extends BaseActivity {

    private ImageView iv_ewm;
    static Bitmap bitmapQR;
    static Bitmap bitmap;
    private static final int BLACK = 0xff000000;
    private static final int WHITE = 0xffffffff;
    private static int PADDING_SIZE_MIN ; // 最小留白长度
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewm);

        initView();
    }

    private void initView() {
        setTitleBack();
        setTitleName("推广二维码");
        iv_ewm=(ImageView)findViewById(R.id.iv_ewm);

//        PADDING_SIZE_MIN= ScreenUtils.dp2px(EwmActivity.this, 5);
//        try {
//            iv_ewm.setImageBitmap(createQRCode("https://www.baidu.com/",200));
//        } catch (WriterException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        if(bitmapQR!=null){
            bitmapQR.recycle();
        }
        if(bitmap!=null){
            bitmap.recycle();
        }
        super.onDestroy();
    }

    /**
     * 生成二维码 要转换的地址或字符串,可以是中文
     */
    public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints);

        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        boolean isFirstBlackPoint = false;
        int startX = 0;
        int startY = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    if (isFirstBlackPoint == false)
                    {
                        isFirstBlackPoint = true;
                        startX = x;
                        startY = y;
                        //Log.d("createQRCode", "x y = " + x + " " + y);
                    }
                    pixels[y * width + x] = BLACK;
                }else {
                    pixels[y * width + x] = WHITE;
                }
            }
        }

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        // 剪切中间的二维码区域，减少padding区域
        if (startX <= PADDING_SIZE_MIN) return bitmap;

        int x1 = startX - PADDING_SIZE_MIN;
        int y1 = startY - PADDING_SIZE_MIN;
        if (x1 < 0 || y1 < 0) return bitmap;

        int w1 = width - x1 * 2;
        int h1 = height - y1 * 2;

        bitmapQR = Bitmap.createBitmap(bitmap, x1, y1, w1, h1);

        return bitmapQR;
    }


}
