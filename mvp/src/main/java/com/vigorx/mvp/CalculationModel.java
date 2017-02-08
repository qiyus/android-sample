package com.vigorx.mvp;

import java.util.ArrayList;
import java.util.List;

/**
 * MVP-Model 负责业务逻辑
 * Created by Vigor on 2017/2/8.
 * Vigor_x studio (hsly_song@163.com)
 */
class CalculationModel implements Contract.IModel {
    private final List<CalculationItem> mRecord = new ArrayList<>();

    @Override
    public String add(String firstAddend, String addend) {
        int a = 0;
        try {
            a = Integer.parseInt(firstAddend);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        int b = 0;
        try {
            b = Integer.parseInt(addend);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // 业务处理
        int c = a + b;
        String result = String.valueOf(c);

        // 追加列表数据
        CalculationItem item = new CalculationItem();
        item.setFirstAddend(firstAddend);
        item.setAddend(addend);
        item.setResult(result);
        mRecord.add(item);

        return result;
    }

    @Override
    public List<CalculationItem> getRecord() {
        return mRecord;
    }
}
