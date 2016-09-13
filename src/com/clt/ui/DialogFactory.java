package com.clt.ui;

import com.clt.ledmanager.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * Dialog装饰
 */
public class DialogFactory{
	
	private static final int WIDTH=400;
	private static final int HEIGHT=300;
	
	public static Dialog createDialog(Context context,View view){
		return createDialog(context, view, WIDTH);
		
	}
	public static Dialog createDialog(Context context,View view,int width){
		Dialog dialog=new Dialog(context, R.style.dialog);
		LayoutParams params = new LayoutParams(width, LayoutParams.FILL_PARENT);
		dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		dialog.addContentView(view, params);
		return dialog;
	}
}
