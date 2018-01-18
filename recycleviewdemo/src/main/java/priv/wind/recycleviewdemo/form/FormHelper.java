package priv.wind.recycleviewdemo.form;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import priv.wind.recycleviewdemo.R;

/**
 * UI创建工具类
 *
 * @author Dongbaicheng
 * @version 2017/12/1
 */

public class FormHelper {
    private Context mContext;

    public FormHelper(Context context) {
        mContext = context;
    }

    /**
     * 创建表头控件
     *
     * @return 表头项控件
     */
    public TextView createHeader(int width) {
        TextView tvItem = new TextView(mContext);
        tvItem.setHeight(30);
        tvItem.setWidth(width);
        tvItem.setBackgroundResource(R.drawable.bg_tv_list_header);
        tvItem.setGravity(Gravity.CENTER);
        tvItem.setTextSize(16);
        tvItem.setTextColor(Color.parseColor("#212121"));
        tvItem.setLines(1);
        tvItem.setPadding(2, 0, 2, 0);
        return tvItem;
    }

    /**
     * 创建表体控件
     *
     * @return 表体项控件
     */
    public TextView createBody(int width) {
        TextView tvItem = new TextView(mContext);
        tvItem.setHeight(30);
        tvItem.setWidth(width);
        tvItem.setBackgroundResource(R.drawable.bg_tv_list_body);
        tvItem.setGravity(Gravity.CENTER);
        tvItem.setTextSize(16);
        //                tvItem.setTextColor(Color.parseColor("#757575"));
        tvItem.setMaxLines(2);
        tvItem.setPadding(2, 0, 2, 0);
        return tvItem;
    }
}
