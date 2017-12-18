package com.chinasie.common.tools;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.chinasie.common.utility.IToolbarFunction;

/**
 * Created by Administrator on 2016-09-24.
 */

public class ToolBarDecorate {

    private Toolbar toolbar;
    private Activity activity;

    public ToolBarDecorate(Activity _activity, ViewGroup viewGroup)
    {
        activity = _activity;
        toolbar = (Toolbar)viewGroup;
    }

    public void DecorateView()
    {
        toolbar.setTitle(com.chinasie.common.R.string.library_name);
        toolbar.setLogo(com.chinasie.common.R.drawable.sie);
        toolbar.inflateMenu(com.chinasie.common.R.menu.menu_notification);//---添加工具按钮--
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     activity.finish();
                                                 }
                                             }
        );

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == com.chinasie.common.R.id.action_scan_code)
                    ((IToolbarFunction)activity).ScanCode();
                else if (menuItemId == com.chinasie.common.R.id.action_notification)
                    ((IToolbarFunction)activity).ShowMessage();
                return true;
            }
        });


    }

}
