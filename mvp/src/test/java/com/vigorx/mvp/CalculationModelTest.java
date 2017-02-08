package com.vigorx.mvp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;

/**
 * Created by Vigor on 2017/2/8.
 * Vigor_x studio (hsly_song@163.com)
 */
public class CalculationModelTest {

    private CalculationModel mModel;

    @Before
    public void setUp() throws Exception {
        mModel = new CalculationModel();
    }

    @Test
    public void add() throws Exception {
        String result = mModel.add("1", "2");
        assertThat(result, is("3"));
    }

    @Test
    public void getRecord() throws Exception {
        mModel.add("1", "2");
        assertThat(mModel.getRecord(), contains(new CalculationItem("1", "2", "3")));
    }

}