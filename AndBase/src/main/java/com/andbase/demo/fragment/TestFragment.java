package com.andbase.demo.fragment;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andbase.demo.R;
import com.andbase.demo.global.MyApplication;
import com.andbase.library.app.base.AbBaseFragment;

public class TestFragment extends AbBaseFragment {

    MyApplication application;
    TextView view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = new TextView(this.getActivity());
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        application = (MyApplication) this.getActivity().getApplication();
        view.setText("页面");
        view.setGravity(Gravity.CENTER);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
