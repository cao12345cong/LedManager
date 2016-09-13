package com.clt;

import com.clt.netmessage.NMBase;
import com.clt.service.TCPConnectorImpl;

/**
 * 消息拦截器
 */
public interface Interceptor
{
    void filterRequest(NMBase nmBase);
    void filterResponse(String jsonStr);
}
