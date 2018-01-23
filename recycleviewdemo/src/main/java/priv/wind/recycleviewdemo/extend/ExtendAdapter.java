package priv.wind.recycleviewdemo.extend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import priv.wind.recycleviewdemo.form.FormHelper;

/**
 * @author Dongbaicheng
 * @version 2018/1/23
 */

public class ExtendAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> mDatas;

    public ExtendAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FormHelper helper = new FormHelper(mContext);

        return new ExtendViewHolder(helper.createBody(230));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExtendViewHolder viewHolder = ((ExtendViewHolder) holder);
        viewHolder.tvOne.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ExtendViewHolder extends RecyclerView.ViewHolder {
        TextView tvOne;

        public ExtendViewHolder(View itemView) {
            super(itemView);
            tvOne = ((TextView) itemView);
        }
    }
}
