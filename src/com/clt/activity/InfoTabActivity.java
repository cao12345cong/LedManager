package com.clt.activity;

import com.clt.ledmanager.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class InfoTabActivity extends TabActivity
{
    private static final String TAB1="MainActivity";
    private static final String TAB2="SenderCardActivity";
    private RadioButton rbMain,rbScreenManager,rbReceiverSetting,rbProgram,rbSetting;
    private TabHost tabHost;
    private Application app;
    private RadioGroup group;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        try
        {
            app=(Application) getApplication();
            setContentView(R.layout.tab_info);
            initView();
            initListener();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
       
    }
    private void initView()
    {
        group=(RadioGroup) findViewById(R.id.rg_bottom_menu);
        tabHost=getTabHost();
        this.tabHost.addTab(this.tabHost.newTabSpec(TAB1)
                .setIndicator(TAB1).setContent(new Intent(this, ProgramInfoActivity.class)));
        this.tabHost.addTab(this.tabHost.newTabSpec(TAB2)
                .setIndicator(TAB2).setContent(new Intent(this, InfoActivity.class)));
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
                }
            }
        });
    }
    
    public void goBack(View v){
    	finish();
    }
}
