package com.vigorx.mvvm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.vigorx.mvvm.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ICalculationContract.IMainView {
    private ActivityMainBinding mBinding;
    private CalculationItem mItem;
    private List<CalculationRecord> mList;
    private BaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mItem = new CalculationItem();
        mList = new ArrayList<>();
        ListView listRecord = (ListView) findViewById(R.id.list_record);
        mAdapter = new RecordAdapter(this, R.layout.item_record, mList, com.vigorx.mvvm.BR.item);
        listRecord.setAdapter(mAdapter);
        mBinding.setItem(mItem);
        mBinding.setEvent(new CalculationViewModel(this, mItem, mList));
    }

    @Override
    public void refreshRecordList() {
        mAdapter.notifyDataSetChanged();
    }
}
