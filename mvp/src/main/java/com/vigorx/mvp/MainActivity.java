package com.vigorx.mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by songlei on 16/9/29.
 * MVP模式利用的例子。
 */
public class MainActivity extends AppCompatActivity implements ICalculationContract.ICalculationView {
    private EditText mEditFirstAddend;
    private EditText mEditAddend;
    private TextView mTextResult;
    private RecordAdapter mAdapter;
    private ICalculationContract.ICalculationPresenter mPresenter;

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
                mPresenter.calculation();
            }
        });

        CalculationModel model = new CalculationModel();
        mPresenter = new CalculationPresenter(this, model);

        ListView listRecord = (ListView) findViewById(R.id.list_record);
        mAdapter = new RecordAdapter(this, R.layout.item_record, mPresenter.getRecordList());
        listRecord.setAdapter(mAdapter);
    }

    @Override
    public void displayResult(String result) {
        mTextResult.setText(result);
    }

    @Override
    public String getFirstAddend() {
        return mEditFirstAddend.getText().toString();
    }

    @Override
    public String getAddend() {
        return mEditAddend.getText().toString();
    }

    @Override
    public void refreshRecordList() {
        mAdapter.notifyDataSetChanged();
    }
}
