package com.vigorx.mvp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by songlei on 16/9/29.
 */
public class RecordAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayoutId;
    private List<CalculationItem> mData;

    /**
     * 创建计算履历的Adapter
     * @param context 上下文
     * @param resource List item的layout
     * @param objects 数据
     */
    public RecordAdapter(Context context, int resource, List<CalculationItem> objects) {
        this.mLayoutId = resource;
        this.mContext = context;
        this.mData = objects;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mData) {
            count = mData.size();
        }
        return count;
    }

    @Override
    public CalculationItem getItem(int position) {
        CalculationItem item = null;
        if (null != mData) {
            item = mData.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mLayoutId, null);
            viewHolder.number = (TextView) convertView.findViewById(R.id.item_number);
            viewHolder.record = (TextView) convertView.findViewById(R.id.item_record);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CalculationItem item = getItem(position);
        if (null != item) {
            viewHolder.number.setText(String.valueOf(position + 1));
            viewHolder.record.setText(item.toString());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView number;
        TextView record;
    }
}
