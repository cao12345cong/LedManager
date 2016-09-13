package com.clt.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.clt.ledmanager.R;

public class DialogUtil
{
    /**
     * 缓冲的提示框
     * 
     * @param context
     */
    public static AlertDialog createDownloadDialog(Context context)
    {
        Builder builder = new Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progressbar, null);
        builder.setView(view);
        return builder.create();
    }

    /**
     * 输入密码的提示框
     * @param context
     * @return
     */
    public static AlertDialog createEnterPasswordDialog(Context context)
    {
        Builder builder = new Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.entry_password, null);
        builder.setView(view);
        return builder.create();
    }

    public static void toast(Context context, String msg, int time)
    {
        Toast.makeText(context, msg, time).show();
    }
    /**
     * 被踢掉的提示框
     * @param context
     * @return
     */
    public static AlertDialog createKickOfDialog(Context context){
        AlertDialog dialog=null;
        Builder builder = new Builder(context);
        builder.setTitle(context.getResources().getString(R.string.kick_of));
        builder.setPositiveButton(context.getResources().getString(R.string.submit), new OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        dialog=builder.create();
        return dialog;
    }
    
    public static AlertDialog createConnBreakDialog(Context context){
        AlertDialog dialog=null;
        Builder builder = new Builder(context);
        builder.setTitle("连接断开");
        builder.setPositiveButton(context.getResources().getString(R.string.submit), new OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        
        dialog=builder.create();
        return dialog;
        
    }

}
