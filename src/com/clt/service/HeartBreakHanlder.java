package com.clt.service;

import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

import com.clt.TCPConnector;
import com.clt.netmessage.NMHeartBreak;
import com.google.gson.Gson;
/**
 * 
 *心跳处理
 */
public class HeartBreakHanlder
{
    private static final boolean useHeartBreak=false;//是否用心跳
    
    private static final int HEART_BREAK_SPAN = 10000;//心跳发送一次的时间间隔

    private static final int HEART_TIME_OUT = 60000;// 心跳的超时时间 

    private long lastReceiverMessageTime;// 最近一次收到心跳的时间

    private OnHeartBreakListener listener;

    private Timer receiverTimer;// 接收定时器

    private TCPConnector connector;

    // WaitTenSecondThread waitThread;

    private boolean isWaiting;

    public void setOnHeartBreakListener(OnHeartBreakListener listener)
    {
        this.listener = listener;
    }

    public HeartBreakHanlder(TCPConnector connector)
    {
        this.connector = connector;
    }

    /**
     * 接收到一个心跳包，清空
     */
    public void recevierOneHeartBreak()
    {
        if(!useHeartBreak){
            return;
        }
        
        lastReceiverMessageTime = System.currentTimeMillis();
        
        if (isWaiting)
        {
            return;
        }

        receiverTimer.cancel();
        receiverTimer = null;
        start();
    }

    /**
     * 开始
     */
    public void start()
    {
        if(!useHeartBreak){
            return;
        }
        new Thread()
        {

            @Override
            public void run()
            {
                // 等10秒
                // waitThread=new WaitTenSecondThread();
                // waitThread.start();
                // waitThread.join();
                try
                {
                    isWaiting = true;
                    Thread.sleep(10000);

                    // 发心跳
                    connector.addOneMessage(new NMHeartBreak());
                    // 接收
                    //lastReceiverMessageTime = System.currentTimeMillis();
                    receiverTimer = new Timer();
                    receiverTimer.schedule(new MyTimeTask(), 0, 1000);

                    isWaiting = false;
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    /**
     * 停止接收
     */
    public void stop()
    {
        if(!useHeartBreak){
            return;
        }
        receiverTimer.cancel();
    }

    /**
     * 计时器
     */
    class MyTimeTask extends TimerTask
    {

        @Override
        public void run()
        {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastReceiverMessageTime >= HEART_TIME_OUT)
            {
                if (listener != null)
                {
                    stop();
                    listener.onReceiverHeartBreakFail();
                }
            }
        }
    };

    // class WaitTenSecondThread extends Thread{
    //
    // public void run() {
    //
    // try
    // {
    // sleep(10000);
    // }
    // catch (InterruptedException e)
    // {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // };
    public interface OnHeartBreakListener
    {
        public void onReceiverHeartBreakFail();
    }
}
