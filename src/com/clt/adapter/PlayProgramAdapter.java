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
import com.clt.entity.Program;
import com.clt.entity.Program;
import com.clt.ledmanager.R;

/**
 *	节目控制播放适配器 
 *
 */
public class PlayProgramAdapter extends BaseAdapter
{
    protected ArrayList<Program> programs;

    private Context mContext;

    private int position;

    public PlayProgramAdapter(Context mContext, ArrayList<Program> programs)
    {
        this.mContext = mContext;
        setData(programs);
        position = -1;
    }

    private void setData(ArrayList<Program> programs)
    {
        if (programs != null)
        {
            this.programs = programs;
        }
        else
        {
            this.programs = new ArrayList<Program>();
        }
    }

    public void updateView(ArrayList<Program> programs)
    {
        setData(programs);
        notifyDataSetChanged();
    }

    public void setSelected(int position)
    {
        this.position = position;
    }

    @Override
    public int getCount()
    {
        return programs.size();
    }

    @Override
    public Program getItem(int position)
    {
        return programs.get(position);
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
            convertView = inflater.inflate(R.layout.play_program_item, null);
            holder = new Holder();
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_program_name);
            holder.tvResource = (TextView) convertView
                    .findViewById(R.id.tv_program_resource);
            holder.ivSelected = (ImageView) convertView
                    .findViewById(R.id.iv_play_program);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
        Program program = getItem(position);
        String fileName=program.getFileName();
        String name=fileName.substring(0, fileName.lastIndexOf(".vsn"));
        
        holder.tvName.setText(name);
        String res=null;
        if(program.getPathType()==Program.UDISK){
            res=mContext.getResources().getString(R.string.from_usb_disk);
        }else if(program.getPathType()==Program.SDCARD){
            res=mContext.getResources().getString(R.string.from_sd_card);
        }else{
            res=mContext.getResources().getString(R.string.from_internal_storage);
        }
        holder.tvResource.setText("("+res+")");
        if (this.position == position)
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
        private TextView tvResource;//节目的位置，U盘还是SD卡
        private ImageView ivSelected;

    }
}
