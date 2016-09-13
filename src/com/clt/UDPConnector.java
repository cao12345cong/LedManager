package com.clt;

import android.os.Handler;
/**
 * UDP连接器
 */
public interface UDPConnector extends LifeCycle
{
    void setHandler(Handler mHandler);
}
