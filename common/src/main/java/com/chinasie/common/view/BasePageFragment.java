package com.chinasie.common.view;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinasie.common.R;
import com.chinasie.common.config.ResultKind;
import com.chinasie.common.tools.PlatformContextHelper;
import com.chinasie.common.utility.IPageParentActivity;
import com.chinasie.common.utility.TextViewMessage;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by 古永财 on 2016-09-24.
 */

public abstract class BasePageFragment extends Fragment implements IPageParentActivity {


    public TextViewMessage textViewMessage;
    protected HashMap<String,Object> haspMapData;
    protected Gson gson = new Gson();
    protected boolean inited = false;//---是否初始化----
    protected BasePageActivity pageActivity;
    protected String title;//---标题---
    public String getTitle() {
        return title;
    }

    private TextView tvMsg;//---消息框----
    public View ViewFragment;//---界面----

    public BasePageFragment()
    {

    }

    public  BasePageFragment(String _title)
    {
        this();
        title = _title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            View view = inflater.inflate(com.chinasie.common.R.layout.frgament_base, container, false);
            tvMsg = (TextView) view.findViewById(com.chinasie.common.R.id.tvMsg);
            tvMsg.setVisibility(View.GONE);//---隐藏--
            textViewMessage = new TextViewMessage(this.getActivity(), tvMsg);
            pageActivity = (BasePageActivity) this.getActivity();
            haspMapData = ((BasePageActivity) this.getActivity()).HaspMapData;
            PlatformContextHelper.getInstance().InitPlatformContextHelper(pageActivity); //---初始化本地信息---

            ViewFragment = view;
            initView();//---初始化界面---
            if(!inited) {
                initData();//---初始化数据---
                inited = true;
            }
            initTitle();
            pageActivity.RefreshPagerAdapter();//---刷新分页适配器----

            return view;
    }

    /**
     * 消息异步设置焦点
     */
    private Handler mHandlerFoucus = new Handler()
    {
        public void handleMessage(Message msg) {
            View view = ViewFragment.findViewById(msg.what);
            if (view != null) {
                view.requestFocus();
                if (view instanceof EditText) {
                    EditText editView = (EditText) view;
                    editView.selectAll();
                }
            }
        }
    };

    /**
     * 设置焦点
     * @param v 界面
     */
    protected void setFocus(View v)
    {
        Message msg = new Message();
        msg.what = v.getId();
        mHandlerFoucus.sendMessage(msg);
    }


    /**
     * 显示页面
     * @param index
     */
    protected void showPage(int index)
    {
        ViewPager mViewPager = (ViewPager)this.getActivity().findViewById(R.id.container);
        if(index <= mViewPager.getChildCount() && index >= 0)
            mViewPager.setCurrentItem(index);
    }

    /**
     * 获取分页
     * @param index
     * @return
     */
    protected Fragment getPageByIndex(int index) throws Exception {
        if(index < pageActivity.pages.length && index >=0)
            return pageActivity.pages[index];
        else {
            throw new Exception("获取页面越界!");
        }
    }

    /**
     * 触发Fragment KEY事件
     * @param keyCode 按键
     * @param event 事件
     * @return
     */
    public boolean OnKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_F1  && event.getAction() == KeyEvent.ACTION_DOWN)
            submitEvent();
        else if(keyCode == KeyEvent.KEYCODE_F2 && event.getAction() == KeyEvent.ACTION_DOWN)
            resetEvent();
        return true;
    }

    /**
     * 添加视图
     * @param resource 资源ID
     */
    protected View addView(int resource)
    {
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        FrameLayout frameLayout = (FrameLayout) ViewFragment.findViewById(R.id.content);
        View view = inflater.inflate(resource,null);
        frameLayout.addView(view);
        return view;
    }

    /**
     * 显示回调的错误信息
     */
    protected void showErrorMsg(String errorMsg){
        if (!TextUtils.isEmpty(errorMsg)) {
            textViewMessage.ShowMessage(errorMsg, ResultKind.ERROR);
            return;
        }
        textViewMessage.ShowMessage(getString(R.string.service_error), ResultKind.ERROR);
    }


    /**
     * 隐藏当前输入法界面
     * @param v
     */
    public void hideInputTools(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化界面
     */
    public abstract void initView();

    /**
     * 提交
     */
    public abstract void submitEvent();

    /**
     * 重置
     */
    public abstract void resetEvent();

    /**
     * 分页选择事件
     */
    public abstract void SelectPageEvent();

    /**
     * 设置标题
     */
    protected abstract void initTitle();



}
