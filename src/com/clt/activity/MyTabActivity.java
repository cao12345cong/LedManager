package com.clt.activity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.clt.adapter.OnTabActivityResultListener;
import com.clt.adapter.UploadPicListener;
import com.clt.ledmanager.R;
import com.clt.upload.PropertyItem;
import com.clt.upload.UploadProgram;
import com.clt.upload.VsnFileFactory;
/**
 *	导航
 */
public class MyTabActivity extends TabActivity
{
    private static final String TAB1="MainActivity";
    private static final String TAB2="SenderCardActivity";
    private static final String TAB3="ReceiverCardActivity";
    private static final String TAB4="ConnectRelationActivity";
    private RadioGroup group;
    private RadioButton rbMain,rbScreenManager,rbReceiverSetting,rbProgram,rbSetting;
    private TabHost tabHost;
    private Application app;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            app=(Application) getApplication();
            setContentView(R.layout.tab_main);
            initView();
            initListener();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
//        Application.getInstance().setSystemLanguage();
       
    }
    private void initView()
    {
        group=(RadioGroup) findViewById(R.id.rg_bottom_menu);
        tabHost=getTabHost();
        this.tabHost.addTab(this.tabHost.newTabSpec(TAB1)
                .setIndicator(TAB1).setContent(new Intent(this, MainActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec(TAB2)
                .setIndicator(TAB2).setContent(new Intent(this, SenderCardActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec(TAB3)
                .setIndicator(TAB3).setContent(new Intent(this, ReceiverCardActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec(TAB4)
                .setIndicator(TAB4).setContent(new Intent(this, ConnectRelationActivity.class)));
        this.tabHost.setCurrentTab(0);
    
    }
    private void initListener()
    {
        group.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.rb_index:
                        tabHost.setCurrentTabByTag(TAB1);
                        break;
                    case R.id.rb_sender_card:
                        tabHost.setCurrentTabByTag(TAB2);
                        break;
                    case R.id.rb_receiver_card:
                        tabHost.setCurrentTabByTag(TAB3);
                        break;
                    case R.id.rb_connect_relation:
                        tabHost.setCurrentTabByTag(TAB4);
                        break;
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Activity subActivity=getLocalActivityManager().getCurrentActivity();
        if(subActivity instanceof OnTabActivityResultListener){
            OnTabActivityResultListener listener=(OnTabActivityResultListener) subActivity;
            listener.onTabActivityResult(requestCode, resultCode, data); 
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Application app=(Application) getApplication();
        app.quit();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        String type=intent.getStringExtra("Type");
        if(type==null){
            
            return;
        }
        //切换语言
        if(type.equals("changeLanguage")){
           
            setContentView(R.layout.tab_main);
            app.setNeedChangeLanguage(true);
            tabHost.clearAllTabs();
            tabHost.clearFocus();
            initView();
            initListener();
        }
       
        //上传图片
        if(type.equals("uploadProgram")){
            Activity subActivity=getLocalActivityManager().getCurrentActivity();
            if(subActivity instanceof UploadPicListener){
                UploadPicListener listener=(UploadPicListener) subActivity;
                UploadProgram uploadProgram=(UploadProgram) intent.getSerializableExtra("ProgramInfo");
                listener.onUploadPic(uploadProgram); 
            }
        }
        
        
    }
    /**
     * 按钮事件
     */
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            return true;
        }
        return false;
    }
}
