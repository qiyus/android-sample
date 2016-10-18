package com.vigorx.mvvm;

/**
 * Created by songlei on 16/10/18.
 */

public interface ICalculationContract {
    /**
     * Created by songlei on 16/10/17.
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
     * Created by songlei on 16/10/18.
     */

    interface IMainView {
        /**
         * 刷新履历列表
         */
        void refreshRecordList();
    }
}
