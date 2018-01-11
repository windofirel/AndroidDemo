package priv.wind.recycleviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import priv.wind.recycleviewdemo.annotation.FormHeader;

/**
 * @author Dongbaicheng
 * @version 2017/11/30
 */

// TODO: 2017/12/1  表头表体的背景更改接口 自适应宽度及其控制接口
// TODO: 2017/12/9 表头增加序号以及控制显示接口
// TODO: 2017/12/9 点击行高亮
// TODO: 2017/12/9 对泛型实体字段进行排序
// TODO: 2017/12/28 不要一开始就对数据源进行反射，应该在每次getItem时反射获取数据
public class MyAdapter<T> extends RecyclerView.Adapter {
    private static final int ITEM_HEADER_TYPE = 894;
    private static final int ITEM_BODY_TYPE = 899;
    private List<T> mDatas;//原始数据源
    private List<String> mHeaders;//表头
    private Context mContext;//上下文环境
    private List<String[]> mBodys;//解析后的表体数据源

    MyAdapter(List<T> datas, Context context) {
        mDatas = datas;
        mHeaders = getHeaderNames(datas.get(0));
        mBodys = getDatas(mDatas);
        mContext = context;
    }

    /**
     * 新增数据
     *
     * @param entity 简单实体
     */
    public void add(T entity) {
        mDatas.add(entity);

        String[] dataRow = new String[mHeaders.size()];
        int index = 0;
        for (Field field : entity.getClass().getDeclaredFields()) {
            FormHeader annotation = field.getAnnotation(FormHeader.class);
            if (annotation != null) {
                String value = "";
                try {
                    value = field.get(entity).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e(this.getClass().toString(), "getDatas: ", e);
                    value = "值反射失败";
                }
                dataRow[index] = value;
                index++;
            }
        }
        mBodys.add(dataRow);
    }

    /**
     * 删除
     *
     * @param index 索引
     */
    public void remove(int index) {
        mDatas.remove(index);
        mBodys.remove(index);
        // FIXME: 2017/12/9 存在问题，删除时会存在key值不连续，导致取值失败
        notifyDataSetChanged();
    }

    /**
     * 更新数据源
     *
     * @param data 数据源
     */
    public void updataData(ArrayList<T> data) {
        mDatas = data;
        mBodys = getDatas(mDatas);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEADER_TYPE;
        }
        return ITEM_BODY_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        if (viewType == ITEM_HEADER_TYPE) {
            return new HeaderViewHolder(view, mHeaders.size());
        }
        return new BodyViewHolder(view, mHeaders.size());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_HEADER_TYPE) {
            HeaderViewHolder viewHolder = ((HeaderViewHolder) holder);
            for (int i = 0; i < mHeaders.size(); i++) {
                viewHolder.mTextViews.get(i).setText(mHeaders.get(i));
            }
        } else {
            final BodyViewHolder viewHolder = ((BodyViewHolder) holder);
            for (int i = 0; i < mHeaders.size(); i++) {
                //因为 position 0 是表头，所以数据行数 对应为 实际位置 - 1
                String[] list = mBodys.get(position - 1);
                viewHolder.mTextViews.get(i).setText(list[i]);
            }
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remove(viewHolder.getLayoutPosition() - 1);
                    return true;
                }
            });
            //设置点击变色
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //因为表头占了一行，所以总数为 数据数量 + 1
        return mDatas == null ? 0 : mDatas.size() + 1;
    }

    /**
     * 解析获取表体数据
     *
     * @param mDatas 数据源
     * @return 表体数据
     */
    private List<String[]> getDatas(List<T> mDatas) {
        List<String[]> datas = new ArrayList<>();
        //用一个 map存储通过反射从数据源获取的数据，通过反射筛选出标记了特性的字段
        for (int i = 0; i < mDatas.size(); i++) {
            T entity = mDatas.get(i);
            String[] dataRow = new String[mHeaders.size()];
            int index = 0;
            for (Field field : entity.getClass().getDeclaredFields()) {
                FormHeader annotation = field.getAnnotation(FormHeader.class);
                if (annotation != null) {
                    String value = "";
                    try {
                        value = field.get(entity).toString();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        Log.e(this.getClass().toString(), "getDatas: ", e);
                        value = "值反射失败";
                    }
                    dataRow[index] = value;
                    index++;
                }
            }
            datas.add(dataRow);
        }
        return datas;
    }

    /**
     * 反射获取表头
     *
     * @param entity 列表行数据实体
     * @return 表头
     */
    private List<String> getHeaderNames(T entity) {
        List<String> headerNames = new ArrayList<>();
        Class entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            FormHeader annotation = field.getAnnotation(FormHeader.class);
            if (annotation != null) {
                headerNames.add(annotation.name());
            }
        }

        return headerNames;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        List<TextView> mTextViews;

        HeaderViewHolder(View itemView, int size) {
            super(itemView);
            mTextViews = new ArrayList<>();
            UIHelper tool = new UIHelper(mContext);
            LinearLayout linearLayout = ((LinearLayout) itemView);

            for (int i = 0; i < size; i++) {
                TextView textView = tool.createHeader();
                mTextViews.add(textView);
                linearLayout.addView(textView);
            }
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {
        List<TextView> mTextViews;

        BodyViewHolder(View itemView, int size) {
            super(itemView);
            mTextViews = new ArrayList<>();
            UIHelper tool = new UIHelper(mContext);
            LinearLayout linearLayout = ((LinearLayout) itemView);

            for (int i = 0; i < size; i++) {
                TextView textView = tool.createBody();
                mTextViews.add(textView);
                linearLayout.addView(textView);
            }
        }
    }
}
