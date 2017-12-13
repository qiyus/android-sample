package com.vigor.mvp;

/**
 * MVP-Model 数据Bean
 * Created by Vigor on 2017/2/8.
 * Vigor_x studio (hsly_song@163.com)
 */
class CalculationItem {
    private String firstAddend;
    private String addend;
    private String result;

    public CalculationItem(String firstAddend, String addend, String result) {
        this.firstAddend = firstAddend;
        this.addend = addend;
        this.result = result;
    }

    public String getFirstAddend() {
        return firstAddend;
    }


    public String getAddend() {
        return addend;
    }


    public String getResult() {
        return result;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalculationItem that = (CalculationItem) o;

        if (!firstAddend.equals(that.firstAddend)) return false;
        if (!addend.equals(that.addend)) return false;
        return result.equals(that.result);

    }

    @Override
    public int hashCode() {
        int result1 = firstAddend.hashCode();
        result1 = 31 * result1 + addend.hashCode();
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }
}
