package priv.wind.recycleviewdemo.form;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import priv.wind.recycleviewdemo.MainActivity;

/**
 * 表单类
 * 整体功能由 FormView,FormAdapter,FormHelper,FormAttr四部分组成
 * 实现了表单的添加、删除、清空、获取表单项等功能
 * @author Dongbaicheng
 * @version 2018/1/12
 */

public class FormView<E> {
    private RecyclerView mRecyclerView;
    private FormAdapter<E> mFormAdapter;

    public FormView(RecyclerView recyclerView, FormAdapter<E> formAdapter) {
        mRecyclerView = recyclerView;
        mFormAdapter = formAdapter;
    }

    //region 数据变更通知

    /**
     * 通知数据集变更
     */
    private void notifyDataSetChanged() {
        mFormAdapter.notifyDataSetChanged();
    }

    /**
     * 通知元素数据变更
     *
     * @param index 元素索引
     */
    private void notifyItemChanged(int index) {
        mFormAdapter.notifyItemChanged(index + 1);
    }

    /**
     * 通知元素数据变更
     *
     * @param element 元素
     */
    private void notifyItemChanged(@NonNull E element) {
        mFormAdapter.notifyItemChanged(element);
    }
    //endregion

    //region 对外暴露的接口

    /**
     * 添加新元素
     *
     * @param element 新元素
     */
    @SuppressWarnings("unchecked")
    public void add(@NonNull E element) {
        mFormAdapter.add(element);
    }

    /**
     * 添加新元素集
     *
     * @param elements 新元素集
     */
    @SuppressWarnings("unchecked")
    public void addList(@NonNull List<E> elements) {
        mFormAdapter.addList(elements);
    }

    /**
     * 通过索引删除列表元素
     *
     * @param index 元素索引
     */
    public void remove(int index) {
        mFormAdapter.remove(index);
    }

    /**
     * 通过对比hashcode删除列表元素
     *
     * @param element 元素
     */
    @SuppressWarnings("unchecked")
    public void remove(@NonNull E element) {
        mFormAdapter.remove(element);
    }

    /**
     * 更新列表数据集
     *
     * @param datas 新数据集
     */
    @SuppressWarnings("unchecked")
    public void replaceDatas(List<E> datas) {
        mFormAdapter.replaceData(datas);
    }

    /**
     * 清空列表数据
     */
    public void clear() {
        mFormAdapter.clear();
    }

    /**
     * 通过索引获取元素
     *
     * @param index 元素所在索引
     * @return 实体元素
     */
    public E get(int index) {
        return mFormAdapter.get(index);
    }

    /**
     * 获取列表当前数据集
     *
     * @return 当前数据集
     */
    public List<E> getDatas() {
        return mFormAdapter.getRawDatas();
    }
    //endregion

    /**
     * 表单 构建 类
     * 泛型E 为 数据集对应的bean类
     *
     * @author Dongbaicheng
     * @version 2018/1/12
     */
    public static class FormBuilder<E> {
        private FormParams mFormParams;
        private List<E> mDatas;
        private FormAdapter<E> mFormAdapter;

        /**
         * 构建表单类构造方法
         *
         * @param context 上下文
         * @param resId   form控件Id
         */
        public FormBuilder(@NonNull Context context, int resId, @NonNull List<E> datas) {
            mFormParams = new FormParams(context, resId);
            mDatas = datas;
        }

        /**
         * 基于设置的参数创建一个表单控件
         *
         * @return 表单控件引用
         */
        public FormView<E> build() {
            RecyclerView recyclerView = ((MainActivity) mFormParams.mContext).findViewById(mFormParams.mResId);
            mFormAdapter = new FormAdapter<>(mDatas, mFormParams.mContext, mFormParams.mEnableSequence);

            //设置点击监听事件
            if (mFormParams.mOnFormItemClickListener != null) {
                mFormAdapter.setOnFormItemClickListener(mFormParams.mOnFormItemClickListener);
            }

            //设置长按监听事件
            if (mFormParams.mEnableDelete) {
                mFormAdapter.setOnFormItemLongClickListener(new FormAdapter.OnFormItemLongClickListener() {
                    @Override
                    public boolean onLongClick(View v, int position) {
                        mFormAdapter.remove(position);
                        return true;
                    }
                });
            } else if (mFormParams.mOnFormItemLongClickListener != null) {
                mFormAdapter.setOnFormItemLongClickListener(mFormParams.mOnFormItemLongClickListener);
            }

            //设置布局管理器
            if (mFormParams.mLayoutManager == null) {
                //生成默认的线性垂直布局
                mFormParams.mLayoutManager = new LinearLayoutManager(mFormParams.mContext, LinearLayoutManager.VERTICAL, false);
            }
            recyclerView.setLayoutManager(mFormParams.mLayoutManager);

            //添加装饰器
            if (mFormParams.mItemDecorations.size() > 0) {
                for (RecyclerView.ItemDecoration itemDecoration : mFormParams.mItemDecorations) {
                    recyclerView.addItemDecoration(itemDecoration);
                }
            }
            //设置适配器
            recyclerView.setAdapter(mFormAdapter);

            return new FormView<>(recyclerView, mFormAdapter);
        }

        /**
         * 设置是否启用序号列
         *
         * @return 序号列
         */
        public FormBuilder setEnableSequence() {
            mFormParams.mEnableSequence = true;
            return this;
        }

        /**
         * 设置长按删除是否启用
         *
         * @return 表单构建者
         */
        public FormBuilder setEnableDelete() {
            mFormParams.mEnableDelete = true;
            return this;
        }

        /**
         * 设置表单项单击事件
         *
         * @param listener 监听事件
         * @return 表单构建者
         */
        public FormBuilder setOnFormClickListener(@NonNull FormAdapter.OnFormItemClickListener listener) {
            mFormParams.mOnFormItemClickListener = listener;
            return this;
        }

        /**
         * 设置表单项长按事件
         *
         * @param listener 监听事件
         * @return 表单构建者
         */
        public FormBuilder setOnFormLongClickListener(@NonNull FormAdapter.OnFormItemLongClickListener listener) {
            mFormParams.mOnFormItemLongClickListener = listener;
            return this;
        }

        /**
         * 设置表单布局管理器
         *
         * @param manager 布局管理器
         * @return 表单构建者
         */
        public FormBuilder setLayoutManager(@NonNull RecyclerView.LayoutManager manager) {
            mFormParams.mLayoutManager = manager;
            return this;
        }

        /**
         * 添加装饰器
         *
         * @param itemDecoration 装饰器
         * @return 表单构建者
         */
        public FormBuilder addItemDecoration(@NonNull RecyclerView.ItemDecoration itemDecoration) {
            mFormParams.mItemDecorations.add(itemDecoration);
            return this;
        }
    }

    /**
     * 表单构建参数
     */
    public static class FormParams {
        private Context mContext;//上下文
        private int mResId;//form控件Id
        private RecyclerView.LayoutManager mLayoutManager;
        private List<RecyclerView.ItemDecoration> mItemDecorations;

        private FormAdapter.OnFormItemClickListener mOnFormItemClickListener;//点击监听事件
        private FormAdapter.OnFormItemLongClickListener mOnFormItemLongClickListener;//长按监听事件

        private boolean mEnableDelete;//控制是否启用长按删除
        private boolean mEnableSequence;//控制是否启用序号列


        public FormParams(@NonNull Context context, int resId) {
            mContext = context;
            mResId = resId;
            mEnableDelete = false;
            mEnableSequence = false;
            mItemDecorations = new ArrayList<>();
        }
    }
}
