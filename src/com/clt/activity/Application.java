package com.clt.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.clt.activity.Application.Terminate;
import com.clt.commondata.LedTerminateInfo;
import com.clt.commondata.SenderInfo;
import com.clt.commondata.SomeInfo;
import com.clt.entity.Program;
import com.clt.entity.SendCardType;
import com.clt.util.ApkUtil;

import android.content.SharedPreferences;
import android.text.TextUtils;

public class Application extends android.app.Application
{
    private static Application itBookApp = null;
    /**
     * 全局变量
     */

    private String language;// 语言

    private SendCardType sendCardType;// 发送卡类型

    private HashMap<String, Terminate> ip2TerminateMap;

    public ArrayList<Program> programs;//当前的节目清单
    
    /**
     * 
     */
    
    public boolean isOnline;// 是否在线
    
    public SenderInfo senderInfo;// 发送卡信息
    
    private boolean needChangeLanguage=false;
    
    public SomeInfo someInfo;//一些信息
    
    public LedTerminateInfo ledTerminateInfo;//当前的连接终端
    
    public static final String operationPassword="168";

    public boolean isNeedChangeLanguage()
    {
        return needChangeLanguage;
    }

    public void setNeedChangeLanguage(boolean needChangeLanguage)
    {
        this.needChangeLanguage = needChangeLanguage;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        itBookApp = this;
        setSystemLanguage();
        sendCardType = SendCardType.T7;
        ip2TerminateMap = new HashMap<String, Application.Terminate>();
        programs=new ArrayList<Program>();
    }

    public SendCardType getSendCardType()
    {
        return sendCardType;
    }

    public void setSendCardType(SendCardType sendCardType)
    {
        this.sendCardType = sendCardType;
    }

    /**
     * 设置系统语言
     */
    public void setSystemLanguage()
    {
        ApkUtil.configLanguage(getLanguage());
    }

    /**
     * 单例
     * @return
     */
    public static Application getInstance()
    {
        return itBookApp;
    }

    /**
     * 若没设置语言则 根据系统语言
     */
    public String getLanguage()
    {
        SharedPreferences shared = this.getSharedPreferences("sys_language",
                MODE_PRIVATE);
        language = shared.getString("language", ApkUtil.getSysLanguage());
        return language;
    }

    /**
     * 设置系统语言
     * @param language
     */
    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * 保存系统语言到偏好设置
     * 
     * @param language
     */
    public void xmlLanguage(String language)
    {
        SharedPreferences sp = getSharedPreferences("sys_language",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("language", language);
        editor.commit();
    }

//    public SenderInfo getSenderInfo()
//    {
//        return senderInfo;
//    }
//
//    public void setSenderInfo(SenderInfo senderInfo)
//    {
//        this.senderInfo = senderInfo;
//        if (senderInfo == null)
//        {
//            return;
//        }
//        String senderType = senderInfo.getSenderType();
//        if (!TextUtils.isEmpty(senderType))
//        {
//            if (senderType.toLowerCase().startsWith("it7"))
//            {
//                this.sendCardType = SendCardType.T7;
//            }
//            else if (senderType.toLowerCase().startsWith("iq7"))
//            {
//                this.sendCardType = SendCardType.IQ7;
//            }
//        }
//    }

    private void uncaughtException()
    {
        
    }

    /**
     * 设置服务端列表映射
     * @param ledList
     */
    public void setIp2TerminateMap(ArrayList<LedTerminateInfo> ledList)
    {
        String ip = null;
        String password = null;
        for (LedTerminateInfo ledTerminateInfo : ledList)
        {
            ip = ledTerminateInfo.getIpAddress();
            password = ledTerminateInfo.getPassword();
            if (ip2TerminateMap.containsKey(ip))
            {
                String oldPassword = ip2TerminateMap.get(ip).getPassword();
                if (!oldPassword.equals(password))
                {
                    ip2TerminateMap.put(ip, new Terminate(ip, password, false));
                }
            }
            else
            {
                ip2TerminateMap.put(ip, new Terminate(ip, password, false));
            }
        }
    }

    public HashMap<String, Terminate> getIp2TerminateMap()
    {
        return ip2TerminateMap;
    }

    public void setIp2TerminateMap(HashMap<String, Terminate> ip2TerminateMap)
    {
        this.ip2TerminateMap = ip2TerminateMap;
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }

    /**
     * 记录输入过密码的服务端
     */
    class Terminate
    {
        String ip;

        String password;

        boolean hasEnteredPass;

        public Terminate(String ip, String password, boolean hasEnteredPass)
        {
            super();
            this.ip = ip;
            this.password = password;
            this.hasEnteredPass = hasEnteredPass;
        }

        public String getIp()
        {
            return ip;
        }

        public void setIp(String ip)
        {
            this.ip = ip;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }

        public boolean isHasEnteredPass()
        {
            return hasEnteredPass;
        }

        public void setHasEnteredPass(boolean hasEnteredPass)
        {
            this.hasEnteredPass = hasEnteredPass;
        }

    }

    public boolean isOnline()
    {
        return isOnline;
    }

    public void setOnline(boolean isOnline)
    {
        this.isOnline = isOnline;
    }

    /**
     * 退出
     */
    public void quit()
    {
        ip2TerminateMap.clear();
        senderInfo = null;
        sendCardType = null;
        someInfo=null;
        System.exit(0);
    }

    /**
     * 
     */
    public void clear(){
        isOnline=false;
        needChangeLanguage=false;
        senderInfo=null;
        someInfo=null;
        ledTerminateInfo=null;
    }
}
