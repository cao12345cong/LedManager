package com.clt.ui;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.clt.adapter.SpinnerAdapter;
import com.clt.ledmanager.R;

public class CustomerSpinnerColor extends Button implements OnItemClickListener,OnItemLongClickListener
{

    public static SelectDialog dialog = null;
    
    private ArrayList<String> list;// ArrayList<String> list存储所要显示的数据

    private Context context;

    public OnItemLongClickListener onItemLongClickListener;
    
    public OnItemClickListener onItemClickListener;
    
    private int oldPosition;
    
    public CustomerSpinnerColor(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context=context;
    }

    // 如果视图定义了OnClickListener监听器，调用此方法来执行
    @Override
    public boolean performClick()
    {
        if(list==null||list.size()==0){
            
            return false;
        }
        Context context = getContext();
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        final View view = inflater.inflate(R.layout.custom_spinner, null);
        //listview
        final ListView listview = (ListView) view
                .findViewById(R.id.formcustomspinner_list);
        SpinnerAdapter adapters = new SpinnerAdapter(context, getList());
        listview.setAdapter(adapters);
        listview.setOnItemLongClickListener(this);
        listview.setOnItemClickListener(this);
        dialog = new SelectDialog(context, R.style.dialog);// 创建Dialog并设置样式主题
        LayoutParams params = new LayoutParams(440, LayoutParams.FILL_PARENT);
        dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        dialog.show();
        dialog.addContentView(view, params);
        return true;
    }


    public ArrayList<String> getList()
    {
        return list;
    }

    public void setList(ArrayList<String> list)
    {
        this.list = list;
    }


    class SelectDialog extends Dialog
    {

        public SelectDialog(Context context, int theme)
        {
            super(context, theme);
        }

        public SelectDialog(Context context)
        {
            super(context);
        }

    }
    /**
     * 初始化数据
     * @param resArrId 数组资源id
     */
    public void initView(int resArrId){
        String [] testModes = getResources().getStringArray(resArrId);
        ArrayList<String> testModesList = new ArrayList<String>(
                Arrays.asList(testModes));
        setList(testModesList);
        
        setText(list.get(0));
        
    }
    
    public void initView(String[] arr){
        ArrayList<String> testModesList = new ArrayList<String>(
                Arrays.asList(arr));
        setList(testModesList);
        
        setText(list.get(0));
        
    }
    
    
    public void setSelection(int index, boolean anim){
        setText(list.get(index));
        
    }
    

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        
        this.onItemLongClickListener=listener;
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        
        this.onItemClickListener=listener;
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id)
    {
    	try
		{
    		//setSelection(position);
            //setText(list.get(position));
            if(onItemLongClickListener==null){
                
                return false;
            }
            if (dialog != null)
            {
                dialog.dismiss();
                dialog = null;
            }
            onItemLongClickListener.onItemLongClick(null, view, position, id);
            return true;
		}
		catch (Exception e)
		{
			return false;
		}
        
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id)
    {
    	try
		{
    		setText(list.get(position));
            if (dialog != null)
            {
                dialog.dismiss();
                dialog = null;
            }
            if(onItemClickListener==null){
                return;
            }
            onItemClickListener.onItemClick(null, null, position, id);
		}
		catch (Exception e)
		{
		}
    	
    }
    
}
