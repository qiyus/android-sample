package com.vigorx.mvvm;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by songlei on 16/9/29.
 */
public class CalculationItem {
    // 被加数
    public ObservableField<String> firstAddend = new ObservableField<>();
    // 加数
    public ObservableField<String> addend = new ObservableField<>();
    // 和
    public ObservableField<String> result = new ObservableField<>();
}
