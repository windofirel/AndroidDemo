package priv.wind.recycleviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        init();
        initData();
        initView();
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new MyAdapter<>(getData(), this);
    }

    private void initView() {
        mRecyclerView = ((RecyclerView) findViewById(R.id.rv_detail));
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new MyDivider(this));
        mRecyclerView.setAdapter(mAdapter) ;
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

    private ArrayList<String> getTitles(){
        ArrayList<String> titles = new ArrayList<>();
        titles.add("序号");
        titles.add("物料标签");
        titles.add("数量");
        titles.add("仓库编码");
        titles.add("货位编码");
        return titles;
    }

    private void init(){
        ItemLabel itemLabel = new ItemLabel();
        ListHelper helper = new ListHelper();
//        helper.getHeaderNames(itemLabel);
//        helper.getHeaderNames(itemLabel.getClass());
    }
}
