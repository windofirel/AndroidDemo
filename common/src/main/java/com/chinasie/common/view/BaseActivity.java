package com.chinasie.common.view;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinasie.common.R;
import com.chinasie.common.config.ResultKind;
import com.chinasie.common.tools.PlatformContextHelper;
import com.chinasie.common.tools.ToolBarDecorate;
import com.chinasie.common.utility.IToolbarFunction;
import com.chinasie.common.utility.TextViewMessage;
import com.chinasie.common.zxing.activity.CaptureActivity;

/**
 * Created by 古永财 on 2016-09-24.
 */

public abstract class BaseActivity extends AppCompatActivity implements IToolbarFunction {

    protected Toolbar toolbar;
    protected TextView tvMsg;
    public TextViewMessage textViewMessage;
    protected View viewEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //-----在4.0之后在主线程里面执行Http请求都会报这个错，也许是怕Http请求时间太长造成程序假死的情况吧。
        // 相应的解决方案
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//取消标题
        setContentView(R.layout.activity_base);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvMsg.setVisibility(View.GONE);//---隐藏--
        textViewMessage = new TextViewMessage(this, tvMsg);
        //-----初始化工具栏----
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        ToolBarDecorate toolBarDecorate = new ToolBarDecorate(this, toolbar);
        toolBarDecorate.DecorateView();
        //-----获取系统配置信息---
        PlatformContextHelper.getInstance().InitPlatformContextHelper(this); //---初始化本地信息---

        initData();//---初始化数据----
        initView();//---初始化界面---
    }

    /**
     * 消息异步设置焦点
     */
    private Handler mHandlerFoucus = new Handler()
    {
        public void handleMessage(Message msg) {
            View view = findViewById(msg.what);
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
    protected void SetFocus(View v)
    {
        Message msg = new Message();
        msg.what = v.getId();
        mHandlerFoucus.sendMessage(msg);
    }

    /**
     * 隐藏当前输入法界面
     * @param v
     */
    public void HideInputTools(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }


    /**
     * 显示消息框
     */
    public void ShowMessage()
    {
        textViewMessage.ShowMessage();
    }

    /**
     * 扫描二维码,条码
     */
    public void ScanCode()
    {
        try {
            //打开扫描界面扫描条形码或二维码
            Intent openCameraIntent = new Intent(BaseActivity.this, CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0);
        }
        catch (Exception ex){
            Log.e(this.getClass().getName(),ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 添加视图
     * @param resource 资源ID
     */
    protected void AddView(int resource)
    {
        LayoutInflater inflater = getLayoutInflater();
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.content);
        viewEdit = inflater.inflate(resource,null);
        frameLayout.addView(viewEdit);
    }

    /**
     * 显示回调的错误信息
     */
    protected void showErrorMsg(String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            textViewMessage.ShowMessage(errorMsg, ResultKind.ERROR);
            return;
        }
        textViewMessage.ShowMessage(getString(R.string.service_error), ResultKind.ERROR);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        Log.d("ActionBar", "OnKey事件");
        if(keyCode == KeyEvent.KEYCODE_F1 && event.getAction() == KeyEvent.ACTION_DOWN)
            submitEvent();
        else if(keyCode == KeyEvent.KEYCODE_F2 && event.getAction() == KeyEvent.ACTION_DOWN)
            resetEvent();
        else if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
            finish();
        return super.onKeyDown(keyCode, event);
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
            ViewGroup vp = (ViewGroup) viewEdit;
            setScanValue(vp, scanResult);
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
                if (view instanceof EditText) {
                    EditText editView = (EditText) view;
                    editView.setText(value);
                    editView.setSelection(value.length());

                    return;
                } else if (view instanceof TextView) {
                    TextView editView = (TextView) view;
                    editView.setText(value);

                    return;
                }
            }else
                continue;
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
    protected abstract void submitEvent();

    /**
     * 重置
     */
    protected abstract void resetEvent();


}
