package com.glavesoft.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.util.Calendar;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.glavesoft.pawnuser.base.BaseApplication;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@SuppressLint("NewApi")
public class FileUtils {
    //	public static String CACHE_SAVE = BaseApplication.getInstance().getExternalFilesDir("")+File.separator;
    public static String CACHE_SAVE = Environment.getExternalStorageDirectory() + File.separator;
    public static String CACHE_SAVE_PATH = CACHE_SAVE + "pawn_user/";
    public static String CACHE_SAVE_IMG_PATH = CACHE_SAVE + "pawn_user" + File.separator + "Imgs/"; // 保存相册的路径
    public static String CACHE_SAVE_VIDEO_PATH = CACHE_SAVE + "pawn_user" + File.separator + "Videos/";
    public static String CACHE_SAVE_PDF_PATH = CACHE_SAVE + "pawn_user" + File.separator + "pdf/";
    public static String CACHE_SAVE_CONTRACT_PATH = CACHE_SAVE + "pawn_user" + File.separator + "Contracts/";
    public static String FACE_PATH = CACHE_SAVE + "pawn_user" + File.separator + "face.jpg";
    public static final long FILE_IMAGE_MAXSIZE = 1024 * 1024; // 图片上传文件的最大大小
    // private static String TAG = "FileUtils";
    // private static boolean DEBUG = false; //是否Debug
    // private static final int BUFFER = 8192;

    /**
     * 获取一个jpg图片的缓存路径
     * <p>图片名根据时间轴生成
     *
     * @param context
     * @return
     */
    public static String getCachePath(Context context) {
        return context.getExternalCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
    }

    // 是否有SD卡
    public static boolean isHasSDCard() {
        boolean isHasSDCard = false;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            isHasSDCard = true;
        }
        return isHasSDCard;
    }

    /*
     * 读取文本文件 param String
     */
    public static String readTextFile(File file) throws IOException {
        String text = null;
        InputStream is = null;
        if (isHasSDCard()) {
            try {
                is = new FileInputStream(file);
                text = readTextInputStream(is);
                ;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return text;
    }

    /*
     * 读取文本文件 param InputStream
     */
    public static String readTextInputStream(InputStream is) throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        if (!isHasSDCard()) {
            return null;
        }
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return strbuffer.toString();
    }

    /*
     * 写入文本文件 param String
     */
    public static void writeTextFile(File file, String str) throws IOException {
        DataOutputStream out = null;
        if (isHasSDCard()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                out = new DataOutputStream(new FileOutputStream(file));
                out.write(str.getBytes());
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        }
    }

    /*
     * 写入文本文件 param inStream
     */
    public static void writeTextFile(File file, InputStream inStream) throws IOException {
        FileOutputStream outStream = null;
        if (isHasSDCard()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                outStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
            } finally {
                if (outStream != null) {
                    outStream.flush();
                    outStream.close();
                }
            }
        }
    }

    /**
     * @param fromFile 被复制的文件
     * @param toFile   复制的目录文件
     * @param rewrite  是否重新创建文件
     *
     *                 <p>
     *                 文件的复制操作方法
     */
    public static boolean copyfile(File fromFile, File toFile, Boolean rewrite) {

        if (!fromFile.exists()) {
            return false;
        }

        if (!fromFile.isFile()) {
            return false;
        }
        if (!fromFile.canRead()) {
            return false;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile);

            byte[] bt = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            // 关闭输入、输出流
            fosfrom.close();
            fosto.close();
            return true;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /*
     * 递归获得文件集大小
     */
    public static long getFilesSize(ArrayList<File> files) {
        long size = 0;
        if (isHasSDCard()) {
            for (int i = 0; i < files.size(); i++) {
                if (files.get(i).exists()) {
                    size += files.get(i).length();
                } else {
                    size += 0;
                }
            }
        }
        return size;
    }

    /*
     * 递归获得文件夹大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (isHasSDCard()) {
            if (!file.exists())
                return 0;

            File[] files = file.listFiles();
            if (files != null)
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        size += getFileSize(files[i]);
                    } else {
                        size += files[i].length();
                    }
                }
        }
        return size;
    }

    /*
     * 删除文件及其下面的子文件
     */
    public static void delFile(File file) {
        if (isHasSDCard()) {
            if (!file.exists())
                return;
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    delFile(files[i]); // 删除下面子文件或文件夹
                    files[i].delete(); // 删除自个儿
                } else {
                    files[i].delete(); // 文件直接删掉
                }
            }
        }
    }

    /*
     * 图片压缩
     *
     * @param file :待压缩源文件
     *
     * @param size ：压缩大小
     *
     * @param path ：压缩
     */
    public static File compressImg(File file, long size, String path) throws IOException {
        OutputStream os = null;
        Bitmap btm = null;
        if (isHasSDCard()) {
            if (!file.exists() || file.length() < size) // 若文件不存在，或者小于size，则不进行压缩
                return file;

            try {
                File tmpfile = new File(path, "tmp.jpg");

                while (file.length() > size) {
                    // 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
                    BitmapFactory.Options newOpts = new BitmapFactory.Options();
                    // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
                    newOpts.inSampleSize = 2;
                    // inJustDecodeBounds设为false表示把图片读进内存中
                    newOpts.inJustDecodeBounds = false;
                    // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
                    btm = BitmapFactory.decodeFile(file.getAbsolutePath(), newOpts);

                    // 创建文件输出流
                    if (btm == null) {
                        return file;
                    }
                    tmpfile = new File(path, "tmp.jpg");
                    os = new FileOutputStream(tmpfile);
                    // 存储
                    btm.compress(CompressFormat.JPEG, 100, os);
                    // 关闭流
                    os.flush();
                    os.close();
                    if (!btm.isRecycled()) {
                        btm.recycle();
                    }
                    file = new File(path + file.getName());
                    tmpfile.renameTo(file);
                }
                return file;
            } catch (Exception e) {
                return file;
            }
        }
        return file;
    }

    /*
     * zip解压
     *
     * AssetManager assetManager = getAssets(); // 需要解压的对象 InputStream
     * dataSource = assetManager.open("ShiningTrip.zip"); // 調用解压的方法
     * ZipUtil.unzip(dataSource,
     * android.os.Environment.getExternalStorageDirectory() + "");
     */
    public static void unzip(InputStream zipFileName, String outputDirectory) {
        try {
            ZipInputStream in = new ZipInputStream(zipFileName);
            // 获取ZipInputStream中的ZipEntry条目，一个zip文件中可能包含多个ZipEntry，
            // 当getNextEntry方法的返回值为null，则代表ZipInputStream中没有下一个ZipEntry，
            // 输入流读取完成；
            ZipEntry entry = in.getNextEntry();
            while (entry != null) {

                // 创建以zip包文件名为目录名的根目录
                if (FileUtils.isHasSDCard()) {
                    File file = new File(outputDirectory);
                    file.mkdirs();
                    if (entry.isDirectory()) {
                        String name = entry.getName();
                        name = name.substring(0, name.length() - 1);

                        file = new File(outputDirectory + File.separator + name);
                        file.mkdirs();
                    } else {
                        file = new File(outputDirectory + File.separator + entry.getName());
                        file.createNewFile();
                        FileOutputStream out = new FileOutputStream(file);
                        int b;
                        while ((b = in.read()) != -1) {
                            out.write(b);
                        }
                        out.close();
                    }
                    // 读取下一个ZipEntry
                    entry = in.getNextEntry();
                }
            }
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 压缩文件（例如图片） --->返回要判断是否为null
    public static File compressImg(File file) throws IOException {
        String orientation = "";
        int angle = 0;
        Bitmap mBitmap = null;
        Bitmap tmpBitmap = null;
        OutputStream os = null;
        Bitmap btm = null;

        // 文件不存在
        if (!file.exists()) {
            return null;
        }

        String filepath = file.getAbsolutePath();
        orientation = ImageUtils.getOrientation(filepath);

        // 若文件小于指定大小，则不进行压缩
        if (file.length() > FILE_IMAGE_MAXSIZE) {
            try {
                String path = CACHE_SAVE_IMG_PATH;
                File tmpfile = new File(path);
                if (!tmpfile.exists()) {
                    tmpfile.mkdirs();
                }
                tmpfile = new File(path, "tmp.jpg");

                while (file.length() > FILE_IMAGE_MAXSIZE) {

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
                    opts.inSampleSize = 2;
                    // inJustDecodeBounds设为false表示把图片读进内存中
                    opts.inJustDecodeBounds = false;
                    // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
                    btm = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

                    // 创建文件输出流
                    if (btm == null) {
                        return file;
                    }

                    tmpfile = new File(path, "tmp.jpg");
                    os = new FileOutputStream(tmpfile);
                    // 存储
                    btm.compress(CompressFormat.JPEG, 100, os);
                    // 关闭流
                    os.flush();
                    os.close();
                    if (btm != null && !btm.isRecycled()) {
                        btm.recycle();
                    }
                    file = new File(path + file.getName());
                    tmpfile.renameTo(file);
                }
            } catch (Exception e) {
                return null;
            }
        }

        return file;
    }

    // 压缩文件
    public static File compressiconImg1(File file, float Width, float Heigth) throws IOException {
        String orientation = "";
        OutputStream os = null;
        // 文件不存在
        if (!file.exists()) {
            return null;
        }
        String filepath = file.getAbsolutePath();
        // orientation = ImageUtils.getOrientation(filepath);

        // 若文件小于指定大小，则不进行压缩
        // if (file.length() > BaseConstants.FILE_IMAGE_ICONSIZE) {
        try {
            String path = CACHE_SAVE_IMG_PATH;
            File tmpfile = new File(path);
            if (!tmpfile.exists()) {
                tmpfile.mkdirs();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // 获取这个图片的宽和高
            BitmapFactory.decodeFile(filepath, options); // 此时返回bm为空
            // options.inJustDecodeBounds = false;
            // 获得图片的宽高
            // int width = options.getWidth();
            // int height = options.getHeight();
            int width = options.outWidth;
            int height = options.outHeight;
            // 计算缩放比例
            float scaleWidth = ((float) Width) / width;
            float scaleHeight = ((float) Heigth) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(filepath, options);
            // 得到新的图片 www.2cto.com
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

            // 创建文件输出流
            if (bitmap == null) {
                return file;
            }

            tmpfile = new File(path, "tmp.jpg");
            os = new FileOutputStream(tmpfile);
            // 存储
            bitmap.compress(CompressFormat.JPEG, 75, os);
            if (!bitmap.isRecycled()) {

                bitmap.recycle();
                System.gc();
            }
            // 关闭流
            os.flush();
            os.close();

            file = new File(path + file.getName());
            tmpfile.renameTo(file);

        } catch (Exception e) {
            return file;

        }
        // }
        return file;
    }

    // 压缩文件
    public static File compressiconImg(File file, float Width, float Heigth) throws IOException {
        String orientation = "";
        OutputStream os = null;
        // 文件不存在
        if (!file.exists()) {
            return null;
        }
        String filepath = file.getAbsolutePath();
        orientation = ImageUtils.getOrientation(filepath);

        // 若文件小于指定大小，则不进行压缩
        // if (file.length() > BaseConstants.FILE_IMAGE_ICONSIZE) {
        try {
            String path = CACHE_SAVE_IMG_PATH;
            File tmpfile = new File(path);
            if (!tmpfile.exists()) {
                tmpfile.mkdirs();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            // 获取这个图片的宽和高
            Bitmap bitmap = BitmapFactory.decodeFile(filepath, options); // 此时返回bm为空
            options.inJustDecodeBounds = false;
            // 获得图片的宽高
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            // 计算缩放比例
            float scaleWidth = ((float) Width) / width;
            float scaleHeight = ((float) Heigth) / height;
            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);
            // 得到新的图片 www.2cto.com
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

            // 创建文件输出流
            if (bitmap == null) {
                return file;
            }

            tmpfile = new File(path, "tmp.jpg");
            os = new FileOutputStream(tmpfile);
            // 存储
            bitmap.compress(CompressFormat.JPEG, 80, os);
            // 关闭流
            os.flush();
            os.close();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            System.gc();
            file = new File(path + file.getName());
            tmpfile.renameTo(file);

        } catch (OutOfMemoryError e) {

            return file;
        }
        // }
        return file;
    }

    /**
     * 判断文件是否为图片文件(GIF,PNG,JPG)
     *
     * @param srcFileName
     * @return
     */
    public static boolean isImage(String srcFileName) {
        boolean isImage = false;
        if (!srcFileName.contains(".")) {
            return isImage;
        }
        String imagetype = srcFileName.substring(srcFileName.lastIndexOf("."));

        if (imagetype.equalsIgnoreCase(".jpg") || imagetype.equalsIgnoreCase(".gif") || imagetype.equalsIgnoreCase(".jpeg") || imagetype.equalsIgnoreCase(".bmp") || imagetype.equalsIgnoreCase(".png")) {
            isImage = true;
        }
        return isImage;
    }

    // 删除numDays之前的文件夹
    public static int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (StringUtils.isEmpty(fileName))
            return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }

    /**
     * InputStream转Byte
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] InputStreamToByte(InputStream in) throws IOException {
        int BUFFER_SIZE = 4096;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    // 压缩文件
    public static File compressImg(File file, int width) throws IOException {
        String orientation = "";
        OutputStream os = null;
        // 文件不存在
        if (!file.exists()) {
            return null;
        }
        String filepath = file.getAbsolutePath();
        orientation = ImageUtils.getOrientation(filepath);

        // 若文件小于指定大小，则不进行压缩
        // if (file.length() > BaseConstants.FILE_IMAGE_ICONSIZE) {
        try {
            String path = CACHE_SAVE_IMG_PATH;
            File tmpfile = new File(path);
            if (!tmpfile.exists()) {
                tmpfile.mkdirs();
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            // 获取这个图片的宽和高
            Bitmap bitmap = BitmapFactory.decodeFile(filepath, options); // 此时返回bm为空
            options.inJustDecodeBounds = false;
            bitmap = ImageUtils.getImageThumbnail(filepath, width);

            // 创建文件输出流
            if (bitmap == null) {
                return file;
            }

            tmpfile = new File(path, "tmp.jpg");
            os = new FileOutputStream(tmpfile);
            // 存储
            bitmap.compress(CompressFormat.JPEG, 75, os);
            // 关闭流
            os.flush();
            os.close();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            file = new File(path + file.getName());
            tmpfile.renameTo(file);

        } catch (Exception e) {
            return null;
        }
        // }
        return file;
    }

    public static File newcompressImg(File filepath) {

        String path = CACHE_SAVE_IMG_PATH;
        File tmpfile = new File(path);
        if (!tmpfile.exists()) {
            tmpfile.mkdirs();
        }
        File file = writeImageToDisk(decodeBitmap(filepath.getAbsolutePath()), path + filepath.getName());
        return file;
    }

    /**
     * 将图片写入到磁盘
     *
     * @param img      图片数据流
     * @param fileName 文件保存时的名称
     */
    public static File writeImageToDisk(byte[] img, String fileName) {
        try {
            File file = new File(fileName);
            FileOutputStream fops = new FileOutputStream(file);
            fops.write(img);
            fops.flush();
            fops.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decodeBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = computeSampleSize2(opts, -1, 1024 * 800);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        FileInputStream is = null;
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(path);
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            double scale = getScaling(opts.outWidth * opts.outHeight, 1024 * 600);
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp, (int) (opts.outWidth * scale), (int) (opts.outHeight * scale), true);
            bmp.recycle();
            baos = new ByteArrayOutputStream();
            bmp2.compress(CompressFormat.JPEG, 80, baos);
            bmp2.recycle();
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return baos.toByteArray();
    }

    public static int computeSampleSize2(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize2(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize2(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private static double getScaling(int src, int des) {
        /**
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
         */
        double scale = Math.sqrt((double) des / (double) src);
        return scale;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    // 压缩文件（例如图片） --->返回要判断是否为null
    public static File compressImg(File file, long size) throws IOException {
        String orientation = "";
        int angle = 0;
        Bitmap mBitmap = null;
        Bitmap tmpBitmap = null;
        OutputStream os = null;
        Bitmap btm = null;

        // 文件不存在
        if (!file.exists()) {
            return null;
        }

        String filepath = file.getAbsolutePath();
        orientation = ImageUtils.getOrientation(filepath);

        // 若文件小于指定大小，则不进行压缩
        if (file.length() > size) {
            try {
                String path = CACHE_SAVE_IMG_PATH;
                File tmpfile = new File(path);
                if (!tmpfile.exists()) {
                    tmpfile.mkdirs();
                }
                tmpfile = new File(path, "tmp.jpg");

                while (file.length() > size) {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
                    opts.inSampleSize = 2;
                    // inJustDecodeBounds设为false表示把图片读进内存中
                    opts.inJustDecodeBounds = false;
                    // 设置大小，这个一般是不准确的，是以i'nSampleSize的为准，但是如果不设置却不能缩放
                    btm = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);

                    /**
                     * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
                     */
                    int degree = readPictureDegree(file.getAbsolutePath());

                    if (degree != 0) {
                        // 修复图片被旋转的角度
                        btm = rotaingImageView(degree, btm);
                    }

                    // 创建文件输出流
                    if (btm == null) {
                        return file;
                    }

                    tmpfile = new File(path, "tmp.jpg");
                    os = new FileOutputStream(tmpfile);
                    // 存储
                    btm.compress(CompressFormat.JPEG, 100, os);
                    // 关闭流
                    os.flush();
                    os.close();
                    if (btm != null && !btm.isRecycled()) {
                        btm.recycle();
                        btm = null;
                    }
                    file = new File(path + file.getName());
                    tmpfile.renameTo(file);
                }
            } catch (Exception e) {
                return null;
            }
        }

        return file;
    }

    public static File compressFile(String oldpath, String newPath) {
        Bitmap compressBitmap = FileUtils.decodeFile(oldpath);
        Bitmap newBitmap = ratingImage(oldpath, compressBitmap);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        newBitmap.compress(CompressFormat.PNG, 100, os);
        byte[] bytes = os.toByteArray();

        File file = null;
        try {
            file = FileUtils.getFileFromBytes(bytes, newPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (newBitmap != null) {
                if (!newBitmap.isRecycled()) {
                    newBitmap.recycle();
                }
                newBitmap = null;
            }
            if (compressBitmap != null) {
                if (!compressBitmap.isRecycled()) {
                    compressBitmap.recycle();
                }
                compressBitmap = null;
            }
        }
        return file;
    }

    private static Bitmap ratingImage(String filePath, Bitmap bitmap) {
        int degree = readPictureDegree(filePath);
        return rotaingImageView(degree, bitmap);
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属�?�：旋转的角�?
     *
     * @param path 图片绝对路径
     * @return degree旋转的角�?
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

    /**
     * 把字节数组保存为�?个文�?
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            // log.error("helper:get file from byte process error!");
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 图片压缩
     *
     * @param fPath
     * @return
     */
    public static Bitmap decodeFile(String fPath) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        opts.inDither = false; // Disable Dithering mode
        opts.inPurgeable = true; // Tell to gc that whether it needs free
        opts.inInputShareable = true; // Which kind of reference will be used to
        BitmapFactory.decodeFile(fPath, opts);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        if (opts.outHeight > REQUIRED_SIZE || opts.outWidth > REQUIRED_SIZE) {
            final int heightRatio = Math.round((float) opts.outHeight / (float) REQUIRED_SIZE);
            final int widthRatio = Math.round((float) opts.outWidth / (float) REQUIRED_SIZE);
            scale = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        Log.i("scale", "scal =" + scale);
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = scale;
        Bitmap bm = BitmapFactory.decodeFile(fPath, opts).copy(Config.ARGB_8888, false);
        return bm;
    }

    /**
     * 创建目录
     *
     * @param path
     */
    public static void setMkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
            Log.e("file", "目录不存�?  创建目录    ");
        } else {
            Log.e("file", "目录存在");
        }
    }

    /**
     * 获取目录名称
     *
     * @param url
     * @return FileName
     */
    public static String getFileName(String url) {
        int lastIndexStart = url.lastIndexOf("/");
        if (lastIndexStart != -1) {
            return url.substring(lastIndexStart + 1, url.length());
        } else {
            return null;
        }
    }

    /**
     * 删除该目录下的文�?
     *
     * @param path
     */
    public static void delFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 将bitmap转化成图片
     */
    public static String saveImg(Bitmap b) throws Exception {

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 照片命名
        String cropFileName = timeStamp + ".jpg";
        // 裁剪头像的绝对路径
        String protraitPath = CACHE_SAVE_IMG_PATH + cropFileName;
        File dirFile = new File(CACHE_SAVE_IMG_PATH);
        File mediaFile = new File(protraitPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        if (mediaFile.exists()) {
            mediaFile.delete();
        }
        if (!new File(CACHE_SAVE_IMG_PATH).exists()) {
            new File(CACHE_SAVE_IMG_PATH).mkdirs();
        }
        mediaFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(mediaFile);
        b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
        b.recycle();
        b = null;
        System.gc();
        return mediaFile.getPath();
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return "0KB";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}