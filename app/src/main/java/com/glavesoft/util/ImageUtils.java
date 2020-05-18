/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.glavesoft.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.glavesoft.pawnuser.activity.main.ImagePageActivity;
import com.glavesoft.pawnuser.base.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class ImageUtils
{
	// public static String getThumbnailImagePath(String imagePath) {
	// String path = imagePath.substring(0, imagePath.lastIndexOf("/") + 1);
	// path += "th" + imagePath.substring(imagePath.lastIndexOf("/") + 1,
	// imagePath.length());
	// EMLog.d("msg", "original image path:" + imagePath);
	// EMLog.d("msg", "thum image path:" + path);
	// return path;
	// }
	/*
	 * 由URI的到绝对路径
	 */
	public static String getAbsoluteImagePath(Activity activity, Uri uri)
	{
		ContentResolver cr = activity.getContentResolver();
		String[] projection = new String[] { MediaStore.Audio.Media.DATA };
		Cursor cursor = cr.query(uri, projection, null, null, null);
		if (cursor == null || !cursor.moveToFirst())
		{
			return null;
		}
		int index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
		String path = cursor.getString(index);
		cursor.close();
		return path;
	}

	// 第一个参数是图片的路径，第二个参数是获取到的缩略图的宽度，第三个参数是获取到的缩略图的高度
	// 第一个参数是图片的路径，第二个参数是获取到的缩略图的宽度，第三个参数是获取到的缩略图的高度
	public static Bitmap getImageThumbnail(String imagePath, int width, int height)
	{
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight)
		{
			be = beWidth;
		} else
		{
			be = beHeight;
		}
		if (be <= 0)
		{
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/*
	 * 获取Bitmap通过Path
	 */
	public static Bitmap getBitmapByPath(String path)
	{
		Bitmap bitmap = null;
		if (FileUtils.isHasSDCard())
		{
			File file = new File(path);
			if (file.exists())
			{
				// if(FileUtils.isImage(path)){
				// 如果内存大于要显示的图片时，才显示
				if (SystemMemoryUtil.getAvailMemory(BaseApplication.getInstance().getApplicationContext()) > file.length())
				{
					// bitmap=BitmapFactory.decodeFile(path);
					InputStream inputStream = null;
					try
					{
						inputStream = new FileInputStream(path);
					} catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}

					BitmapFactory.Options opts = new BitmapFactory.Options();
					// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
					opts.inSampleSize = 2;
					// inJustDecodeBounds设为false表示把图片读进内存中
					opts.inJustDecodeBounds = false;
					// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
//					btm = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
					bitmap = BitmapFactory.decodeStream(inputStream);
				}
				// }
			}
		}
		return bitmap;
	}

	public static String getAbsolutePathFromNoStandardUri(Uri mUri)
	{
		String SDCARD_MNT = "/mnt/sdcard";
		String SDCARD = "/sdcard";

		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + SDCARD + File.separator;
		String pre2 = "file://" + SDCARD_MNT + File.separator;

		if (mUriString.startsWith(pre1))
		{
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2))
		{
			filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	/*
	 * 获取图片的旋转
	 */
	public static String getOrientation(String imgPath)
	{
		ExifInterface exifInterface;
		try
		{
			exifInterface = new ExifInterface(imgPath);
			int tag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			if (tag == ExifInterface.ORIENTATION_ROTATE_90)
			{// 如果是旋转地图片则先旋转
				return "90";
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_180)
			{// 如果是旋转地图片则先旋转
				return "180";
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_270)
			{// 如果是旋转地图片则先旋转
				return "270";
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return "0";
	}

	/*
	 * 获取相机拍照的图片地址
	 */
	public static String getCameraFileName()
	{
		String picPathString = "";
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH) + 1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String minute = String.valueOf(c.get(Calendar.MINUTE));
		String second = String.valueOf(c.get(Calendar.SECOND));
		String name = "IMG_" + year + "_" + month + "_" + day + "_" + hour + "_" + minute + "_" + second;
		picPathString = FileUtils.CACHE_SAVE_IMG_PATH;
		if (FileUtils.isHasSDCard())
		{
			File destDir = new File(picPathString);
			if (!destDir.exists())
			{
				destDir.mkdirs();
			}
		}
		picPathString = picPathString + name + ".jpg";
		return picPathString;
	}

	public static Bitmap getImageThumbnail(String imagePath, int width)
	{
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int be = beWidth;
		if (be <= 0)
		{
			be = 1;
		}
		if (w > width)
		{// 如果宽度大的话根据宽度固定大小缩放
			w = (int) width;
			h = options.outHeight * w / options.outWidth;
		}
		options.inSampleSize = be;
		options.outWidth = w;
		options.outHeight = h;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, w, h, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/*
	 * 图片地址是否存在
	 */
	public static boolean IsImageFileExist(String imagePath)
	{
		if (imagePath == null || imagePath.equals(""))
		{
			return false;
		}
		boolean isExist = false;
		if (FileUtils.isHasSDCard())
		{
			File file = new File(imagePath);
			// 文件存在
			if (file.exists())
			{
				isExist = true;
			}
		}
		return isExist;
	}

	@SuppressLint("NewApi")
	public static Bitmap imageZoom(Bitmap bitmap, double maxSize)
	{
		maxSize = 150;
		// 图片允许最大空间 单位：KB
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		if (mid > maxSize)
		{
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitmap = zoomImage(bitmap, bitmap.getWidth() / Math.sqrt(i), bitmap.getHeight() / Math.sqrt(i));
		}
		return bitmap;
	}

	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight)
	{
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	// bitmap转byte[]
	public static byte[] BitmapToBytes(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// 看大图
	public static void seeLargePhoto(Context context, String url)
	{
//		Intent intent = new Intent(context, LargePhotoActivity.class);
//		intent.putExtra("url", url);
//		context.startActivity(intent);
	}

	// 看大图（多图）
	public static void seeLargePhoto(Context context, ArrayList<String> urllist, int pos)
	{
		Intent intent = new Intent(context, ImagePageActivity.class);
		intent.putStringArrayListExtra("picurlList", urllist);
		intent.putExtra("selectPos", pos);
		context.startActivity(intent);
	}
}
