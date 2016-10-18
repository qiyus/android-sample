package com.vigorx.mvvm;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by songlei on 16/10/17.
 */

public class RecordAdapter<T> extends BaseAdapter {
    private Context mContext;
    private int mLayoutId;
    private List<T> mList;
    private  int mVariableId;

    /**
     * 属性设置
     * @param context 上下文
     * @param layoutId list item的layout id
     * @param list 列表数据
     * @param resId item id
     */
    public RecordAdapter(Context context, int layoutId, List<T> list, int resId) {
        mContext = context;
        mLayoutId = layoutId;
        mList = list;
        mVariableId = resId;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewDataBinding dataBinding;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            dataBinding = DataBindingUtil.inflate(inflater, mLayoutId, parent, false);
        }
        else {
            dataBinding = DataBindingUtil.getBinding(convertView);
        }

        dataBinding.setVariable(mVariableId, mList.get(position));
        return dataBinding.getRoot();
    }
}
