package com.vigorx.mvp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songlei on 16/9/29.
 */
public class CalculationPresenter implements ICalculationContract.ICalculationPresenter {
    private ICalculationContract.ICalculationView mView;
    private ICalculationContract.ICalculationModel mModel;
    private List<CalculationItem> mData = new ArrayList<>();

    /**
     * 持有计算视图、履历数据
     *
     * @param view  视图
     * @param model 履历数据
     */
    public CalculationPresenter(ICalculationContract.ICalculationView view, ICalculationContract.ICalculationModel model) {
        this.mView = view;
        this.mModel = model;
    }

    @Override
    public void calculation() {

        // 计算
        String firstAddend = mView.getFirstAddend();
        String addend = mView.getAddend();
        String result = mModel.add(firstAddend, addend);

        // 显示结果
        mView.displayResult(result);

        // 追加列表数据
        CalculationItem item = new CalculationItem();
        item.setFirstAddend(firstAddend);
        item.setAddend(addend);
        item.setResult(result);
        mData.add(item);

        // 刷新履历列表
        mView.refreshRecordList();
    }

    @Override
    public List<CalculationItem> getRecordList() {
        return mData;
    }
}
