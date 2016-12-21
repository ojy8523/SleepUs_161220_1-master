package com.example.seungkyu.dreamtodream;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by junyepoh on 2016. 12. 14..
 */

public class CompanyList_LayoutOne extends Fragment{
    public static CompanyList_LayoutOne newInstance() {
        CompanyList_LayoutOne fragment = new CompanyList_LayoutOne();
        return fragment;
    }

    public CompanyList_LayoutOne() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_company_list_layout_one, null);
        return root;
    }

}

