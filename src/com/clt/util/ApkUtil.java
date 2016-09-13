package com.clt.util;

import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.clt.activity.Application;

/**
 * APK全局工具类
 * 
 * @author Administrator
 * 
 */
public class ApkUtil
{
    /**
     * 获取系统语言
     * 
     * @return
     */
    public static String getSysLanguage()
    {
        String language = Locale.getDefault().getLanguage();
        if (language.equalsIgnoreCase("zh"))
        {
            return Const.CN;
        }
        return language;
    }

    public static void configLanguage(String language)
    {
        // Locale locale = new Locale(Application.getInstance().getLanguage()
        // .equals("cn") ? "zh" : "en");
        // Locale.setDefault(locale);
        Resources resources = Application.getInstance().getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        if (language.equalsIgnoreCase(Const.CN))
        {// 中文
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        else if (language.equalsIgnoreCase(Const.EN))
        {// 英文
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, metrics);
    }
}
