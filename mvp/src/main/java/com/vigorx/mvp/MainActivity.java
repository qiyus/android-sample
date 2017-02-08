package com.vigorx.mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * MVP-View 负责处理UI
 * Created by Vigor on 2017/2/8.
 * Vigor_x studio (hsly_song@163.com)
 */
public class MainActivity extends AppCompatActivity implements Contract.IView {
    private EditText mEditFirstAddend;
    private EditText mEditAddend;
    private TextView mTextResult;
    private RecordAdapter mAdapter;
    private Contract.IPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditFirstAddend = (EditText) findViewById(R.id.first_addend);
        mEditAddend = (EditText) findViewById(R.id.addend);
        mTextResult = (TextView) findViewById(R.id.sum);
        Button mButtonAdd = (Button) findViewById(R.id.add_button);
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstAddend = mEditFirstAddend.getText().toString();
                String addend = mEditAddend.getText().toString();
                mPresenter.add(firstAddend, addend);
            }
        });

        CalculationModel model = new CalculationModel();
        mPresenter = new CalculationPresenter(this, model);

        ListView listRecord = (ListView) findViewById(R.id.list_record);
        mAdapter = new RecordAdapter(this, new ArrayList<CalculationItem>(0));
        listRecord.setAdapter(mAdapter);
    }

    @Override
    public void showResult(String result) {
        mTextResult.setText(result);
    }

    @Override
    public void showRecord(List<CalculationItem> record) {
        mAdapter.replaceData(record);
    }
}
