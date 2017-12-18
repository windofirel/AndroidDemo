package com.chinasie.demo;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LinearLayout view = ((LinearLayout) LayoutInflater.from(this).inflate(R.layout.dialog_test, null));

        Button btnTest = ((Button) findViewById(R.id.btn_test));
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("弹出框测试");
                builder.setView(view);
                builder.setPositiveButton("确定", null) ;
                builder.show();
            }
        });


    }
}
