package priv.wind.recycleviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import priv.wind.recycleviewdemo.extend.ExtendAdapter;
import priv.wind.recycleviewdemo.extend.ExtendItemDecoration;
import priv.wind.recycleviewdemo.extend.RecyclerExtend;
import priv.wind.recycleviewdemo.form.FormView;

public class MainActivity extends AppCompatActivity {
    FormView mFormView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend);

        initDemo();
        //        init();
        //        initButton();
    }

    private void initDemo() {
        RecyclerExtend extend = findViewById(R.id.re_demo);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ExtendAdapter adapter = new ExtendAdapter(this, new ArrayList<String>());
        extend.setLayoutManager(manager);
        extend.addItemDecoration(new ExtendItemDecoration(this));
        extend.setAdapter(adapter);
    }

    private List<String> getStringData() {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            datas.add("3838382929天" + String.valueOf(i));
        }
        return datas;
    }

    private void initButton() {
        final ItemLabel itemLabel = new ItemLabel();
        itemLabel.no = "sss";
        Button btnOne = findViewById(R.id.btn_one);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                mFormView.addList(getData());
                mFormView.add(itemLabel);
            }
        });

        Button btnTwo = findViewById(R.id.btn_two);
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFormView.remove(itemLabel);
            }
        });

        Button btnThree = findViewById(R.id.btn_three);
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFormView.replaceDatas(getData());
            }
        });

        Button btnFour = findViewById(R.id.btn_four);
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFormView.clear();
            }
        });
    }

    private List<ItemLabel> getData() {
        List<ItemLabel> data = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            ItemLabel label = new ItemLabel();
            label.no = "GD13141516";
            label.itemCode = "13999990909";
            label.itemName = "黑色幽默" + i;
            label.location = "M110-110";
            label.qty = 11;
            label.unit = "克";
            data.add(label);
        }

        return data;
    }

    private void init() {
        mFormView = new FormView.FormBuilder<>(this, R.id.rv_detail, new ArrayList<ItemLabel>(), ItemLabel.class)
                //        mFormView = new FormView.FormBuilder<>(this, R.id.rv_detail, getData(), ItemLabel.class)
                .setEnableDelete()
                .setEnableSequence()
                .build();

    }
}
