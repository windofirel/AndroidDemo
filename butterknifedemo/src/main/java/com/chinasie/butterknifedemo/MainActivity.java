package com.chinasie.butterknifedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.et_hello)
    public EditText tvHello;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.et_1)
    EditText mEt1;

    @OnEditorAction(R.id.et_hello)
    public boolean enter() {
        Toast.makeText(this, "测试回车", Toast.LENGTH_LONG).show();
        tvHello.requestFocus();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        EditText et1 = ButterKnife.findById(this, R.id.et_1);
        et1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
    }

    @OnClick({R.id.btn_submit, R.id.et_1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                break;
            case R.id.et_1:
                break;
        }
    }
}
