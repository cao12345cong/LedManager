package com.clt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * netutil.
 * @author Abelart.
 */
public class NetUtil
{
    private static final String TAG = "NetUtil";

    /**
     * 网络连接是否可用
     */
    public static boolean isConnnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager)
        {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo)
            {
                for (NetworkInfo info : networkInfo)
                {
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否是wifi连接
     */
    public static boolean isWifiConnect(Context context)
    {

        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mWifi = connManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return mWifi.isConnected();

    }
    
    /**
     * 获得本机Ip地址
     * @return
     */
    public static String getIpAddress(Context context) { 
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        
        WifiInfo wifiInfo = wifiManager.getConnectionInfo(); 
        int ipAddress = wifiInfo.getIpAddress(); 
         
        // 格式化IP address，例如：格式化前：1828825280，格式化后：192.168.1.109 
        String ip = String.format("%d.%d.%d.%d", 
                (ipAddress & 0xff), 
                (ipAddress >> 8 & 0xff), 
                (ipAddress >> 16 & 0xff), 
                (ipAddress >> 24 & 0xff)); 
        return ip; 
         
    } 
    
    /**
     * 获得本机Ip地址的前三位
     * @return
     */
    public static String getNetNumIpAddress(Context context) { 
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        
        WifiInfo wifiInfo = wifiManager.getConnectionInfo(); 
        int ipAddress = wifiInfo.getIpAddress(); 
         
        // 格式化IP address，例如：格式化前：1828825280，格式化后：192.168.1.109 
        String ip = String.format("%d.%d.%d", 
                (ipAddress & 0xff), 
                (ipAddress >> 8 & 0xff), 
                (ipAddress >> 16 & 0xff)); 
        return ip; 
         
    } 

}