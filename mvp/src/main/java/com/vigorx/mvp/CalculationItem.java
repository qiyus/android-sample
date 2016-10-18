package com.vigorx.mvp;

/**
 * Created by songlei on 16/9/29.
 */
public class CalculationItem {
    public String getFirstAddend() {
        return firstAddend;
    }

    public void setFirstAddend(String firstAddend) {
        this.firstAddend = firstAddend;
    }

    public String getAddend() {
        return addend;
    }

    public void setAddend(String addend) {
        this.addend = addend;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return firstAddend + "   +   " + addend + "   =   " + result;
    }

    private String firstAddend;
    private String addend;
    private String result;
}
