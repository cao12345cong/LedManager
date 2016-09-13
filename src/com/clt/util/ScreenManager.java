/*
 * 文件名：ScreenManager.java
 * 描述：TODO
 * 修改人：王鹏
 * 修改时间：上午10:34:35
 * 修改内容：待定.
 * @since 
 */
package com.clt.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 获取屏幕分辨率
 * @author Abelart.
 */
public final class ScreenManager
{
    private DisplayMetrics dMetrics = new DisplayMetrics();

    private static Context activity;

    public ScreenManager(Activity activity)
    {
        this.activity = activity;
        activity.getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
    }

    /**获取屏幕分辨率宽度.*/
    public int getScreenH()
    {

        return dMetrics.heightPixels;
    }

    /**获取屏幕分辨率高度.*/
    public int getScreenW()
    {
        return dMetrics.widthPixels;
    }

    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */
    public static int dip2px(float dpValue)
    {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */
    public static int px2dip(float pxValue)
    {
        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
