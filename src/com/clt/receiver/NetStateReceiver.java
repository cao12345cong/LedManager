package com.clt.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;

public class NetStateReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        State wifiState = null;
        State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED == mobileState)
        {
            // 手机2G网连接成功
            //Log.i("NetStateReceiver", "手机网络连接成功");
        }
        else if (wifiState != null && mobileState != null
                && State.CONNECTED != wifiState
                && State.CONNECTED != mobileState)
        {
            // 手机没有任何的网络
            //Log.i("NetStateReceiver", "手机没有任何的网络");
        }
        else if (wifiState != null && State.CONNECTED == wifiState)
        {
            // 无线网络连接成功
            //Log.i("NetStateReceiver", "无线网络连接成功");
        }

    }

}
