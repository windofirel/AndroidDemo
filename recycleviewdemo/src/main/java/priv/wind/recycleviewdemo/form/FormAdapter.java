package priv.wind.recycleviewdemo.form;

import android.content.Context;
import android.support.annotation.NonNull;
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

import priv.wind.recycleviewdemo.R;

/**
 * @author Dongbaicheng
 * @version 2017/11/30
 */

// TODO: 2017/12/1  表头表体的背景更改接口 自适应宽度及其控制接口
// TODO: 2017/12/9 表头增加序号以及控制显示接口
// TODO: 2017/12/9 对泛型实体字段进行排序
// TODO: 2017/12/28 不要一开始就对数据源进行反射，应该在每次getItem时反射获取数据
public class FormAdapter<E> extends RecyclerView.Adapter {
    private static final int ITEM_HEADER_TYPE = 894;
    private static final int ITEM_BODY_TYPE = 899;

    protected boolean mEnableSequence = false;//启用序号列

    private Context mContext;//上下文环境
    private List<E> mDatas;//原始数据源
    private List<String> mHeaders;//表头
    private List<Integer> mColumnWidth;//列宽
    private List<List<String>> mBodys;//解析后的表体数据源
    private OnFormItemClickListener mOnFormItemClickListener;//点击事件
    private OnFormItemLongClickListener mOnFormItemLongClickListener;//长按事件

    FormAdapter(@NonNull List<E> datas, @NonNull Context context) {
        this(datas, context, false);
    }

    FormAdapter(@NonNull List<E> datas, @NonNull Context context, boolean enableSequence) {
        mEnableSequence = enableSequence;
        mDatas = datas;
        resolveEntityAnnotation(datas.get(0));
        mBodys = getDatas(mDatas);
        mContext = context;
    }

    public void setOnFormItemLongClickListener(OnFormItemLongClickListener onFormItemLongClickListener) {
        mOnFormItemLongClickListener = onFormItemLongClickListener;
    }

    public void setOnFormItemClickListener(OnFormItemClickListener onFormItemClickListener) {
        mOnFormItemClickListener = onFormItemClickListener;
    }

    /**
     * 通过索引获取元素
     *
     * @param index 元素所在索引
     * @return 实体元素
     */
    public E get(int index) {
        return mDatas.get(index);
    }

    /**
     * 获取列表数据集
     *
     * @return 列表数据集
     */
    public List<E> getDatas() {
        return mDatas;
    }

    /**
     * 新增数据
     *
     * @param entity 简单实体
     */
    public void add(@NonNull E entity) {
        List<String> dataRow = resolveEntity(entity, getItemCount());
        mDatas.add(entity);
        mBodys.add(dataRow);
        notifyDataSetChanged();
    }

    /**
     * 新增数据集
     *
     * @param entityList 简单实体
     */
    public void addList(@NonNull List<E> entityList) {
        for (E entity : entityList) {
            List<String> dataRow = resolveEntity(entity, getItemCount());
            mDatas.add(entity);
            mBodys.add(dataRow);
        }
        notifyDataSetChanged();
    }


    /**
     * 删除
     *
     * @param index 索引
     */
    public void remove(int index) {
        if (mEnableSequence) {
            mDatas.remove(index);
            mBodys.clear();
            mBodys.addAll(getDatas(mDatas));
            notifyDataSetChanged();
        } else {
            mDatas.remove(index);
            mBodys.remove(index);
            notifyItemRemoved(index + 1);
        }
    }

    /**
     * 删除
     *
     * @param element 被删除元素
     */
    public void remove(@NonNull E element) {
        int index = mDatas.indexOf(element);

        if (mEnableSequence) {
            mDatas.remove(index);
            mBodys.clear();
            mBodys.addAll(getDatas(mDatas));
            notifyDataSetChanged();
        } else {
            mDatas.remove(index);
            mBodys.remove(index);
            notifyItemRemoved(index + 1);
        }
    }

    /**
     * 查找列表元素索引
     *
     * @param element 列表元素
     * @return 返回元素索引
     */
    public int indexOf(@NonNull E element) {
        return mDatas.indexOf(element);
    }

    /**
     * 通知元素数据变更
     *
     * @param element 元素
     */
    public void notifyItemChanged(@NonNull E element) {
        int index = indexOf(element);
        notifyItemChanged(index + 1);
    }

    /**
     * 更新数据源
     *
     * @param datas 数据源
     */
    public void updataData(List<E> datas) {
        mDatas = datas;
        mBodys.clear();
        mBodys.addAll(getDatas(mDatas));
        notifyDataSetChanged();
    }

    /**
     * 清空列表
     */
    public void clear() {
        mDatas.clear();
        mBodys.clear();
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
                List<String> list = mBodys.get(position - 1);
                viewHolder.mTextViews.get(i).setText(list.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        //因为表头占了一行，所以总数为 数据数量 + 1
        return mDatas == null ? 0 : mDatas.size() + 1;
    }

    /**
     * 解析实体获取列表信息
     *
     * @param entity   实体
     * @param sequence 序号
     * @return 列表信息
     */
    private List<String> resolveEntity(@NonNull E entity, int sequence) {
        List<String> dataRow = new ArrayList<>();
        if (mEnableSequence) {
            dataRow.add(String.valueOf(sequence));
        }
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            FormAttr annotation = field.getAnnotation(FormAttr.class);
            if (annotation != null) {
                String value = "";
                try {
                    Object object = field.get(entity);
                    if (object != null) {
                        value = object.toString();
                    } else {
                        value = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(this.getClass().toString(), "getDatas: ", e);
                    value = "值反射失败";
                }
                dataRow.add(value);
            }
        }
        return dataRow;
    }

    /**
     * 解析获取表体数据
     *
     * @param mDatas 数据源
     * @return 表体数据
     */
    private List<List<String>> getDatas(List<E> mDatas) {
        List<List<String>> datas = new ArrayList<>();
        //用一个 map存储通过反射从数据源获取的数据，通过反射筛选出标记了特性的字段
        for (int i = 0; i < mDatas.size(); i++) {
            E entity = mDatas.get(i);
            List<String> dataRow = resolveEntity(entity, i + 1);
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
    private void resolveEntityAnnotation(E entity) {
        mHeaders = new ArrayList<>();
        mColumnWidth = new ArrayList<>();

        if (mEnableSequence) {
            mHeaders.add("序号");
            mColumnWidth.add(55);
        }
        Class entityClass = entity.getClass();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            FormAttr annotation = field.getAnnotation(FormAttr.class);
            if (annotation != null) {
                mHeaders.add(annotation.name());//获取字段对应的表头名称
                mColumnWidth.add(annotation.width());//获取字段对应的列宽
            }
        }
    }

    /**
     * 表单项点击接口
     */
    public interface OnFormItemClickListener {
        /**
         * 点击事件
         *
         * @param v        view
         * @param position 对应数据行
         */
        void onClick(View v, int position);
    }

    /**
     * 表单项长按接口
     */
    public interface OnFormItemLongClickListener {
        /**
         * 长按事件
         *
         * @param v        view
         * @param position 对应数据行
         * @return 长按事件监听是否在此结束
         */
        boolean onLongClick(View v, int position);
    }

    /**
     * 表头viewholder
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        List<TextView> mTextViews;

        HeaderViewHolder(View itemView, int size) {
            super(itemView);
            mTextViews = new ArrayList<>();
            FormHelper tool = new FormHelper(mContext);
            LinearLayout linearLayout = ((LinearLayout) itemView);

            for (int i = 0; i < size; i++) {
                if (i == 0
                        && mEnableSequence) {
                    TextView textView = tool.createHeaderSequence(mColumnWidth.get(i));
                    mTextViews.add(textView);
                    linearLayout.addView(textView);
                    continue;
                }
                TextView textView = tool.createHeader(mColumnWidth.get(i));
                mTextViews.add(textView);
                linearLayout.addView(textView);
            }
        }
    }

    /**
     * 表体viewholder
     */
    public class BodyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        List<TextView> mTextViews;

        BodyViewHolder(View itemView, int size) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mTextViews = new ArrayList<>();
            FormHelper tool = new FormHelper(mContext);
            LinearLayout linearLayout = ((LinearLayout) itemView);

            for (int i = 0; i < size; i++) {
                if (i == 0
                        && mEnableSequence) {
                    TextView textView = tool.createBodySequence(mColumnWidth.get(i));
                    mTextViews.add(textView);
                    linearLayout.addView(textView);
                    continue;
                }
                TextView textView = tool.createBody(mColumnWidth.get(i));
                mTextViews.add(textView);
                linearLayout.addView(textView);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnFormItemClickListener != null) {
                mOnFormItemClickListener.onClick(v, getLayoutPosition() - 1);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnFormItemLongClickListener != null) {
                return mOnFormItemLongClickListener.onLongClick(v, getLayoutPosition() - 1);
            }

            return false;
        }
    }
}
