package com.glavesoft.pawnuser.mod;

import com.google.gson.annotations.Expose;

/**
 * 基本数据结构
 */
public class DataResult<T>
{
    @Expose
    private String errorMsg = "";
    @Expose
    private int errorCode;
    @Expose
    private T data; // 数据返回
    private String total;

    public static final int RESULT_OK_ZERO = 0;// 发送成功
    public static final int RESULT_OK = 0;// 发送成功

    public static final int RESULT_10000 = 10000;// 没有数据
    public static final int RESULT_1002 = 1002;// 缺少deviceid
    public static final int RESULT_102 = 102;// 登录验证失败
    public static final int RESULT_1004 = 1004;// 缺少分页参数page
    public static final int RESULT_1005 = 1005;// 缺少分页参数limit
    public static final int RESULT_1007 = 1007;// 缺失签名参数sign
    public static final int RESULT_1008 = 1008;// 签名错误
    public static final int RESULT_1009 = 1009;// 缺失签名参数timestamp
    public static final int RESULT_1010 = 1010;// 签名失效
    public static final int RESULT_1011 = 1011;// 缺失签名参数nonce

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(int errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
