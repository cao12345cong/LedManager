package com.clt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 分时段亮度调节
 * @author Administrator
 *
 */
public class DividTimeSetBrightAdapter extends BaseAdapter
{
    private Context context;

    private int count = 1;

    public DividTimeSetBrightAdapter(Context context)
    {
        this.context = context;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public void updateView()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }

}
