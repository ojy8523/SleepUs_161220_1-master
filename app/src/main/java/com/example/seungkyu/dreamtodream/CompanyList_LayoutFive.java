package com.example.seungkyu.dreamtodream;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by junyepoh on 2016. 12. 14..
 */

public class CompanyList_LayoutFive extends Fragment{
    public static CompanyList_LayoutFive newInstance() {
        CompanyList_LayoutFive fragment = new CompanyList_LayoutFive();
        return fragment;
    }

    public CompanyList_LayoutFive() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_company_list_layout_five, null);
        return root;
    }

}
