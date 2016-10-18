package com.vigorx.mvp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songlei on 16/9/29.
 */
public class CalculationModel implements ICalculationContract.ICalculationModel {

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

        return String.valueOf(c);
    }
}
