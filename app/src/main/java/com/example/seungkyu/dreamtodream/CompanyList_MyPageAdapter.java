package com.example.seungkyu.dreamtodream;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by junyepoh on 2016. 12. 14..
 */

public class CompanyList_MyPageAdapter extends FragmentPagerAdapter{

    private final int NUM_ITEMS = 5;

    public CompanyList_MyPageAdapter(FragmentManager fm){super((fm));}

    public int getCount(){return NUM_ITEMS;}

    public android.support.v4.app.Fragment getItem(int position) {
        if (position == 0)
            return CompanyList_LayoutOne.newInstance();
        else if (position == 1)
            return CompanyList_LayoutTwo.newInstance();
        else if (position == 2)
            return CompanyList_LayoutThree.newInstance();
        else if (position == 3)
            return CompanyList_LayoutFour.newInstance();
        else if (position == 4)
            return CompanyList_LayoutFive.newInstance();
        return null;
    }
}
