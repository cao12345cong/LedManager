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
import com.clt.ledmanager.R;
import com.clt.util.Tools;

public class ProgramListAdapter extends BaseAdapter
{
    protected ArrayList<Program> programs;

    private Context mContext;

    public ProgramListAdapter(Context mContext,
            ArrayList<Program> programs)
    {
        this.mContext = mContext;
        setData(programs);

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

//    public void setChecked(String ipAddress, ListView listView)
//    {
//        ImageView ivChecked = (ImageView) listView.findViewWithTag(ipAddress);
//        if (ivChecked != null)
//        {
//            ivChecked.setImageResource(R.drawable.right);
//        }
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = null;
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.program_info_item, null);
            holder = new Holder();
            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_program_name);
            holder.tvSize=(TextView) convertView
                    .findViewById(R.id.tv_program_size);
            holder.tvCreateTime=(TextView) convertView
                    .findViewById(R.id.tv_program_createtime);
            holder.tvPath=(TextView) convertView
                    .findViewById(R.id.tv_program_path);
            holder.tvPathType=(TextView) convertView
                    .findViewById(R.id.tv_program_path_type);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }
        Program program = getItem(position);
        //文件名
        String fileName=program.getFileName().substring(0, program.getFileName().lastIndexOf("."));
        holder.tvName.setText(fileName);
        
        
        //文件大小
        String sizeStr=mContext.getString(R.string.program_size);
        holder.tvSize.setText(sizeStr+":"+Tools.byte2KbOrMb(program.getSize()));
        //创建时间
        String createTimeStr=mContext.getString(R.string.program_create_time);
        holder.tvCreateTime.setText(createTimeStr+":"+program.getCreateTime());
        //文件路径
        String fullpathStr=mContext.getString(R.string.program_full_path);
        holder.tvPath.setText(fullpathStr+":"+program.getPath());
        
        int type=program.getPathType();
        String pathStr=mContext.getString(R.string.program_path);
        if(type==Program.SDCARD){
            holder.tvPathType.setText(pathStr+":"+mContext.getString(R.string.from_sd_card)); 
        }else if(type==Program.UDISK){
            holder.tvPathType.setText(pathStr+":"+mContext.getString(R.string.from_usb_disk));
        }
        return convertView;
    }

    class Holder
    {
        private TextView tvName,tvSize,tvCreateTime,tvPath,tvPathType;

        private ImageView ivSelected;

    }
}
