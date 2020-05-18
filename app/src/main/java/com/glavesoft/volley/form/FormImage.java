package com.glavesoft.volley.form;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class FormImage
{

    private String mName;

    private String mFileName;

    private String mValue;

    private String mMime;

    private Bitmap mBitmap;

    public FormImage(Bitmap mBitmap)
    {
        this.mBitmap = mBitmap;
    }

    public String getName()
    {
        return "file_upload_pic";
    }

    public String getFileName()
    {
        return "upload.png";
    }

    public byte[] getValue()
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        return bos.toByteArray();
    }

    public String getStringValue()
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        return bos.toString();
    }

    public String getMime()
    {
        return "image/png";
    }
}
