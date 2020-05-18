package com.glavesoft.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StringUtils
{

    /**
     * 把输入流转换成字符数组
     *
     * @param inputStream 输入流
     * @return 字符数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream) throws Exception
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1)
        {
            bout.write(buffer, 0, len);
        }
        bout.close();
        inputStream.close();

        return bout.toByteArray();
    }

    public static String replaceUrlWithPlus(String url)
    {
        if (url != null)
        {
            // return url.replaceAll("http://(.)*?/",
            // "").replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
            return url.replaceAll("[/|&|?|:|%]+", "_");
        }
        return null;
    }

    // 获得汉语拼音首字母
    public static String getAlpha(String str)
    {
        if (str == null)
        {
            return "#";
        }

        if (str.trim().length() == 0)
        {
            return "#";
        }

        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches())
        {
            return (c + "").toUpperCase();
        } else
        {
            return "#";
        }
    }

    // public static String SetUrlToFilePath(int fileType, String url) {
    // String filePath = "";
    // switch (fileType) {
    // case BaseConstant.CACHE_FILETYPE_FILE:
    // filePath = BaseConstant.CACHE_FILE_PATH;
    // break;
    //
    // default:
    // break;
    // }
    // filePath += url.replaceAll("[/|&|?|:|%]+", "_")+".dat";
    // return filePath;
    // }

    /*
     * MD5加密（32位）
     */
    public static String toMD5(String str)
    {
        MessageDigest messageDigest = null;
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return "";
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++)
        {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        // return md5StrBuff.toString();
        return md5StrBuff.substring(0, 3).toString().toUpperCase();
    }

    // 首页tab字体换行大小
    public static SpannableString changeFontSize(String tmpString, int size_big, int size_small)
    {
        SpannableString ss = new SpannableString(tmpString);
        ss.setSpan(new AbsoluteSizeSpan(size_big), 0, tmpString.indexOf("\n"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(size_small), tmpString.indexOf("\n"), tmpString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // if(ScreenUtils.getInstance().getWidth()==480){
        // ss.setSpan(new AbsoluteSizeSpan(21), 0, tmpString.indexOf("\n"),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // ss.setSpan(new AbsoluteSizeSpan(10), tmpString.indexOf("\n"),
        // tmpString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // }else if(GlobalSource.screenWidth==320){
        // ss.setSpan(new AbsoluteSizeSpan(14), 0, tmpString.indexOf("\n"),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // ss.setSpan(new AbsoluteSizeSpan(8), tmpString.indexOf("\n"),
        // tmpString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // }else if(GlobalSource.screenWidth==240){
        // ss.setSpan(new AbsoluteSizeSpan(12), 0, tmpString.indexOf("\n"),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // ss.setSpan(new AbsoluteSizeSpan(6), tmpString.indexOf("\n"),
        // tmpString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // }
        return ss;
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input)
    {
        if (input == null || "".equals(input)) return true;

        for (int i = 0; i < input.length(); i++)
        {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n')
            {
                return false;
            }
        }
        return true;
    }

    public static boolean isMobileNO(String mobile)
    {
//      return (mobile != null && mobile.length() == 11 && mobile.startsWith("1"));
        return (mobile != null);
//		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//		Matcher m = p.matcher(mobile);
//		return m.matches();

    }

    /**
     * 将字符串转成MD5值
     *
     * @param txt
     * @return
     */
    public static String strToMD5(String txt)
    {
        byte[] hash;

        try
        {
            hash = MessageDigest.getInstance("MD5").digest(txt.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return "";
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash)
        {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     *
     * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
     * 实现步骤: <br>
     *
     * @param paraMap   要排序的Map对象
     * @param urlEncode   是否需要URLENCODE
     * @param keyToLower    是否需要将Key转换为全小写
     *            true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower)
    {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try
        {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>()
            {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2)
                {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds)
            {
                if (!StringUtils.isEmpty(item.getKey()))
                {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode)
                    {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower)
                    {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else
                    {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false)
            {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e)
        {
            return null;
        }
        return buff;
    }



}
