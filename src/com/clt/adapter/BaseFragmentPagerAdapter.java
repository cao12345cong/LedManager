package com.clt.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter
{
    private ArrayList<Fragment> fragmentsList;

    public BaseFragmentPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public BaseFragmentPagerAdapter(FragmentManager fragmentManager,
            ArrayList<Fragment> fragments)
    {
        super(fragmentManager);
        this.fragmentsList = fragments;
    }

    @Override
    public Fragment getItem(int arg0)
    {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getCount()
    {
        return fragmentsList.size();
    }

    public int getItemPosition(Object object)
    {
        return super.getItemPosition(object);
    }
}
