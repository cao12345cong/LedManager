package com.clt.util;

import android.util.Log;

/**
 * 统一管理Log的打印和不打印，当项目做完后，应该设置不再打印
 * @author caocong
 * 2013.10.28
 */
public class LogUtil
{
	/** 设置为false 后将不会再输出日志信息.*/
    private static boolean isOutputLog = false;

    public static void i(String tag, String msg)
    {
        if (isOutputLog == true)
        {
        	Log.i(tag, msg);
        }
        
    }

    public static void i(String tag, int msg)
    {
        i(tag, String.valueOf(msg));
    }

    public static void i(String tag, boolean msg)
    {
        i(tag, String.valueOf(msg));
    }

    public static void i(String tag, long msg)
    {
        i(tag, String.valueOf(msg));
    }

}
