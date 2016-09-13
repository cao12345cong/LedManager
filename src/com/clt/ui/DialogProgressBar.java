package com.clt.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.clt.ledmanager.R;

public class DialogProgressBar {
	private Dialog dialog;
	/**
	 * 
	 * @param context  
	 * @param text    显示文字
	 */
	public DialogProgressBar(Context context,String text) {
		dialog=new Dialog(context, R.style.dialog);
		LayoutParams params = new LayoutParams(200, 200);
		dialog.setCanceledOnTouchOutside(true);
		LayoutInflater inflater=LayoutInflater.from(context);
		View view=inflater.inflate(R.layout.custom_dialog_progress, null);
		TextView tvInfo=(TextView) view.findViewById(R.id.tv_loading);
		if(!TextUtils.isEmpty(text)){
			tvInfo.setVisibility(View.VISIBLE);
			tvInfo.setText(text);
		}else{
			tvInfo.setVisibility(View.GONE);
		}
		dialog.addContentView(view, params);
	}
	/**
	 * 
	 * @param context  
	 * @param text    显示文字
	 */
	public DialogProgressBar(Context context) {
		this(context, null);
	}
	
	public void show(){
		if(dialog!=null){
			dialog.show();
		}
		
	}
	
	public void dismiss(){
		if(dialog!=null){
			dialog.dismiss();
		}
		
	}
}
