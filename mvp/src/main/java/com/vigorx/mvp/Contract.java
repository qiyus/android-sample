package com.vigorx.mvp;

import java.util.List;

/**
 * MVP-接口集
 * Created by Vigor on 2017/2/8.
 * Vigor_x studio (hsly_song@163.com)
 */
interface Contract {
    interface IModel {
        /**
         * 求和
         *
         * @param firstAddend 被加数
         * @param addend      加数
         * @return 和
         */
        String add(String firstAddend, String addend);

        /**
         * 追加计算结果到履历
         */
        void addRecord(CalculationItem item);

        /**
         * 返回计算履历
         */
        List<CalculationItem> getRecord();
    }

    interface IPresenter {
        /**
         * 进行加法运算并在视图设置结果。
         */
        void add(String firstAddend, String addend);
    }

    interface IView {
        /**
         * 显示加法的计算结果
         *
         * @param result 计算结果
         */
        void showResult(String result);

        /**
         * 刷新履历列表
         */
        void showRecord(List<CalculationItem> record);
    }
}
