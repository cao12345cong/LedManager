package com.clt.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class BasicParameterPagerAdapter extends PagerAdapter
{
    private List<View> mListViews;

    public BasicParameterPagerAdapter(List<View> mListViews)
    {
        this.mListViews = mListViews;
    }

    @Override
    public int getCount()
    {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView(mListViews.get(position));
    }
}
