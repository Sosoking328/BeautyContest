package com.sosokan.android.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sosokan.android.R;

/**
 * Created by AnhZin on 8/30/2016.
 */
public class SettingsFragment extends Fragment {

    private View parentView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.activity_home, container, false);

        return parentView;
    }


}
