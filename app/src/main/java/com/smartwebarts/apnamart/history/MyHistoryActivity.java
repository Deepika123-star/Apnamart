package com.smartwebarts.apnamart.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smartwebarts.apnamart.R;
import com.smartwebarts.apnamart.models.OrderListModel;
import com.smartwebarts.apnamart.retrofit.UtilMethods;
import com.smartwebarts.apnamart.retrofit.mCallBackResponse;
import com.smartwebarts.apnamart.shared_preference.AppSharedPreferences;
import com.smartwebarts.apnamart.utils.MyGlide;
import com.smartwebarts.apnamart.utils.Toolbar_Set;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private HistoryAdapter2 adapter;
    private ImageView noitem;
    HashSet<String> orderid = new HashSet<>();
    HashMap<String, List<OrderListModel>> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_history);

        Toolbar_Set.INSTANCE.setToolbar(this, "My Orders");
        Toolbar_Set.INSTANCE.setBottomNav(this);

        recyclerView = findViewById(R.id.recyclerView);

        noitem = findViewById(R.id.imageView);
        setOrderData();
    }

    public void setOrderData() {
        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {

            AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
            String userid = preferences.getLoginUserLoginId();

            if (userid!=null && !userid.isEmpty()) {

                UtilMethods.INSTANCE.orderhistory(this, userid, new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {
                        Type type = new TypeToken<List<OrderListModel>>(){}.getType();
                        List<OrderListModel> list = new Gson().fromJson(message, type);
//                        List<OrderListModel> list = new ArrayList<>();
//                        adapter.setList(list);

                        if (list.size() <= 0) {
                            recyclerView.setVisibility(View.GONE);
                            noitem.setVisibility(View.VISIBLE);
                            MyGlide.with(MyHistoryActivity.this, getDrawable(R.drawable.noitem), noitem);
                        } else {
                            recyclerView.setVisibility(View.VISIBLE);
                            noitem.setVisibility(View.GONE);
                        }

                        parseList(list);
                    }

                    @Override
                    public void fail(String from) {
                    }
                });

            }

        } else {
            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }
    }

    private void parseList(List<OrderListModel> list) {

        for (OrderListModel o: list) {
            orderid.add(o.getOrderId());
        }
        Log.e("TOTAL ORDER IDS", orderid.size()+"");

        for (String order: orderid) {
            List<OrderListModel> list1 = new ArrayList<>();
            for (OrderListModel o : list) {
                if (o.getOrderId().equalsIgnoreCase(order)) {
                    list1.add(o);
                }
            }
            map.put(order, list1);
        }

        adapter = new  HistoryAdapter2(this, map, new ArrayList<>(orderid));
        recyclerView.setAdapter(adapter);

        System.out.println(map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toolbar_Set.INSTANCE.getCartList(this);
    }
}