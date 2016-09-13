package com.clt.adapter;

import java.util.ArrayList;

import javax.crypto.spec.IvParameterSpec;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
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
import com.clt.entity.ReceiverPort;
import com.clt.entity.ReceiverPort;
import com.clt.ledmanager.R;

/**
 *	   接收卡网口
 */
public class ReceiverPortAdapter extends BaseAdapter
{
    protected ArrayList<ReceiverPort> ports;

    private Context mContext;

    private int position;

    public ReceiverPortAdapter(Context mContext, ArrayList<ReceiverPort> ports)
    {
        this.mContext = mContext;
        setData(ports);
        position = -1;
    }

    
    private void setData(ArrayList<ReceiverPort> ports)
    {
        if (ports != null)
        {
            this.ports = ports;
        }
        else
        {
            this.ports = new ArrayList<ReceiverPort>();
        }
    }

    public void updateView(ArrayList<ReceiverPort> ports)
    {
        setData(ports);
        notifyDataSetChanged();
    }

    public void setSelected(int position)
    {
        this.position = position;
    }

    @Override
    public int getCount()
    {
        return ports.size();
    }

    @Override
    public ReceiverPort getItem(int position)
    {
        return ports.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.receiver_port_item, null);
            holder = new Holder();
            holder.tvPort = (TextView) convertView
                    .findViewById(R.id.tv_receiver_port);
            holder.tvStartX = (TextView) convertView
                    .findViewById(R.id.tv_receiver_startx);
            holder.tvStartY=(TextView) convertView
                    .findViewById(R.id.tv_receiver_starty);
            holder.tvWidth=(TextView) convertView
                    .findViewById(R.id.tv_receiver_width);
            holder.tvHeight=(TextView) convertView
                    .findViewById(R.id.tv_receiver_height);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
        ReceiverPort receiverPort = getItem(position);
        holder.tvPort.setText(receiverPort.getPort());
        holder.tvStartX.setText(receiverPort.getStartx()+"");
        holder.tvStartY.setText(receiverPort.getStarty()+"");
        holder.tvWidth.setText(receiverPort.getWidth()+"");
        holder.tvHeight.setText(receiverPort.getHeight()+"");
        convertView.setBackgroundResource(R.color.transparent);
        holder.tvPort.setTextColor(Color.BLACK);
        holder.tvStartX.setTextColor(Color.BLACK);
        holder.tvStartY.setTextColor(Color.BLACK);
        holder.tvWidth.setTextColor(Color.BLACK);
        holder.tvHeight.setTextColor(Color.BLACK);
        if(this.position==position){
            convertView.setBackgroundColor(Color.parseColor("#3399FF"));
            holder.tvPort.setTextColor(Color.WHITE);
            holder.tvStartX.setTextColor(Color.WHITE);
            holder.tvStartY.setTextColor(Color.WHITE);
            holder.tvWidth.setTextColor(Color.WHITE);
            holder.tvHeight.setTextColor(Color.WHITE);
        }
        return convertView;
    }

    class Holder
    {
        private TextView tvPort,tvStartX,tvStartY,tvWidth,tvHeight;

    }
}
