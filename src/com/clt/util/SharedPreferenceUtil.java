package com.clt.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * 偏好设置
 * @author caocong
 * 2013.3.17
 *
 */
public class SharedPreferenceUtil
{
    private static final String FILE_NAME = "settings";

    private SharedPreferences sp;
    
    private Context context;
    
    private static SharedPreferenceUtil instance;

    /**key**/
    public static class ShareKey
    {
        public static final String KEY_BRIGHT = "key_bright";// 亮度

        public static final String KEY_COLOR_TEMP = "key_color_temp";// 色温
        
        public static final String KEY_WIN_WIDTH = "key_win_width";//窗口宽度
        
        public static final String KEY_WIN_HEIGHT = "key_win_height";//窗口高度
        
        //public static final String TerminateName = "terminateName";// 终端名

        //public static final String TerminateAddress = "terminateAddress";// 终端地址

        public static final String LANGUAGE = "language";// 应用的语言
    }
    public static SharedPreferenceUtil getInstance(Context context,String fileName){
        if(instance==null){
            instance=new SharedPreferenceUtil(context,fileName);
        }
        return instance;
    }
    private SharedPreferenceUtil(Context context,String fileName)
    {
       this.context=context;
       if(TextUtils.isEmpty(fileName)||fileName==null){
           fileName=FILE_NAME;
       }
       sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }
    /**
     * 一些列读操作
     */
    public int getInt(String key, int defValue){
        return sp.getInt(key, defValue);
    }
    public boolean getBoolean(String key, boolean defValue){
        return sp.getBoolean(key, defValue);
    }
    public float getFloat(String key, float defValue){
        return sp.getFloat(key, defValue);
    }
    public float getLong(String key, long defValue){
        return sp.getLong(key, defValue);
    }
    public String getString(String key, String defValue){
        return sp.getString(key, defValue);
    }
    
    /**
     * 一系列写的操作
     */
    public void putInt(String key, int value)
    {
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public void putBoolean(String key, boolean value)
    {
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void putFloat(String key, float value)
    {
        Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
    public void putLong(String key, long value)
    {
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }
    public void putString(String key, String value)
    {
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
