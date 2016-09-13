package com.clt.service;

import java.util.Timer;
import java.util.TimerTask;

import com.clt.util.Const;

import android.content.Context;
import android.content.Intent;

/**
 * 计算连接时长
 *
 */
public class Clock
{
    private Context context;
    
    public static long pastTime;//以毫秒为单位
    
    private Timer timer;
    
    private static final long TIME_SPAN=10000;//10秒计时
    
    public Clock(Context context)
    {
        
        this.context=context;
    }
    /**
     * 
     */
    public void start(){
        this.timer=new Timer();
        timer.schedule(new MyTimeTask(), 10000,10000);
        
    }
    public void stop(){
        clear();
        timer.cancel();
        timer=null;
        
        
    }
    public void clear(){
        pastTime=0;
        
    }
    class MyTimeTask extends TimerTask{

        @Override
        public void run()
        {
            pastTime+=10000;
            
            Intent intent=new Intent(Const.CONNECT_TIME_ACTION);
            intent.putExtra("connTime", pastTime);
            context.sendBroadcast(intent);
        }
        
    }
}
