package com.clt.adapter;

import java.util.ArrayList;

import com.clt.commondata.LedTerminateInfo;
import com.clt.ledmanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class LedListViewAdapter extends BaseAdapter
{
    protected ArrayList<LedTerminateInfo> ledInfo;

    private LayoutInflater mInflater;

    private Context mContext;

    public LedListViewAdapter(Context mContext,
            ArrayList<LedTerminateInfo> ledInfo)
    {
        super();
        this.mContext = mContext;
        this.ledInfo = ledInfo;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        if (ledInfo == null)
            return 0;

        return ledInfo.size();
    }

    @Override
    public Object getItem(int arg0)
    {
        if (ledInfo == null)
            return 0;

        return ledInfo.get(arg0);
    }

    @Override
    public long getItemId(int arg0)
    {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater
                    .inflate(R.layout.led_item, null);
            viewHolder.name = (RadioButton) convertView
                    .findViewById(R.id.rbItemOfLedNameList);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String strName = ledInfo.get(position).getStrName();
        strName += "(";
        strName += ledInfo.get(position).getIpAddress();
        strName += ")";

        viewHolder.name.setText(strName);
        if (position == 0)
            viewHolder.name.setChecked(true);

        return convertView;
    }

    static class ViewHolder
    {
        RadioButton name;
    }

}
