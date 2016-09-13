package com.clt.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clt.IService;
import com.clt.entity.ReceiverSettingInfo;
import com.clt.ledmanager.R;
import com.clt.netmessage.NMGetReceiverCardInfoAnswer;
import com.clt.netmessage.NMSetReceiverCardInfoSaveToReceiverAnswer;
import com.clt.netmessage.NMSetReceiverCardInfoSendAnswer;
import com.clt.netmessage.NetMessageType;
import com.clt.service.BaseService.LocalBinder;
import com.clt.service.BaseServiceFactory;
import com.clt.ui.CustomerSpinner;
import com.clt.util.DialogUtil;
import com.google.gson.Gson;

/**
 * 接收卡页面
 * @author caocong 2014.6.9
 */
public class ReceiverCardActivity extends BaseActivity

{
    private LinearLayout llLayout;// 根容器

    private CustomerSpinner spinnerBoxParams;// 箱体参数

    private Button btnSend, btnSolid;

    private TextView tvRefresh, tvRefreshRate, tvGrayLevel, tvGrayMode,
            tvFrequency, tvBlankingValue, tvGamma, tvBriLevle, tvBrightPercent,
            tvLeaseOE;

    private IService mangerNetService;// 通信服务

    private ArrayList<ReceiverSettingInfo> receiverSettingInfos;// 接收设置信息

    private int index = 0;//

    private Dialog waittingDialog;// 等待对话框

    private EditText etBoxWidth, etBoxHeight;// 箱体宽高

    private int boxWidth = 128, boxHeight = 128;// 箱体宽高默认值

    /**
     * 绑定通信service
     */
    private ServiceConnection mangerNetServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceDisconnected(ComponentName name)
        {

            mangerNetService = null;

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            // TODO Auto-generated method stub
            mangerNetService = ((LocalBinder) service)
                    .getService();
            if (mangerNetService != null)
            {
                mangerNetService.setNmHandler(nmHandler);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver_card);
        Application.getInstance().setSystemLanguage();
        init();
        initView();
        initListener();

    }

    /**
     * 初始化
     */
    private void init()
    {
        // 绑定mangerNetService
        Intent _intent1 = new Intent(ReceiverCardActivity.this,
                BaseServiceFactory.getBaseService());
        this.getApplicationContext().bindService(_intent1,
                mangerNetServiceConnection, Context.BIND_AUTO_CREATE);
        waittingDialog = DialogUtil.createDownloadDialog(this);
    }

    /**
     * 初始化视图
     */
    private void initView()
    {
        // 根容器
        llLayout = (LinearLayout) findViewById(R.id.ll_layout);
        // 下拉菜单
        spinnerBoxParams = (CustomerSpinner) findViewById(R.id.spinner_box_param);
        spinnerBoxParams.setText(getResString(R.string.none));
        // 两个按钮
        btnSend = (Button) findViewById(R.id.btn_send);
        btnSolid = (Button) findViewById(R.id.btn_solid);
        // Textview
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);
        tvRefreshRate = (TextView) findViewById(R.id.tv_refresh_rate);
        tvGrayLevel = (TextView) findViewById(R.id.tv_gray_level);
        tvGrayMode = (TextView) findViewById(R.id.tv_gray_mode);
        tvFrequency = (TextView) findViewById(R.id.tv_frequency);
        tvBlankingValue = (TextView) findViewById(R.id.tv_blanking_value);
        tvGamma = (TextView) findViewById(R.id.tv_gamma_value);
        tvBriLevle = (TextView) findViewById(R.id.tv_bri_level);
        tvBrightPercent = (TextView) findViewById(R.id.tv_bright_percent);
        tvLeaseOE = (TextView) findViewById(R.id.tv_lease_oe);
        // 箱体宽高
        etBoxWidth = (EditText) findViewById(R.id.et_box_width);
        etBoxHeight = (EditText) findViewById(R.id.et_box_height);
        etBoxWidth.setText(boxWidth + "");
        etBoxHeight.setText(boxHeight + "");

    }

    /**
     * 初始化监听器
     */
    private void initListener()
    {
        // 让EditText失去焦点
        llLayout.setOnTouchListener(new OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                llLayout.requestFocus();
                return false;
            }
        });
        // 选择bin文件
        spinnerBoxParams.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id)
            {
                ReceiverSettingInfo receiverSettingInfo = receiverSettingInfos
                        .get(position);
                updateTextViews(receiverSettingInfo);
                index = position;
            }

        });
        // 发送
        btnSend.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                boolean isOk = doConditional();
                if (!isOk)
                {
                    return;
                }
                if (receiverSettingInfos == null
                        || receiverSettingInfos.isEmpty())
                {
                    return;
                }
                if (waittingDialog.isShowing())
                {
                    waittingDialog.dismiss();
                }
                if (mangerNetService != null)
                {
                    boxWidth = Integer
                            .parseInt(etBoxWidth.getText().toString());
                    boxHeight = Integer.parseInt(etBoxHeight.getText()
                            .toString());
                    ReceiverSettingInfo receiverSettingInfo = receiverSettingInfos
                            .get(index);
                    receiverSettingInfo.setWidth(boxWidth);
                    receiverSettingInfo.setHeight(boxHeight);
                    mangerNetService
                            .setReceiverCardInfoSend(receiverSettingInfo);
                }

            }
        });
        // 固化
        btnSolid.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                boolean isOk = doConditional();
                if (!isOk)
                {
                    return;
                }
                if (receiverSettingInfos == null
                        || receiverSettingInfos.isEmpty())
                {

                    return;
                }
                if (waittingDialog.isShowing())
                {
                    waittingDialog.dismiss();
                }
                if (mangerNetService != null)
                {
                    boxWidth = Integer
                            .parseInt(etBoxWidth.getText().toString());
                    boxHeight = Integer.parseInt(etBoxHeight.getText()
                            .toString());
                    ReceiverSettingInfo receiverSettingInfo = receiverSettingInfos
                            .get(index);
                    receiverSettingInfo.setWidth(boxWidth);
                    receiverSettingInfo.setHeight(boxHeight);
                    mangerNetService
                            .setReceiverCardInfoSaveToReceiver(receiverSettingInfos
                                    .get(index));
                }

            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (mangerNetService != null)
        {
            mangerNetService.setNmHandler(nmHandler);
            mangerNetService.getReceiverCardInfo();
        }
    }

    /**
     * 操作之前的条件判断
     */
    public boolean doConditional()
    {
        // 宽高判断
        int w = 0, h = 0;
        try
        {
            w = Integer.parseInt(etBoxWidth.getText().toString().trim());
        }
        catch (NumberFormatException e)
        {
            toast(getResString(R.string.please_input_pos_int), 1000);
            etBoxWidth.requestFocus();
            return false;
        }
        try
        {
            h = Integer.parseInt(etBoxHeight.getText().toString().trim());
        }
        catch (NumberFormatException e)
        {
            toast(getResString(R.string.please_input_pos_int), 1000);
            etBoxHeight.requestFocus();
            return false;
        }

        if (w < 0)
        {
            // toast("宽小于等于150", 1000);
            toast(getResString(R.string.please_input_pos_int), 1000);
            etBoxWidth.requestFocus();
            return false;
        }
        if (h < 0)
        {
            // toast("高小于等于150", 1000);
            toast(getResString(R.string.please_input_pos_int), 1000);
            etBoxHeight.requestFocus();
            return false;
        }

        return true;

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        this.getApplicationContext().unbindService(mangerNetServiceConnection);
    }

    /**
     * 处理message
     * @param msg
     */
    @Override
    public void dealHandlerMessage(android.os.Message netMessage)
    {
        switch (netMessage.what)
        {
            case NetMessageType.getReceiveCardInfo:// 获取信息
                break;
            case NetMessageType.getReceiveCardInfoAnswer:// 获取信息结果
            {
                String message = (String) netMessage.obj;
                Gson gson = new Gson();
                NMGetReceiverCardInfoAnswer answer = gson.fromJson(message,
                        NMGetReceiverCardInfoAnswer.class);
                receiverSettingInfos = answer.getReceiverSettings();
                if (receiverSettingInfos != null
                        && !receiverSettingInfos.isEmpty())
                {
                    updateView();
                    findViewById(R.id.tr_box_w_h).setVisibility(View.VISIBLE);
                }
            }
                break;
            case NetMessageType.setReceiveCardSettingInfoSendAnswer:// 发送回应
            {
                if (waittingDialog.isShowing())
                {
                    waittingDialog.dismiss();
                }
                String strMessage = (String) netMessage.obj;
                Gson gson = new Gson();
                NMSetReceiverCardInfoSendAnswer nm = gson.fromJson(strMessage,
                        NMSetReceiverCardInfoSendAnswer.class);
                if (nm.getErrorCode() == 0)
                {
                    Toast.makeText(ReceiverCardActivity.this,
                            getResString(R.string.setting_fail), 500).show();
                }
                else
                {
                    Toast.makeText(ReceiverCardActivity.this,
                            getResString(R.string.setting_success), 500).show();
                }

            }
                break;
            case NetMessageType.setReceiveCardSettingInfoSaveToReceiverAnswer:// 固化回应
            {
                if (waittingDialog.isShowing())
                {
                    waittingDialog.dismiss();
                }
                String strMessage = (String) netMessage.obj;
                Gson gson = new Gson();
                NMSetReceiverCardInfoSaveToReceiverAnswer nm = gson.fromJson(
                        strMessage,
                        NMSetReceiverCardInfoSaveToReceiverAnswer.class);
                if (nm.getErrorCode() == 0)
                {
                    Toast.makeText(ReceiverCardActivity.this,
                            getResString(R.string.setting_fail), 500).show();
                }
                else
                {
                    Toast.makeText(ReceiverCardActivity.this,
                            getResString(R.string.setting_success), 500).show();
                }
            }
                break;
        }
    }

    /**
     * 更新UI
     */
    public void updateView()
    {
        try
        {
            if (receiverSettingInfos == null || receiverSettingInfos.isEmpty())
            {
                return;
            }
            int size = receiverSettingInfos.size();
            String [] names = new String [size];
            for (int i = 0; i < size; i++)
            {
                String fileName = receiverSettingInfos.get(i).getFileName();
                String name = fileName.substring(0, fileName.lastIndexOf("."));
                names[i] = name;
            }
            spinnerBoxParams.initView(names);
            //spinnerBoxParams.setSelection(0, true);
            spinnerBoxParams.setText(getResString(R.string.please_select));
            ReceiverSettingInfo receiverSettingInfo = receiverSettingInfos
                    .get(0);
            // Text
            // tvRefresh.setText(receiverSettingInfo.getGrayLevel());
            updateTextViews(receiverSettingInfo);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 更新Textview
     * @param receiverSettingInfo
     */
    public void updateTextViews(ReceiverSettingInfo receiverSettingInfo)
    {
        // tvRefreshRate.setText(receiverSettingInfo.getMuliple()+"");
        // tvGrayLevel.setText(receiverSettingInfo.getGrayLevel()+"");
        // tvGrayMode.setText(receiverSettingInfo.getGrayMode()+"");
        // //tvFrequency.setText(receiverSettingInfo.get);
        // tvBlankingValue.setText(receiverSettingInfo.getBlankingValue()+"");
        // tvGamma.setText(receiverSettingInfo.getGammaValue()+"");
        // tvBriLevle.setText(receiverSettingInfo.getBrightnessLevel()+"");
        // tvBrightPercent.setText(receiverSettingInfo.getBrightnessPercent()+"");
        // tvLeaseOE.setText(receiverSettingInfo.getMinimumOE()+"");
    }

}
