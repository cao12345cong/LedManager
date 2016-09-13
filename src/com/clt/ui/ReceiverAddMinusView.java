package com.clt.ui;

import com.clt.ledmanager.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
/**
 * 接收卡参数设置，行列数加减1
 * @author Administrator
 *
 */
public class ReceiverAddMinusView extends LinearLayout
{
    private Context context;
    private EditText etNum;
    private Button btnAdd,btnMinus;
    private boolean canAddOne=true;//能否加一
    private OnNumChangedListener listener;
    public ReceiverAddMinusView(Context context)
    {
        super(context);
        init();
    }
    
    public ReceiverAddMinusView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }
    
    
    public boolean isCanAddOne()
    {
        return canAddOne;
    }

    public void setCanAddOne(boolean canAddOne)
    {
        this.canAddOne = canAddOne;
    }

    private void init()
    {
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.receive_num_view, null);
        view.setLayoutParams(params);
        etNum=(EditText) view.findViewById(R.id.et_receiver);
        btnAdd=(Button) view.findViewById(R.id.btn_add_one);
        btnMinus=(Button) view.findViewById(R.id.btn_minus_one);
        
        initListener();
        this.addView(view);
        
    }

    private void initListener()
    {
        //加一
        btnAdd.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if(canAddOne){
                    int num=Integer.valueOf(etNum.getText().toString());
                    etNum.setText(String.valueOf(++num));
                    if(listener!=null){
                        listener.onchanged(num);
                    }
                }
            }
        });
        //减一
        btnMinus.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                int num=Integer.valueOf(etNum.getText().toString());
                if(num>1){
                    etNum.setText(String.valueOf(--num));
                    if(listener!=null){
                        listener.onchanged(num);
                    }
                }
            }
        });
    }
   
    public void setOnNumChangedListener(OnNumChangedListener listener){
        
        this.listener=listener;
    }
    /**
     * 设置数据
     * @param num
     */
    public void setNum(int num){
        etNum.setText(num);
    }
    
    public interface OnNumChangedListener{
        void onchanged(int num);
    }

}


