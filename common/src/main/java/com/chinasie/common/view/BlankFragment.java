package com.chinasie.common.view;

/**
 * Created by 古永财 on 2016-09-24.
 */
import android.annotation.SuppressLint;

@SuppressLint("ValidFragment")
public class BlankFragment extends BasePageFragment {


    public BlankFragment(String _title)
    {
        // Required empty public constructor
        super(_title);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void submitEvent() {

    }

    @Override
    public void resetEvent() {

    }

    @Override
    public void SelectPageEvent() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    public BasePageActivity GetParentActivity() {
        return (BasePageActivity)pageActivity;
    }
}