package com.clt.adapter;

import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clt.commondata.LedTerminateInfo;
import com.clt.ledmanager.R;

public class LedSelectAdapter extends BaseAdapter
{
    protected ArrayList<LedTerminateInfo> ledInfos;

    private Context mContext;

    private String ipAddress;

    public LedSelectAdapter(Context mContext,
            ArrayList<LedTerminateInfo> ledInfos)
    {
        this.mContext = mContext;
        setData(ledInfos);

    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    private void setData(ArrayList<LedTerminateInfo> ledInfos)
    {
        if (ledInfos != null)
        {
            this.ledInfos = ledInfos;
        }
        else
        {
            this.ledInfos = new ArrayList<LedTerminateInfo>();
        }
    }

    public void clearData(){
    	updateView(new ArrayList<LedTerminateInfo>());
    }
    public void updateView(ArrayList<LedTerminateInfo> ledInfos)
    {
        setData(ledInfos);
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return ledInfos.size();
    }

    @Override
    public LedTerminateInfo getItem(int position)
    {
        return ledInfos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public void setChecked(String ipAddress, ListView listView)
    {
        ImageView ivChecked = (ImageView) listView.findViewWithTag(ipAddress);
        if (ivChecked != null)
        {
            ivChecked.setImageResource(R.drawable.right);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.led_select_item, null);
            holder = new Holder();
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_led_name);
            holder.ivSelected = (ImageView) convertView
                    .findViewById(R.id.iv_led_selected);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
        LedTerminateInfo ledInfo = getItem(position);
        holder.tvName.setText(ledInfo.getStrName() + "("
                + ledInfo.getIpAddress() + ")");
        // holder.ivSelected.setTag(ledInfo.getIpAddress());
        if (!TextUtils.isEmpty(ledInfo.getIpAddress())
                && ledInfo.getIpAddress().equals(ipAddress))
        {
            holder.ivSelected.setImageResource(R.drawable.right);
        }
        else
        {
            holder.ivSelected.setImageResource(android.R.color.transparent);
        }

        return convertView;
    }

    class Holder
    {
        private TextView tvName;

        private ImageView ivSelected;

    }
}
