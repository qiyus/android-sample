package com.vigorx.mvp;

import java.util.List;

/**
 * Created by songlei on 16/9/30.
 */
public interface ICalculationContract {
    /**
     * Created by songlei on 16/9/29.
     */
    interface ICalculationModel {
        /**
         * 求和
         * @param firstAddend 被加数
         * @param addend 加数
         * @return 和
         */
        String add(String firstAddend, String addend);
    }

    /**
     * Created by songlei on 16/9/30.
     */
    interface ICalculationPresenter {
        /**
         * 进行加法运算并在视图设置结果。
         */
        void calculation();

        /**
         * 取得计算列表数据
         */
        List<CalculationItem> getRecordList();
    }

    /**
     * Created by songlei on 16/9/29.
     */
    interface ICalculationView {
        /**
         * 显示加法的计算结果
         * @param result 计算结果
         */
        void displayResult(String result);

        /**
         * 取得被加数
         * @return 被加数
         */
        String getFirstAddend();

        /**
         * 取得加数
         * @return 加数
         */
        String getAddend();

        /**
         * 刷新履历列表
         */
        void refreshRecordList();
    }
}
