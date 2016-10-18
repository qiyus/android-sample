package com.vigorx.mvvm;

import android.databinding.BaseObservable;
import android.view.View;

import java.util.List;

/**
 * Created by songlei on 16/10/17.
 */

public class CalculationViewModel extends BaseObservable{
    private CalculationItem mItem;
    private List<CalculationRecord> mList;
    private ICalculationContract.ICalculationModel mModel;
    private ICalculationContract.IMainView mView;

    public CalculationViewModel(ICalculationContract.IMainView view, CalculationItem item, List<CalculationRecord> list) {
        this.mItem = item;
        this.mList = list;
        this.mModel = new CalculationModel();
        this.mView = view;
    }

    /**
     * 单击求和按钮的处理。
     * @param view 求和button
     */
    public void onClickSumButton(View view) {
        // 计算
        String firstAddend = mItem.firstAddend.get();
        String addend = mItem.addend.get();
        String result = mModel.add(firstAddend, addend);

        // 显示计算结果
        mItem.result.set(result);

        // 追加列表数据
        CalculationRecord record = new CalculationRecord();
        int number = mList.size() + 1;
        record.number.set(String.valueOf(number));
        String equation = firstAddend + " + " + addend + " = " + result;
        record.record.set(equation);
        mList.add(record);

        // 刷新列表
        mView.refreshRecordList();
    }
}
