package com.clt.service;

import android.app.Service;
import android.os.Binder;
import android.os.IBinder;

import com.clt.IService;
/**
 * Service的基类
 *
 */
public abstract class BaseService extends Service implements IService
{
    
    protected IBinder mBinder = new LocalBinder(this);
    
    public class LocalBinder extends Binder
    {
        private IService service;
        public LocalBinder(IService service)
        {
            this.service=service;
        }
        public IService getService()
        {
            return service;
        }
        
        
    }
    
}
