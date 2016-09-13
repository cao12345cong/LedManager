package com.clt.ui;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.clt.ledmanager.R;

public class CustomerSpinner2 extends Button implements OnItemClickListener
{

    public static SelectDialog dialog = null;
    
    private ArrayList<Integer> list;// ArrayList<String> list存储所要显示的数据

    private Context context;

    public OnItemSelectedListener listener;
    
    private int oldPosition;
    
    public CustomerSpinner2(Context context, AttributeSet attrs)
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
        final View view = inflater.inflate(R.layout.custom_spinner_2, null);
        //listview
      //设置adapter
        final GridView gridview = (GridView) view
                .findViewById(R.id.formcustom_spinner_gridview);
        MyAdapter adapters = new MyAdapter(list);
        gridview.setAdapter(adapters);
        gridview.setOnItemClickListener(this);
        
        dialog = new SelectDialog(context, R.style.dialog);// 创建Dialog并设置样式主题
        LayoutParams params = new LayoutParams(650, LayoutParams.FILL_PARENT);
        dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
        dialog.show();
        dialog.addContentView(view, params);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> view, View itemView, int position,
            long id)
    {
        //setSelection(position);
        //setText(list.get(position));
        setImage(position);
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
        listener.onItemSelected(null, null, position, id);
    }



    class SelectDialog extends AlertDialog
    {

        public SelectDialog(Context context, int theme)
        {
            super(context, theme);
        }

        public SelectDialog(Context context)
        {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            // setContentView(R.layout.slt_cnt_type);
        }
    }
   
    public void setTitle(String text){
        setText(text);
        setCompoundDrawables(null,null,null,null);  
        
    }
    public void setImage(int index){
        setText("");
        Drawable drawable= getResources().getDrawable(list.get(index));  
        /// 这一步必须要做,否则不会显示.  
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
        setCompoundDrawables(drawable,null,null,null);  
        
    }
   
    public void initView(ArrayList<Integer> list){
        
        this.list=list;
        
    }
    public void setSelection(int index, boolean anim){
        setImage(list.get(index));
        
    }
    
    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        this.listener=listener;
    }
    
    private class MyAdapter extends BaseAdapter{
        private ArrayList<Integer> resImages;
        public MyAdapter(ArrayList<Integer> resImages)
        {
            this.resImages=resImages;
        }
        @Override
        public int getCount()
        {
            return resImages.size();
        }

        @Override
        public Integer getItem(int position)
        {
            return resImages.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder = null;
            if (convertView == null)
            {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(getContext());
                convertView = mInflater
                        .inflate(R.layout.custom_spinner_item_2, null);
                viewHolder.ivType = (ImageView) convertView
                        .findViewById(R.id.iv_item);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.ivType.setImageResource(resImages.get(position));
            return convertView;
        }
        
    }
    static class ViewHolder
    {
        ImageView ivType;
    }
}
