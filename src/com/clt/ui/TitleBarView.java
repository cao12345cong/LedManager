package com.clt.ui;

import java.lang.ref.Reference;

import com.clt.ledmanager.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 标题栏view
 * 
 * @author caocong 2013.11.07
 * 
 *         使用说明：
 *         xml加上xmlns:itbook="http://schemas.android.com/apk/res/com.xtx.itbook.ui"
 *         <com.xtx.itbook.ui.TitleBarView android:id="@+id/titlebar"
 *         android:layout_width="fill_parent" android:layout_height="48dp"
 *         itbook:left_drawble="@drawable/ic_goback_selector"
 *         itbook:right_drawble="@drawable/ic_goback_selector"
 *         itbook:title_name="R.string.faq" 标题名 />
 */

public class TitleBarView extends LinearLayout
{
	private Context context;
	
	private TitleBarListener listener;
	
	private LinearLayout llLeft, llRight;
	
	private ImageView ivLeft, ivRight;
	
	private TextView tvTitle, tvLeft, tvRight;
	
	boolean isShowSearch = false;
	
	public TitleBarView(Context context)
	{
		super(context);
		this.context = context;
	}
	
	public TitleBarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		try
		{
			
			this.context = context;
			initView();
			initAttr(attrs);
		}
		catch (Exception e)
		{
		}
		
	}
	
	private void initView()
	{
		try
		{
			View root = LayoutInflater.from(context).inflate(R.layout.titlebar,
					null);
			addView(root);
			// 初始化view
			llLeft = (LinearLayout) root.findViewById(R.id.ll_left);
			ivLeft = (ImageView) root.findViewById(R.id.iv_left);
			tvLeft = (TextView) root.findViewById(R.id.tv_left);
			tvRight = (TextView) root.findViewById(R.id.tv_right);
			llRight = (LinearLayout) root.findViewById(R.id.ll_right);
			ivRight = (ImageView) root.findViewById(R.id.iv_right);
			tvTitle = (TextView) root.findViewById(R.id.tv_titlebar_title);
		}
		catch (Exception e)
		{
		}
		
	}
	
	private void initAttr(AttributeSet attrs)
	{
		try
		{
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.TitleBarView);
			int leftDrawalbe = a.getResourceId(
					R.styleable.TitleBarView_left_drawble, -1);
			int rightDrawalbe = a.getResourceId(
					R.styleable.TitleBarView_right_drawble, -1);
			String titleName = a.getString(R.styleable.TitleBarView_title_name);
			String leftText = a.getString(R.styleable.TitleBarView_left_text);
			String rightText = a.getString(R.styleable.TitleBarView_right_text);
			
			if (leftDrawalbe != -1)
			{
				ivLeft.setImageResource(leftDrawalbe);
			}
			if (rightDrawalbe != -1)
			{
				ivRight.setImageResource(rightDrawalbe);
			}
			
			if (titleName != null)
			{
				tvTitle.setText(titleName);
			}
			if (!TextUtils.isEmpty(leftText))
			{
				tvLeft.setText(leftText);
			}
			if (!TextUtils.isEmpty(rightText))
			{
				tvRight.setText(rightText);
			}
			
		}
		catch (Exception e)
		{
		}
		
		llLeft.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					listener.onClickLeftImg(v);
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}
				
			}
		});
		
		llRight.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				try
				{
					listener.onClickRightImg(v);
				}
				catch (Exception e)
				{
					// TODO: handle exception
				}
				
			}
		});
	}
	
	public void setTitle(String title)
	{
		try
		{
			if (title == null)
			{
				tvTitle.setText("");
			}
			tvTitle.setText(title);
		}
		catch (Exception e)
		{
		}
		
	}
	
	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setTitleBarListener(TitleBarListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * 向外的接口
	 * 
	 * @author caocong
	 * 
	 */
	public interface TitleBarListener
	{
		// 左按钮点击
		void onClickLeftImg(View v);
		
		// 右按钮点击
		void onClickRightImg(View v);
		
	}
	
}
