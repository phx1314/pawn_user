package com.glavesoft

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.preference.PreferenceManager
import android.text.TextUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.glavesoft.pawnuser.R
import com.glavesoft.pawnuser.constant.BaseConstant
import com.glavesoft.pawnuser.converter.CustomGsonConverterFactory
import com.glavesoft.pawnuser.mod.LocalData
import com.glavesoft.pawnuser.service.ApiService
import com.google.gson.Gson
import com.guoxiaoxing.phoenix.compress.picture.internal.PictureCompressor
import com.guoxiaoxing.phoenix.compress.video.VideoCompressor
import com.guoxiaoxing.phoenix.compress.video.format.MediaFormatStrategyPresets
import com.guoxiaoxing.phoenix.core.PhoenixOption
import com.guoxiaoxing.phoenix.core.model.MediaEntity
import com.guoxiaoxing.phoenix.core.model.MimeType
import com.guoxiaoxing.phoenix.picker.Phoenix
import com.mdx.framework.Frame
import com.mdx.framework.model.ModelDx
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException


object F {
    var list_fx: ArrayList<ModelDx> = ArrayList()
    fun gB(TIME: Long = 50) = com.mdx.framework.service.gB(
        ApiService::class.java,
        BaseConstant.URL,
        LocalData.getInstance().getUserInfo().getToken(),
        TIME,
        CustomGsonConverterFactory.create()
    )

    fun init() {
        list_fx.add(ModelDx("钻石", 1))
        list_fx.add(ModelDx("彩色宝石", 2))
        list_fx.add(ModelDx("手表", 3))
        list_fx.add(ModelDx("珠宝饰品", 4))
        list_fx.add(ModelDx("翡翠及饰品", 5))
        list_fx.add(ModelDx("和田玉及饰品", 6))
        list_fx.add(ModelDx("贵金属及贵金属饰品", 7))
        list_fx.add(ModelDx("瓷器", 8))
        list_fx.add(ModelDx("书法", 9))
        list_fx.add(ModelDx("绘画", 10))
        list_fx.add(ModelDx("文玩杂项", 11))
        list_fx.add(ModelDx("其他物品", 12))

    }

    fun getVideoBody(
        filePath: String, compressFile: File,
        listener: VideoCompressor.Listener? = null
    ) {
        try {
            VideoCompressor.with().asyncTranscodeVideo(
                filePath, compressFile.absolutePath,
                MediaFormatStrategyPresets.createAndroid480pFormatStrategy(), listener
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getBody(
        filePath: String,
        mMediaType: String? = "image/jpg"
    ): MultipartBody.Part {
        var body: MultipartBody.Part
        val file = File(filePath)
        try {
            val compressFIle = PictureCompressor.with(Frame.CONTEXT)
                .savePath(Frame.CONTEXT.cacheDir.absolutePath)
                .load(file)
                .get()
            body =
                MultipartBody.Part.createFormData(
                    "file", compressFIle.name, RequestBody.create(
                        "application/octet-stream"?.toMediaTypeOrNull(), compressFIle
                    )
                )
        } catch (e: IOException) {
            e.printStackTrace()
            body =
                MultipartBody.Part.createFormData(
                    "file", file.name, RequestBody.create(
                        "application/octet-stream"?.toMediaTypeOrNull(), file
                    )
                )
        }
        return body
    }


    @Synchronized
    fun toast(message: String?) {
        if (!TextUtils.isEmpty(message)) {
            val toast =
                Toast.makeText(Frame.CONTEXT, "", Toast.LENGTH_SHORT)
            toast.setText(message)
            toast.show()
        }
    }

    fun takePhoto(
        act: Activity, requestCode: Int,
        fileType: Int = MimeType.ofImage(),
        maxPickNumber: Int = 1,
        minPickNumber: Int = 0,
        spanCount: Int = 4,
        enablePreview: Boolean = true,
        enableCamera: Boolean = true,
        enableAnimation: Boolean = true,
        enableCompress: Boolean = false,
        thumbnailHeight: Int = 160,
        thumbnailWidth: Int = 160,
        type: Int = PhoenixOption.TYPE_PICK_MEDIA
    ) {
        try {
            Phoenix.with()
                .theme(Color.parseColor("#12595B"))// 主题
                .fileType(fileType)//显示的文件类型图片、视频、图片和视频
                .maxPickNumber(maxPickNumber)// 最大选择数量
                .minPickNumber(minPickNumber)// 最小选择数量
                .spanCount(spanCount)// 每行显示个数
                .enablePreview(enablePreview)// 是否开启预览
                .enableCamera(enableCamera)// 是否开启拍照
                .enableAnimation(enableAnimation)// 选择界面图片点击效果
                .enableCompress(enableCompress)// 是否开启压缩
                .compressPictureFilterSize(1024)//多少kb以下的图片不压缩
                .compressVideoFilterSize(2018)//多少kb以下的视频不压缩
                .thumbnailHeight(thumbnailHeight)// 选择界面图片高度
                .thumbnailWidth(thumbnailWidth)// 选择界面图片宽度
                .enableClickSound(false)// 是否开启点击声音
//                .pickedMediaList(data)// 已选图片数据
                .videoFilterTime(0)//显示多少秒以内的视频
                .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                //如果是在Activity里使用就传Activity，如果是在Fragment里使用就传Fragment
                .start(act, type, requestCode);
            act.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun takePhoto(
        act: Activity,
        fileType: Int = MimeType.ofImage(),
        maxPickNumber: Int = 1,
        minPickNumber: Int = 0,
        spanCount: Int = 4,
        enablePreview: Boolean = true,
        enableCamera: Boolean = true,
        enableAnimation: Boolean = true,
        enableCompress: Boolean = false,
        thumbnailHeight: Int = 160,
        thumbnailWidth: Int = 160,
        type: Int = PhoenixOption.TYPE_BROWSER_PICTURE,
        data: List<MediaEntity>
    ) {
        try {
            Phoenix.with()
                .theme(Color.parseColor("#12595B"))// 主题
                .fileType(fileType)//显示的文件类型图片、视频、图片和视频
                .maxPickNumber(maxPickNumber)// 最大选择数量
                .minPickNumber(minPickNumber)// 最小选择数量
                .spanCount(spanCount)// 每行显示个数
                .enablePreview(enablePreview)// 是否开启预览
                .enableCamera(enableCamera)// 是否开启拍照
                .enableAnimation(enableAnimation)// 选择界面图片点击效果
                .enableCompress(enableCompress)// 是否开启压缩
                .compressPictureFilterSize(1024)//多少kb以下的图片不压缩
                .compressVideoFilterSize(2018)//多少kb以下的视频不压缩
                .thumbnailHeight(thumbnailHeight)// 选择界面图片高度
                .thumbnailWidth(thumbnailWidth)// 选择界面图片宽度
                .enableClickSound(false)// 是否开启点击声音
                .pickedMediaList(data)// 已选图片数据
                .videoFilterTime(0)//显示多少秒以内的视频
                .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                //如果是在Activity里使用就传Activity，如果是在Fragment里使用就传Fragment
                .start(act, type, 1);
            act.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T> data2Model(data: String?, mclass: Class<T>): T {
        return Gson().fromJson(data, mclass)
    }

    fun getJson(key: String): String? {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        return sp.getString(key, "")
    }

    fun saveJson(key: String, json: String?) {
        val sp = PreferenceManager.getDefaultSharedPreferences(Frame.CONTEXT)
        sp.edit().putString(key, json).apply()

    }

    fun logOut(context: Context?, isShow: Boolean = true, isFromTask: Boolean = false) {


    }


    // kfc 1
    // / 关闭软件盘
    fun closeSoftKey(act: Activity) {
        val localInputMethodManager =
            act.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val localIBinder = act.getWindow().getDecorView().getWindowToken()
        localInputMethodManager.hideSoftInputFromWindow(localIBinder, 2)
        // InputMethodManager imm = (InputMethodManager) getActivity()
        // .getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}












