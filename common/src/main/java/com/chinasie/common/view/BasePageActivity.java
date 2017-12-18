package com.chinasie.common.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.chinasie.common.config.ResultKind;
import com.chinasie.common.tools.ToolBarDecorate;
import com.chinasie.common.utility.IToolbarFunction;
import com.chinasie.common.zxing.activity.CaptureActivity;
import com.chinasie.common.R;
import com.chinasie.common.tools.PlatformContextHelper;

import java.util.HashMap;

;

/**
 * Created by 古永财 on 2016-09-24.
 */
public abstract class BasePageActivity extends AppCompatActivity implements IToolbarFunction {

    protected Toolbar toolbar;
    protected HashMap<String,Object> HaspMapData = new HashMap<String,Object>();
    protected BasePageFragment[] pages = null;//---分页---
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题
        setContentView(R.layout.activity_page);
        PlatformContextHelper.getInstance().InitPlatformContextHelper(this); //---初始化本地信息---
        //-----初始化分页------
        initPages();
        if(pages == null)
        {
            pages = new BasePageFragment[2];
            pages[0] = new BlankFragment("空白页1");
            pages[1] = new BlankFragment("空白页2");
        }
        //-----分页适配器-----
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());//---初始化---
        mViewPager = (ViewPager) findViewById(R.id.container);
		mViewPager.setOffscreenPageLimit(pages.length);//---缓存个数---
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pages[position].SelectPageEvent();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //-----初始化工具栏----
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        ToolBarDecorate toolBarDecorate = new ToolBarDecorate(this,toolbar);
        toolBarDecorate.DecorateView();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        initView();
        initData();
    }




    /**
     * 消息框显示
     */
    public void ShowMessage()
    {
        pages[mViewPager.getCurrentItem()].textViewMessage.ShowMessage();
    }

    /**
     * 扫描二维码,条码
     */
    public void ScanCode()
    {
        try {
            //打开扫描界面扫描条形码或二维码
            Intent openCameraIntent = new Intent(this, CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);
        }
        catch (Exception ex){
            Log.e(this.getClass().getName(),ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * 扫描返回结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");

            ViewGroup vp = (ViewGroup)pages[mViewPager.getCurrentItem()].ViewFragment;
            setScanValue(vp, scanResult + "/n");
        }
    }

    /**
     * 遍历并赋值到焦点控件中,暂时支持TextView, EditText
     * @param viewGroup 组
     * @param value 返回条码值
     */
    private void setScanValue(ViewGroup viewGroup,String value) {
        if (viewGroup == null) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                // 若是布局控件（LinearLayout或RelativeLayout）,继续查询子View
                this.setScanValue((ViewGroup) view,value);
            }
            if(view.isFocused()) {
                if (view instanceof TextView) {
                    TextView editView = (TextView) view;
                    editView.setText(value);
                    return;
                } else if (view instanceof EditText) {
                    EditText editView = (EditText) view;
                    editView.setText(value);
                    return;
                }
            }else
                continue;
        }
    }


    /**
     * 刷新分页内容
     */
    public void RefreshPagerAdapter()
    {
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return pages[position];
        }

        @Override
        public int getCount() {
            return pages.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pages[position].title;
        }

        public Object instantiateItem(ViewGroup container, int position)
        {
            return super.instantiateItem(container,position);
        }

    }

    /**
     * 显示消息框
     */
    private void showMessage(String resultInfo,ResultKind resultKind)
    {
        pages[mViewPager.getCurrentItem()].textViewMessage.ShowMessage(resultInfo,resultKind);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Log.d("ActionBar", "OnKey事件");
        if((keyCode == KeyEvent.KEYCODE_F1 || keyCode == KeyEvent.KEYCODE_F2 )
                && event.getAction() == KeyEvent.ACTION_DOWN) {
                pages[mViewPager.getCurrentItem()].OnKeyDown(keyCode, event);
        }else if(keyCode == KeyEvent.KEYCODE_BACK)
            finish();
        return super.onKeyDown(keyCode, event);
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
     * 初始化分页
     */
    protected abstract void initPages();
}
