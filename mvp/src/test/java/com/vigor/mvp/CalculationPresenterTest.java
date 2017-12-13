package com.vigor.mvp;

import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vigor on 2017/2/8.
 * Vigor_x studio
 * hsly_song@163.com
 */
public class CalculationPresenterTest {

    @Mock
    Contract.IView mView;

    @Mock
    Contract.IModel mModel;

    private CalculationPresenter mPresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new CalculationPresenter(mView, mModel);
        when(mModel.add(anyString(), anyString())).thenReturn("3");
        when(mModel.getRecord()).thenReturn(Lists.<CalculationItem>newArrayList());
    }

    @After
    public void tearDown() throws Exception {

    }

/*    @Test
    public void addStudy() throws Exception {
        mPresenter.add("1", "2");
        mPresenter.add("3", "4");
        mPresenter.add("3", "4");
        mPresenter.add("4", "4");
        verify(mView, times(1)).showResult("3");
        verify(mView, times(2)).showResult("7");
        verify(mView, atLeastOnce()).showResult("8");
        verify(mView, never()).showResult("9");

        assertThat(mModel.getRecord(), contains(new CalculationItem("1", "2", "3"),
                new CalculationItem("3", "4", "7"),
                new CalculationItem("3", "4", "7"),
                new CalculationItem("4", "4", "8")));
    }*/

    @Test
    public void add() throws Exception {
        mPresenter.add("1", "2");
        verify(mView).showResult("3");
        verify(mView).showRecord(anyListOf(CalculationItem.class));
    }
}