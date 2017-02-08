package com.vigorx.mvp;

/**
 * MVP-Presenter 负责控制
 * Created by Vigor on 2017/2/8.
 * Vigor_x studio (hsly_song@163.com)
 */
class CalculationPresenter implements Contract.IPresenter {
    private final Contract.IView mView;
    private final Contract.IModel mModel;

    /**
     * 持有计算视图、履历数据
     *
     * @param view  视图
     * @param model 履历数据
     */
    public CalculationPresenter(Contract.IView view, Contract.IModel model) {
        this.mView = view;
        this.mModel = model;
    }

    @Override
    public void add(String firstAddend, String addend) {

        // 计算
        String result = mModel.add(firstAddend, addend);

        // 显示结果
        mView.showResult(result);

        // 刷新履历列表
        mView.showRecord(mModel.getRecord());
    }
}
