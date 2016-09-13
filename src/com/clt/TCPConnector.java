package com.clt;

import android.content.Context;

import com.clt.netmessage.NMBase;

/**
 *  TCP连接器
 */
public interface TCPConnector extends LifeCycle
{

    /**
     * 设置ip地址
     * @param ip
     */
    void setIpAddress(String ip);
    
    /**
     * 当前是否连接上
     * @return
     */
    boolean isConnecting();
    /**
     * 向消息队列中添加一条消息
     * @param nmBase
     */
    void addOneMessage(NMBase nmBase);
    
    /**
     * 返回消息
     * @param listener
     */
    void setOnHandlerMessageListener(OnHandlerMessageListener listener);
        

    
    
}
