package com.foamtrace.photopicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 严光
 * @date: 2018/12/7
 * @company:常州宝丰
 */
public class PhotoBitmapUtils {

    /**
     * 存放拍摄图片的文件夹
     */
    private static final String FILES_NAME = "/MyPhoto";
    /**
     * 获取的时间格式
     */
    public static final String TIME_STYLE = "yyyyMMddHHmmss";
    /**
     * 图片种类
     */
    public static final String IMAGE_TYPE = ".png";

    /**
     * 处理旋转后的图片
     * 默认不压缩
     *
     * @param originpath    原图路径
     * @param isReplaceFile 是否替换之前的文件 true 替换 false 不替换 默认保存位置
     * @return 返回修复完毕后的图片路径
     */
    public static String amendRotatePhoto(String originpath, boolean isReplaceFile) {
        return amendRotatePhoto(originpath, false, isReplaceFile);
    }

    /**
     * 处理旋转后的图片
     * 默认不压缩
     * 默认替换原图路径下保存
     *
     * @param originpath
     * @return
     */
    public static String amendRotatePhoto(String originpath) {
        return amendRotatePhoto(originpath, false, true);
    }

    /**
     * 处理旋转后的图片
     *
     * @param originpath    原图路径
     * @param isCompress    是否压缩
     * @param isReplaceFile 是否替换之前的文件 true 替换 false 不替换 默认保存位置
     * @return 返回修复完毕后的图片路径
     */
    public static String amendRotatePhoto(String originpath, boolean isCompress, boolean isReplaceFile) {

        if (TextUtils.isEmpty(originpath)) return originpath;

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);

        //是否压缩
        Bitmap bmp = null;
        if (isCompress) {
            // 把原图压缩后得到Bitmap对象
            bmp = getCompressPhoto(originpath);
        }

        if (bmp != null) {
            //处理旋转
            Bitmap bitmap = null;
            if (angle != 0) {
                // 修复图片被旋转的角度
                bitmap = rotaingImageView(angle, bmp);
            }
            if (bitmap != null) {

            }
            // 保存修复后的图片并返回保存后的图片路径
            return savePhotoToSD(bitmap, originpath, isReplaceFile);
        } else {
            Bitmap localBitmap = getLocalBitmap(originpath);
            if (localBitmap == null) return originpath;
            //处理旋转
            Bitmap bitmap = null;
            if (angle != 0) {
                // 修复图片被旋转的角度
                bitmap = rotaingImageView(angle, localBitmap);
            }
            if (bitmap != null) {
                return savePhotoToSD(bitmap, originpath, isReplaceFile);
            } else {
                return originpath;
            }
        }
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /* 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param mbitmap       需要保存的Bitmap图片
     * @param originpath    文件的原路径
     * @param isReplaceFile 是否替换原文件
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public static String savePhotoToSD(Bitmap mbitmap, String originpath, boolean isReplaceFile) {
        FileOutputStream outStream = null;
        String fileName = "";
        if (mbitmap == null) return originpath;
//        if (isReplaceFile) {
//            fileName = getPhotoFileName(context);
//        } else {
            if (TextUtils.isEmpty(originpath)) return originpath;
            fileName = originpath;
//        }
        try {
            outStream = new FileOutputStream(fileName);
            // 把数据写入文件，100表示不压缩
            mbitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (mbitmap != null) {
                    mbitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把原图按1/10的比例压缩
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 10;  // 图片的大小设置为原来的十分之一
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options = null;
        return bmp;
    }

    public static Bitmap getLocalBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        options = null;
        return bmp;
    }

    /**
     * 获取手机可存储路径
     *
     * @param context 上下文
     * @return 手机可存储路径
     */
    private static String getPhoneRootPath(Context context) {
        // 是否有SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                || !Environment.isExternalStorageRemovable()) {
            // 获取SD卡根目录
            return context.getExternalCacheDir().getPath();
        } else {
            // 获取apk包下的缓存路径
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 使用当前系统时间作为上传图片的名称
     *
     * @return 存储的根路径+图片名称
     */
    public static String getPhotoFileName(Context context) {
        File file = new File(getPhoneRootPath(context) + FILES_NAME);
        // 判断文件是否已经存在，不存在则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 设置图片文件名称
        SimpleDateFormat format = new SimpleDateFormat(TIME_STYLE, Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        String photoName = "/" + time + IMAGE_TYPE;
        return file + photoName;
    }


}
