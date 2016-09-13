package com.clt.service;

import com.clt.activity.MainActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 
 *工厂
 */
public class BaseServiceFactory
{
    /**
     * 获得服务类
     */
    public static Class getBaseService(){
        
       return NetService.class;
    }
}
