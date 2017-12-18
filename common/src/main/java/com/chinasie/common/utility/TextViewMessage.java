package com.chinasie.common.utility;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.chinasie.common.config.ResultKind;
import com.chinasie.common.R;

/**
 * Created by 古永财 on 2016-09-24.
 */
public class TextViewMessage {

    private Handler mHandler;
    private TextView viewMsg;//---消息框----
    private Context context;

    public TextViewMessage(Context _context, View _view)
    {
        mHandler = new Handler(_context.getMainLooper());
        viewMsg = (TextView)_view;
        context  =_context;
    }

    public void ShowMessage() {
        if(viewMsg.getVisibility() == View.GONE) {
            viewMsg.setVisibility(View.VISIBLE);
        }else
        {
            viewMsg.setVisibility(View.GONE);
        }
    }
    public void Clear(){viewMsg.setText("");}
    /**
     * 显示消息
     */
    public void ShowMessage(String resultInfo,ResultKind resultKind)
    {
        if(resultKind == ResultKind.ERROR)
            viewMsg.setTextColor(context.getResources().getColor(R.color.red));
        else
            viewMsg.setTextColor(context.getResources().getColor(R.color.black));

        viewMsg.setText(resultInfo);
        viewMsg.setVisibility(View.VISIBLE);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewMsg.setVisibility(View.GONE);
            }
        }, 5000);//----3秒后停止显示----
    }

}
