package priv.wind.recycleviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FormView mFormView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        final ItemLabel itemLabel = new ItemLabel();
        itemLabel.no = "sss";
        Button btnOne = findViewById(R.id.btn_one);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                mFormView.updateData(getData());
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

    private void init(){
        mFormView = new FormView.FormBuilder<>(this, R.id.rv_detail, getData())
                .setEnableDelete()
                .setEnableSequence()
                .build();

    }
}
