package priv.wind.recycleviewdemo.form;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final int ITEM_BODY_TYPE = 899;

    protected boolean mEnableSequence = false;//启用序号列

    private Context mContext;//上下文环境
    private List<E> mRawDatas;//原始数据源
    private List<List<String>> mFormDatas;//解析后的表单数据源
    private List<FormAttrBean> mFormAttrBeans;
    private OnFormItemClickListener mOnFormItemClickListener;//点击事件
    private OnFormItemLongClickListener mOnFormItemLongClickListener;//长按事件

    FormAdapter(@NonNull Context context, @NonNull List<E> rawDatas, List<FormAttrBean> formAttrBeans) {
        this(context, rawDatas, formAttrBeans, false);
    }

    FormAdapter(@NonNull Context context, @NonNull List<E> rawDatas, List<FormAttrBean> formAttrBeans, boolean enableSequence) {
        mEnableSequence = enableSequence;
        mRawDatas = rawDatas;
        mFormAttrBeans = formAttrBeans;
        mFormDatas = getFormDatas(mRawDatas);
        mContext = context;
    }

    //region 数据操作
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
        return mRawDatas.get(index);
    }

    /**
     * 获取列表数据集
     *
     * @return 列表数据集
     */
    public List<E> getRawDatas() {
        return mRawDatas;
    }

    /**
     * 新增数据
     *
     * @param entity 简单实体
     */
    public void add(@NonNull E entity) {
        List<String> dataRow = resolveEntity(entity, getItemCount());
        mRawDatas.add(entity);
        mFormDatas.add(dataRow);
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
            mRawDatas.add(entity);
            mFormDatas.add(dataRow);
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
            mRawDatas.remove(index);
            mFormDatas.clear();
            mFormDatas.addAll(getFormDatas(mRawDatas));
            notifyDataSetChanged();
        } else {
            mRawDatas.remove(index);
            mFormDatas.remove(index);
            notifyItemRemoved(index);
        }
    }

    /**
     * 删除
     *
     * @param element 被删除元素
     */
    public void remove(@NonNull E element) {
        int index = mRawDatas.indexOf(element);

        if (mEnableSequence) {
            mRawDatas.remove(index);
            mFormDatas.clear();
            mFormDatas.addAll(getFormDatas(mRawDatas));
            notifyDataSetChanged();
        } else {
            mRawDatas.remove(index);
            mFormDatas.remove(index);
            notifyItemRemoved(index);
        }
    }

    /**
     * 查找列表元素索引
     *
     * @param element 列表元素
     * @return 返回元素索引
     */
    public int indexOf(@NonNull E element) {
        return mRawDatas.indexOf(element);
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
     * 替换数据源
     *
     * @param datas 数据源
     */
    public void replaceData(List<E> datas) {
        mRawDatas = datas;
        mFormDatas.clear();
        mFormDatas.addAll(getFormDatas(mRawDatas));
        notifyDataSetChanged();
    }

    /**
     * 清空列表
     */
    public void clear() {
        mRawDatas.clear();
        mFormDatas.clear();
        notifyDataSetChanged();
    }
    //endregion

    @Override
    public int getItemViewType(int position) {
        return ITEM_BODY_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new BodyViewHolder(view, mFormAttrBeans.size());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BodyViewHolder viewHolder = ((BodyViewHolder) holder);
        for (int i = 0; i < mFormAttrBeans.size(); i++) {
            //因为 position 0 是表头，所以数据行数 对应为 实际位置 - 1
            List<String> list = mFormDatas.get(position);
            viewHolder.mTextViews.get(i).setText(list.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mRawDatas == null ? 0 : mRawDatas.size();
    }

    /**
     * 解析实体获取列表信息
     *
     * @param entity   实体
     * @param sequence 序号
     * @return 列表信息
     */
    private List<String> resolveEntity(@NonNull E entity, int sequence) {
        String[] dataRow = new String[mFormAttrBeans.size()];

        Field[] fields = entity.getClass().getDeclaredFields();
        List<Field> usefulFields = new ArrayList<>();
        //筛选出标记了注解的字段
        for (Field field : fields) {
            FormAttr annotation = field.getAnnotation(FormAttr.class);
            if (annotation != null) {
                usefulFields.add(field);
            }
        }

        if (mEnableSequence) {
            //给每一行的数据头增加序号
            dataRow[0] = String.valueOf(sequence);
        }
        for (int i = 0; i < usefulFields.size(); i++) {
            Field field = usefulFields.get(i);
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
                Log.e(this.getClass().toString(), "getRawDatas: ", e);
                value = "值反射失败";
            }

            if (mEnableSequence) {
                dataRow[mFormAttrBeans.get(i + 1).columnSeq] = value;
            } else {
                dataRow[mFormAttrBeans.get(i).columnSeq - 1] = value;
            }
        }
        return Arrays.asList(dataRow);
    }

    /**
     * 解析获取表体数据
     *
     * @param mRawDatas 原始数据源
     * @return 表体数据
     */
    private List<List<String>> getFormDatas(List<E> mRawDatas) {
        List<List<String>> datas = new ArrayList<>();
        //用一个 map存储通过反射从数据源获取的数据，通过反射筛选出标记了特性的字段
        for (int i = 0; i < mRawDatas.size(); i++) {
            E entity = mRawDatas.get(i);
            List<String> dataRow = resolveEntity(entity, i + 1);
            datas.add(dataRow);
        }
        return datas;
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
     * 表体viewholder
     */
    public class BodyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        List<TextView> mTextViews;

        BodyViewHolder(View itemView, int columnCount) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mTextViews = new ArrayList<>();
            FormHelper tool = new FormHelper(mContext);
            LinearLayout linearLayout = ((LinearLayout) itemView);

            for (int i = 0; i < columnCount; i++) {
                int width = mFormAttrBeans.get(i).columnWidth;
                if (i == 0
                        && mEnableSequence) {
                    TextView textView = tool.createBody(width);
                    mTextViews.add(textView);
                    linearLayout.addView(textView);

                    LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) textView.getLayoutParams());
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.width = width;
                    textView.setLayoutParams(layoutParams);

                    continue;
                }
                TextView textView = tool.createBody(width);
                mTextViews.add(textView);
                linearLayout.addView(textView);

                LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams) textView.getLayoutParams());
                layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.width = width;
                textView.setLayoutParams(layoutParams);
            }
        }

        @Override
        public void onClick(View v) {
            if (mOnFormItemClickListener != null) {
                mOnFormItemClickListener.onClick(v, getLayoutPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnFormItemLongClickListener != null) {
                return mOnFormItemLongClickListener.onLongClick(v, getLayoutPosition());
            }

            return false;
        }
    }
}
